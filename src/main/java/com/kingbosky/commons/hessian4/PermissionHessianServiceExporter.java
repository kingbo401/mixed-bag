package com.kingbosky.commons.hessian4;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.kingbosky.commons.utils.SecurityUtils;
import com.kingbosky.commons.web.uitls.IPUtils;

public class PermissionHessianServiceExporter extends HessianServiceExporter{
	private final static Logger logger = LoggerFactory.getLogger(PermissionHessianServiceExporter.class);
	private String secretKey;
	private String whiteIps;
	private List<String> whiteIpList;
	public void handleRequest(HttpServletRequest request,  
            HttpServletResponse response) throws ServletException, IOException {
		//验证ip白名单
		if(!CollectionUtils.isEmpty(whiteIpList)){
			String ip = IPUtils.getIpAddr(request);
			if(!whiteIpList.contains(ip)){
				logger.error("ip:" + ip + " no access permission");
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		}
		//验证签名
		if(!StringUtils.isEmpty(secretKey)){
	        String sign = request.getHeader("Signature-Sign");
	        String timestamp = request.getHeader("Signature-Timestamp");
	        if (!SecurityUtils.md5(timestamp + secretKey).equals(sign)){
	        	logger.error("sign error:timestamp=" + timestamp + ",sign=" + sign);
	        	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	        	return;
	        }
		}
		
		 super.handleRequest(request, response);
    }

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getWhiteIps() {
		return whiteIps;
	}

	public void setWhiteIps(String whiteIps) {
		this.whiteIps = whiteIps;
		this.whiteIpList = getWhiteIpList();
	} 
	
	public List<String> getWhiteIpList(){
		if(!StringUtils.isEmpty(whiteIps)){
			return Arrays.asList(whiteIps.split(","));
		}
		return null;
	}
}
