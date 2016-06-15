package com.kingbosky.commons.web.uitls;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class IPUtils {
	private static final String ipPattern = "^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$";   
	
	public static boolean isValidIp(String ip){
		return Pattern.compile(ipPattern).matcher(ip).find();   
	}
	
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static long toLong(String ip) {
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
