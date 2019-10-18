package com.kingbo401.commons.web.util;

import java.nio.charset.CodingErrorAction;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.commons.util.NumberUtil;
import com.kingbo401.commons.util.PropertiesLoader;
import com.kingbo401.commons.util.StringUtil;

public class HttpClientUtil {
	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	// http client
	public static PoolingHttpClientConnectionManager httpConnManager = null;
	private static CloseableHttpClient httpClient = null;
	private static int POOL_MAX_TOTAL = 1000;
	private static int POOL_MAX_PER_ROUTE = 100;
	private static int KEEP_ALIVE = 5000;

	static {
		initHttpClient();
	}
	
	/**
	 * 初始化httpClient对象
	 */
	private static void initHttpClient() {
		try {
			Properties properties = PropertiesLoader.load("httpClientUtil.properties");
			if(properties != null){
				int poolMaxTotal = NumberUtil.toInt(properties.getProperty("POOL_MAX_TOTAL"));
				if(poolMaxTotal > 0){
					POOL_MAX_TOTAL = poolMaxTotal;
				}
				int poolMaxPerRoute = NumberUtil.toInt(properties.getProperty("POOL_MAX_PER_ROUTE"));
				if(poolMaxPerRoute > 0){
					POOL_MAX_PER_ROUTE = poolMaxPerRoute;
				}
				int keepAlive = NumberUtil.toInt(properties.getProperty("KEEP_ALIVE"));
				if(keepAlive > 0){
					KEEP_ALIVE = keepAlive;
				}
			}
			
			SSLContext sslContext = SSLContexts.custom().useProtocol("TLS").build();
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} }, null);
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", new SSLConnectionSocketFactory(sslContext,
							new HostnameVerifier(){
								@Override
								public boolean verify(String hostname,
										SSLSession session) {
									return true;
								}//解决https请求 hostname in certificate didn't match的问题
					})).build();
			httpConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, null, null , null, 60L, TimeUnit.SECONDS);
			// Create socket configuration
			SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).setSoKeepAlive(true)
					.setSoReuseAddress(true).build();
			httpConnManager.setDefaultSocketConfig(socketConfig);
			// Create message constraints
			MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
					.setMaxLineLength(2000).build();
			// Create connection configuration
			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setMalformedInputAction(CodingErrorAction.IGNORE)
					.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
					.setMessageConstraints(messageConstraints).build();
			httpConnManager.setDefaultConnectionConfig(connectionConfig);

			httpConnManager.setMaxTotal(POOL_MAX_TOTAL);
			httpConnManager.setDefaultMaxPerRoute(POOL_MAX_PER_ROUTE);
			
			ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
				@Override
				public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
					long keepAlive = super.getKeepAliveDuration(response, context);
					if (keepAlive == -1) {
						// Keep connections alive KEEP_ALIVE milliseconds if a
						// keep-alive value has not be explicitly set by the
						// server
						keepAlive = KEEP_ALIVE;
					}
					return keepAlive;
				}
			};
			
			httpClient = HttpClients.custom().setConnectionManager(httpConnManager).setKeepAliveStrategy(keepAliveStrategy).build();
			IdleConnectionMonitorThread monitorThread = new HttpClientUtil.IdleConnectionMonitorThread(httpConnManager);
			monitorThread.start();
		} catch (Exception e) {
			logger.error("Init HttpClient Exception:", e);
		}
	}
	
	public static String get(String url, Map<String, String> params, String encode, int connectTimeout,
			int soTimeout) {
		return get(url, params, null, encode, connectTimeout, soTimeout);
	}
	public static String get(String url, Map<String, String> params,
			Map<String, String> headers, String encode, int connectTimeout,
			int soTimeout) {
		StringBuilder logMsg = new StringBuilder();
		logMsg.append("url : ").append(url).append(", params: ").append(ParamUtil.getKeyAndValueStr(params)).append(", headers : ").append(headers);
		StringBuilder fullUrl = new StringBuilder(url);
		if(!CollectionUtil.isEmpty(params)){
			fullUrl.append("?").append(ParamUtil.getKeyAndValueStr(params, true));
		}
		HttpGet httpGet = new HttpGet(fullUrl.toString());
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(soTimeout).setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout).build();
		httpGet.setConfig(config);

		if (!CollectionUtil.isEmpty(headers)) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpGet.setHeader(entry.getKey(), entry.getValue());
			}
		}
		try {
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();
				String result = null;
				if (entity != null) {
					result = EntityUtils.toString(entity, Consts.UTF_8);
				}
				logger.info("HttpUtils HttpClient Get Result : " + logMsg
						+ ", Response status code : " + statusCode
						+ ", Response content : " + result);
				return result;
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (Exception e) {
			httpGet.abort();
			logger.error("HttpClient Get error :" + logMsg, e);
		}

		return null;
	}
	/**
	 * 
	 * @param url
	 * @param content
	 *            要post的字符串
	 * @param encode
	 * @param connectTimeout
	 * @param soTimeout
	 * @return
	 */
	public static String post(String url, String content, String encode, int connectTimeout, int soTimeout) {
		return post(url, content, null, encode, connectTimeout, soTimeout);
	}

	/**
	 * 
	 * @param url
	 * @param content
	 *            要post的字符串
	 * @param headers
	 * @param encode
	 * @param connectTimeout
	 * @param soTimeout
	 * @return
	 */
	public static String post(String url, String content, Map<String, String> headers, String encode,
			int connectTimeout, int soTimeout) {
		if (content == null || content.length() == 0) {
			throw new IllegalArgumentException();
		}
		StringBuilder logMsg = new StringBuilder();
		logMsg.append("url : ").append(url).append(", params : ")
				.append(content).append(", headers : ").append(headers);
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(soTimeout)
				.setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout)
				.setExpectContinueEnabled(false).build();
		httpPost.setConfig(requestConfig);

		if (!CollectionUtil.isEmpty(headers)) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
		}
		encode = StringUtil.isEmpty(encode) ? Consts.UTF_8.name() : encode;

		try {
			HttpEntity requestEntity = new StringEntity(content, encode);
			httpPost.setEntity(requestEntity);
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(httpPost);
				int statusCode = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();
				String result = null;
				if (entity != null) {
					result = EntityUtils.toString(entity, Consts.UTF_8);
				}
				logger.info("HttpClient POST Result : " + logMsg  
						+ ", Response status code : " + statusCode 
						+ ", Response content : " + result);
				return result;
			} finally {
				if (response != null) {
					response.close();
				}
			}
		}catch (Exception e) {
			httpPost.abort();
			logger.error("HttpClient Post error : " + logMsg, e);
		}
		return null;
	}

	public static String post(String url, Map<String, String> params, String encode, int connectTimeout,
			int soTimeout){
		return post(url, params, null, encode, connectTimeout, soTimeout);
	}
	public static String post(String url, Map<String, String> params,
			Map<String, String> headers, String encode, int connectTimeout,
			int soTimeout) {
		StringBuilder logMsg = new StringBuilder();
		logMsg.append("url : ").append(url).append(", params : ")
				.append(ParamUtil.getKeyAndValueStr(params)).append(", headers : ").append(headers);

		List<NameValuePair> paramPairs = new ArrayList<NameValuePair>();
		if (!CollectionUtil.isEmpty(params)) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				BasicNameValuePair param = new BasicNameValuePair(
						entry.getKey(), String.valueOf(entry.getValue()));
				paramPairs.add(param);
			}
		}
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(soTimeout).setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectTimeout)
				.setExpectContinueEnabled(false).build();
		httpPost.setConfig(requestConfig);

		if (!CollectionUtil.isEmpty(headers)) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
		}

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(paramPairs, StringUtil
					.isEmpty(encode) ? Consts.UTF_8.name() : encode));
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(httpPost);
				int statusCode = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();
				String result = null;
				if (entity != null) {
					result = EntityUtils.toString(entity, Consts.UTF_8);
				}
				logger.info("HttpClient POST Result : " + logMsg
						+ ", Response status code : " + statusCode
						+ ", Response content : " + result);
				return result;
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (Exception e) {
			httpPost.abort();
			logger.error("HttpClient POST error :" + logMsg, e);
		} 
		return null;
	}
	
	
	private static class IdleConnectionMonitorThread extends Thread{
		
		private final HttpClientConnectionManager connMgr;
	    private Logger logger = LoggerFactory.getLogger(IdleConnectionMonitorThread.class);
	    
	    IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
	        super();
	        super.setDaemon(true);
	        this.connMgr = connMgr;
	    }

	    @Override
	    public void run() {
	        try {
	        	while (true) {
	                synchronized (this) {
	                    TimeUnit.SECONDS.sleep(5);
	                    // 关闭失效的连接
	                    connMgr.closeExpiredConnections();
	                    // 可选的, 关闭30秒内不活动的连接
	                    connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
	                }
	            }
	        } catch (InterruptedException ex) {
	        	logger.info("IdleConnectionMonitorThread InterruptedException exits!");
	        }
	    }

	}

}
