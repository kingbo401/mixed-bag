package com.kingbo401.commons.web.util;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.kingbo401.commons.util.StringTool;

public class IPUtil {
	private static final String ipPattern = "^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$";   
	
	public static boolean isValidIp(String ip){
		return Pattern.compile(ipPattern).matcher(ip).find();   
	}
	
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringTool.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringTool.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringTool.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringTool.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringTool.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static long toLong(String ip) {
		if(!isValidIp(ip)) return 0;
		long result = 0;
		StringTokenizer token = new StringTokenizer(ip, ".");
		result |= Long.parseLong(token.nextToken()) << 24;
		result |= Long.parseLong(token.nextToken()) << 16;
		result |= Long.parseLong(token.nextToken()) << 8;
		result |= Long.parseLong(token.nextToken());
		return result;
	}

	public static String toString(long ip) {
		StringBuilder sb = new StringBuilder();
		sb.append(ip >>> 24);
		sb.append(".");
		sb.append(String.valueOf((ip & 0x00FFFFFF) >>> 16));
		sb.append(".");
		sb.append(String.valueOf((ip & 0x0000FFFF) >>> 8));
		sb.append(".");
		sb.append(String.valueOf(ip & 0x000000FF));
		return sb.toString();
	}
}
