package com.kingbosky.commons.web.uitls;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

	/**
	 * 添加cookie
	 * @param response
	 * @param domain       域
	 * @param key          键
	 * @param value        值
	 * @param expiry       过期时间[单位：/s]
	 * @return
	 */
	public static void addCookie(HttpServletResponse response, String domain, String key, String value, int expiry) {
		Cookie cookie = new Cookie(key, value); //will be throw new IllegalArgumentException(errMsg);
		cookie.setPath("/"); // very important
		cookie.setDomain(domain);
		cookie.setMaxAge(expiry);
		response.setHeader("P3P", "CP=\"IDC DSP COR CURa ADMa OUR IND PHY ONL COM STA\"");
		response.addCookie(cookie);
	}

	/**
	 * 取一个cookie
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if ((cookies == null) || (cookies.length == 0)) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (key.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
	public static void removeCookie(HttpServletResponse response, String domain, String key){
		Cookie cookie = new Cookie(key, "");
		cookie.setPath("/"); // very important
		cookie.setDomain(domain);
		cookie.setMaxAge(0);
		response.setHeader("P3P", "CP=\"IDC DSP COR CURa ADMa OUR IND PHY ONL COM STA\"");
		response.addCookie(cookie);
	}
}
