package com.kingbo401.commons.web.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingbo401.commons.constant.Constants;
import com.kingbo401.commons.exception.MixedBagException;
import com.kingbo401.commons.util.StringUtil;

/**
 * 简版http工具类
 *
 * @author kingbo401
 * @date 2019/07/20
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static String get(String url, Map<String, String> params, Map<String, String> headers, String encode, int connectTimeout,
			int soTimeout){
    	return request(url, "GET", params, headers, encode, connectTimeout, soTimeout);
    }
    
    public static String get(String url, Map<String, String> params, String encode, int connectTimeout,
			int soTimeout){
    	return get(url, params, null, encode, connectTimeout, soTimeout);
    }
    
    public static String post(String url, Map<String, String> params, Map<String, String> headers, String encode, int connectTimeout,
			int soTimeout){
    	return request(url, "POST", params, headers, encode, connectTimeout, soTimeout);
    }
    
    public static String post(String url, Map<String, String> params, String encode, int connectTimeout,
			int soTimeout){
    	return post(url, params, null, encode, connectTimeout, soTimeout);
    }
    
	@SuppressWarnings("restriction")
    private static String request(String url, String requestMethod, Map<String, String> params, Map<String, String> headers, String encode, int connectTimeout,
			int soTimeout){
        BufferedReader r = null;
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            if ((connection instanceof sun.net.www.protocol.https.HttpsURLConnectionImpl)) {
                ((sun.net.www.protocol.https.HttpsURLConnectionImpl) connection).setSSLSocketFactory(createSSLSocketFactory());
                ((sun.net.www.protocol.https.HttpsURLConnectionImpl) connection).setHostnameVerifier(new TrustAnyHostnameVerifier());
            }
            connection.setRequestProperty("Connection", "close");
            if(headers != null && !headers.isEmpty()){
            	for(Map.Entry<String, String> entry : headers.entrySet()){
            		connection.setRequestProperty(entry.getKey(), entry.getValue());
            	}
            }
            encode = StringUtil.isEmpty(encode) ? Consts.UTF_8.name() : encode;
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(soTimeout);
            if (params != null && !params.isEmpty()) {
                connection.setRequestMethod(requestMethod);
                String datas = ParamUtil.getKeyAndValueStr(params, true);
                connection.getOutputStream().write(datas.toString().getBytes(encode));
                connection.getOutputStream().flush();
                connection.getOutputStream().close();
            }
            int retCode = connection.getResponseCode();
            InputStream inputStream = null;
            if (retCode >= 400) {
                inputStream = connection.getErrorStream();
            } else {
                inputStream = connection.getInputStream();
            }
            String result = "";
            if (inputStream != null) {
                r = new BufferedReader(new InputStreamReader(inputStream, Constants.DFT_CHARSET));
                result = read(r);
            }
            return result;
        } catch (Exception ex) {
        	logger.error(ex.getMessage(), ex);
        	throw new MixedBagException(ex);
        }  finally {
            try {
                if (r != null) {
                    r.close();
                }
            } catch (IOException ex) {
                // ignore
            }
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() throws NoSuchAlgorithmException, NoSuchProviderException,
                                                            KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new SecureRandom());
        return sslContext.getSocketFactory();
    }

    private static String read(Reader reader) {
        try {
            final int DEFAULT_BUFFER_SIZE = 1024 * 4;

            StringWriter writer = new StringWriter();

            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }

            return writer.toString();
        } catch (IOException ex) {
            throw new IllegalStateException("read error", ex);
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
