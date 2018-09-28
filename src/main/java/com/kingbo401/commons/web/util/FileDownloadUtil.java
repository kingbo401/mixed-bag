package com.kingbo401.commons.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FileDownloadUtil {
	public static String buildFileName(String name, String userAgent){
		String fileName = null;
		try {
			fileName = URLEncoder.encode(name, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
		String result = "filename=\"" + fileName + "\"";
		if (userAgent != null) {
			userAgent = userAgent.toLowerCase();
			// IE浏览器，只能采用URLEncoder编码
			if (userAgent.indexOf("msie") != -1) {
				result = "filename=\"" + fileName + "\"";
			}
			// Opera浏览器只能采用filename*
			else if (userAgent.indexOf("opera") != -1) {
				result = "filename*=UTF-8''" + fileName;
			}
			// Safari浏览器，只能采用ISO编码的中文输出
			else if (userAgent.indexOf("safari") != -1) {
				try {
					result = "filename=\""
							+ new String(name.getBytes("UTF-8"), "ISO8859-1")
							+ "\"";
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			// Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
			else if (userAgent.indexOf("applewebkit") != -1) {
				result = "filename=\"" + fileName + "\"";
			}
			// FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
			else if (userAgent.indexOf("mozilla") != -1) {
				result = "filename*=UTF-8''" + fileName;
			}
		}
		return result;
	}
}
