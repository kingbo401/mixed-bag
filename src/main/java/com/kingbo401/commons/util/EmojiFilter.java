package com.kingbo401.commons.util;


public class EmojiFilter {
	public static void main(String[] args) {
		System.out.println(filterEmoji("👪小北💭"));
		System.out.println(filterEmoji("用心🤔"));
		System.out.println((char)0x32FF);
	}

	public static boolean containsEmoji(String source) {
		if (StringTool.isBlank(source)) {
			return false;
		}
		int len = source.length();

		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isEmojiCharacter(codePoint)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isEmojiCharacter(char codePoint) {
		 return (codePoint >= 0x2600 && codePoint <= 0x27BF) // 杂项符号与符号字体
	                || codePoint == 0x303D
	                || codePoint == 0x2049
	                || codePoint == 0x203C
	                || (codePoint >= 0x2000 && codePoint <= 0x200F)//
	                || (codePoint >= 0x2028 && codePoint <= 0x202F)//
	                || codePoint == 0x205F //
	                || (codePoint >= 0x2065 && codePoint <= 0x206F)//
	                /* 标点符号占用区域 */
	                || (codePoint >= 0x2100 && codePoint <= 0x214F)// 字母符号
	                || (codePoint >= 0x2300 && codePoint <= 0x23FF)// 各种技术符号
	                || (codePoint >= 0x2B00 && codePoint <= 0x2BFF)// 箭头A
	                || (codePoint >= 0x2900 && codePoint <= 0x297F)// 箭头B
	                || (codePoint >= 0x3200 && codePoint <= 0x32FF)// 中文符号
	                || (codePoint >= 0xD800 && codePoint <= 0xDFFF)// 高低位替代符保留区域
	                || (codePoint >= 0xE000 && codePoint <= 0xF8FF)// 私有保留区域
	                || (codePoint >= 0xFE00 && codePoint <= 0xFE0F)// 变异选择器
	                || codePoint >= 0x10000; // Plane在第二平面以上的，char都不可以存，全部都转
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {
		if (StringTool.isBlank(source)) return "";
		if (!containsEmoji(source)) {
			return source;// 如果不包含，直接返回
		}
		StringBuilder buf = new StringBuilder(source.length());
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!isEmojiCharacter(codePoint)) {
				buf.append(codePoint);
			}
		}
		return buf.toString();
	}
}
