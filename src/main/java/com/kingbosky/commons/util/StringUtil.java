package com.kingbosky.commons.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.kingbosky.commons.exception.GeneralException;

/**
 * 字符串工具类
 */
public class StringUtil {
	/** 单例 */
	private static final StringUtil instance = new StringUtil();
	/** 随机数对象 */
	private static final Random random = new Random();
	/** 数字与字母字典 */
	private static final char[] LETTER_AND_DIGIT = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	/** 数字与字母字典长度 */
	private static final int LETTER_AND_DIGIT_LENGTH = LETTER_AND_DIGIT.length;
	
	private StringUtil() {}
	
	/**
	 * 取得该类唯一实例
	 * @return 该类唯实例
	 */
	public static StringUtil instance() {
		return instance;
	}
	
	/**
	 * 检测字符串是否为空字符串
	 * 字符串为空的标准：null或全部由空字符组成的字符串
	 * @param input 待检测字符串
	 * @return
	 * <li>true：字符串是空字符串</li>
	 * <li>false：字符串不是空字符串</li>
	 */
	public static boolean isEmpty(String input) {
		return (input == null || input.trim().length() == 0);
	}
	
	public static boolean isNotEmpty(String input) {
		return (input != null && input.trim().length() > 0);
	}
	
	/**
	 * 将对象转换为字符串
	 * @param input 待转换对象
	 * @return 转换后的字符串
	 * @see #getString(Object, String)
	 * @see #getString(String)
	 * @see #getString(String, String)
	 * @see com.nx.commons.lang.NxConsts#DFT_STRING_VAL
	 */
	public static String getString(Object input) {
		return getString(input, Constants.DFT_STRING_VAL);
	}
	
	/**
	 * 将对象转换为字符串
	 * @param input 待转换对象
	 * @param defVal 对象转换为空字符串时的默认返回值
	 * @return 转换后的字符串
	 * @see #getString(String)
	 * @see #getString(String, String)
	 */
	public static String getString(Object input, String defVal) {
		return (input == null) ? defVal : getString(input.toString(), defVal);
	}
	
	/**
	 * 转换字符串
	 * @param input 待转换字符串
	 * @return 转换后的字符串
	 * @see #getString(String, String)
	 */
	public static String getString(String input) {
		return getString(input, Constants.DFT_STRING_VAL);
	}
	
	/**
	 * 转换字符串
	 * @param input 待转换字符串
	 * @param defVal 默认转换值
	 * @return 转换后的字符串
	 * <li>字符串为null或全部由空白字符组成的字符串时，返回defVal参数指定的值</li>
	 * <li>其他情况，返回去掉字符串两端空白字符后的字符串</li>
	 */
	public static String getString(String input, String defVal) {
		return (isEmpty(input)) ? defVal : input.trim();
	}
	
	/**
	 * 生成固定长度的随机字符串
	 * @param len 随机字符串长度
	 * @return 生成的随机字符串
	 */
	public static String getRandomString(final int len) {
		if (len < 1) return "";
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(LETTER_AND_DIGIT[random.nextInt(LETTER_AND_DIGIT_LENGTH)]);
		}
		return sb.toString();
	}
	
	/**
	 * 生成固定长度的随机字符串
	 * @param len 随机字符串长度
	 * @param dictionary 字符串字典
	 * @return 生成的随机字符串
	 */
	public static String getRandomString(final int len, char[] dictionary) {
		if (len < 1) return "";
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(dictionary[random.nextInt(dictionary.length)]);
		}
		return sb.toString();
	}
	
	public static String newString(byte[] bytes) {
		return newString(bytes, Constants.DFT_CHARSET);
	}
	
	public static String newString(byte[] bytes, String charset) {
		try {
			return new String(bytes, charset);
		} catch (UnsupportedEncodingException e) {
			throw new GeneralException("不支持的字符集:" + charset, e);
		}
	}
	
	public static byte[] getBytes(String input) {
		return getBytes(input, Constants.DFT_CHARSET);
	}
	
	public static byte[] getBytes(String input, String charset) {
		if (input == null) return null;
		try {
			return input.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new GeneralException("不支持的字符集:" + charset, e);
		}
	}
	
	/**
     * <p>Uncapitalizes a String changing the first letter to title case as
     * per {@link Character#toLowerCase(char)}. No other letters are changed.</p>
     *
     * <pre>
     * StringUtils.uncapitalize(null)  = null
     * StringUtils.uncapitalize("")    = ""
     * StringUtils.uncapitalize("Cat") = "cat"
     * StringUtils.uncapitalize("CAT") = "cAT"
     * </pre>
     *
     * @param str  the String to uncapitalize, may be null
     * @return the uncapitalized String, <code>null</code> if null String input
     */
    public static String uncapitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen)
            .append(Character.toLowerCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
    
    public static <T> String joinList(String seperator, List<T> params) {
    	if (params == null || params.size() == 0) return Constants.DFT_STRING_VAL;
		if (seperator == null) seperator = Constants.DFT_STRING_VAL;
		StringBuilder ret = new StringBuilder();
		for (Object param : params) {
			if (ret.length() > 0) ret.append(seperator);
			ret.append(param);
		}
		return ret.toString();
    }
    
	public static String join(String seperator, Object... params) {
		if (params == null || params.length == 0) return Constants.DFT_STRING_VAL;
		if (seperator == null) seperator = Constants.DFT_STRING_VAL;
		StringBuilder ret = new StringBuilder();
		for (Object param : params) {
			if (ret.length() > 0) ret.append(seperator);
			ret.append(param);
		}
		return ret.toString();
	}
	
	public static String joinArray(String seperator, Object[] params) {
		if (params == null || params.length == 0) return Constants.DFT_STRING_VAL;
		
		StringBuilder ret = new StringBuilder();
		for (Object param : params) {
			if (ret.length() > 0) ret.append(seperator);
			ret.append(param);
		}
		return ret.toString();
	}
	
	public static int compare(String str1, String str2) {
		if (str1 == null && str2 == null) return 0;
		if (str1 == null) return -1;
		if (str2 == null) return 1;
		
		int len1 = str1.length();
		int len2 = str2.length();
		int len = Math.min(len1, len2);
		for (int i = 0; i < len; i++) {
			char ch1 = str1.charAt(i);
			char ch2 = str2.charAt(i);
			if (ch1 < ch2) return -1;
			if (ch1 > ch2) return 1;
		}
		
		if (len1 == len2) return 0;
		return (len1 < len2) ? -1 : 1;
	}
	
	public static String toString(Object obj) {
		return ToStringBuilder.reflectionToString(obj, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public static String replaceEmoji(String source, String s) {
		if (source != null) {
			Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
					Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
			Matcher emojiMatcher = emoji.matcher(source);
			if (emojiMatcher.find()) {
				source = emojiMatcher.replaceAll(s);
				return source;
			}
			return source;
		}
		return source;
	}
}
