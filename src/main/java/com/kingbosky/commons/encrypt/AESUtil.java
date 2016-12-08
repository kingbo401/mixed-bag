package com.kingbosky.commons.encrypt;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingbosky.commons.util.Constants;

public class AESUtil {
	private static final Logger logger = LoggerFactory.getLogger(AESUtil.class);
	private final static String HEX = "0123456789ABCDEF";

	public static byte[] encrypt(byte[] content, String pk) {
		if(content == null || StringUtils.isBlank(pk)) return null;
		try {
			Key secretKey = new SecretKeySpec(pk.getBytes(Constants.DFT_CHARSET), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedData = cipher.doFinal(content);
			return encryptedData;
		} catch (Exception e) {
			logger.error("aes encrypt error, content:" + content + " pk:" + pk, e);
			return null;
		}
	}

	public static byte[] decrypt(byte[] content, String pk) {
		if(content == null || StringUtils.isBlank(pk)) return null;
		try {
			Key secretKey = new SecretKeySpec(pk.getBytes(Constants.DFT_CHARSET), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] plainText = cipher.doFinal(content);
			return plainText;
		} catch (Exception e) {
			logger.error("aes decrypt error, content:" + content + " pk:" + pk, e);
			return null;
		}
	}
	
	public static String encryptHex(String content, String pk) {
		try {
			byte[] b = encrypt(content.getBytes(Constants.DFT_CHARSET), pk);
			if(b == null) return null;
			return toHex(b);
		} catch (Exception e) {
			logger.error("aes encryptHex error, content:" + content + " pk:" + pk, e);
			return null;
		}
	}

	public static String decryptHex(String content, String pk) {
		try {
			byte[] b = decrypt(hexToByte(content), pk);
			if(b == null) return null;
			return new String(b, Constants.DFT_CHARSET);
		} catch (Exception e) {
			logger.error("aes decryptHex error, content:" + content + " pk:" + pk, e);
			return null;
		}
	}
	
	public static String encryptBase64(String content, String pk) {
		try {
			byte[] b = encrypt(content.getBytes(Constants.DFT_CHARSET), pk);
			if(b == null) return null;
			return Base64.encodeBase64String(b);
		} catch (Exception e) {
			logger.error("aes encryptBase64 error, content:" + content + " pk:" + pk, e);
			return null;
		}
	}

	public static String decryptBase64(String content, String pk) {
		try {
			byte[] b = decrypt(Base64.decodeBase64(content), pk);
			if(b == null) return null;
			return new String(b, Constants.DFT_CHARSET);
		} catch (Exception e) {
			logger.error("aes decryptBase64 error, content:" + content + " pk:" + pk, e);
			return null;
		}
	}

	private static String toHex(byte[] buf) {
		if (buf == null) return null;
		StringBuilder result = new StringBuilder(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			result.append(HEX.charAt((buf[i] >> 4) & 0x0f)).append(HEX.charAt(buf[i] & 0x0f));
		}
		return result.toString();
	}

	private static byte[] hexToByte(String hexStr) {
		if(hexStr == null) return null;
		int len = hexStr.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexStr.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		return result;
	}
	
	public static void main(String[] args) {
		String content = "{}";
		String key ="lwfcxxxwmddfjpqc";
		String encryptStr = encryptHex(content, key);
		System.out.println(encryptStr);
		System.out.println(decryptHex(encryptStr, key));
		
		encryptStr = encryptBase64(content, key);
		System.out.println(encryptStr);
		System.out.println(decryptBase64(encryptStr, key));
	}
}
