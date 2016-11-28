package com.kingbosky.commons.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingbosky.commons.util.Constants;
import com.kingbosky.commons.util.StringUtil;

public class AESUtil {
	private static final Logger logger = LoggerFactory.getLogger(AESUtil.class);
	private final static String HEX = "0123456789ABCDEF";

	public static byte[] encrypt(byte[] content, String pk) {
		if(content == null || StringUtil.isEmpty(pk)) return null;
		try {
			Key secureKey = new SecretKeySpec(pk.getBytes(Constants.DFT_CHARSET), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secureKey);
			byte[] encryptedData = cipher.doFinal(content);
			return encryptedData;
		} catch (Exception e) {
			logger.error("aes encrypt error, content:" + content + " pk:" + pk, e);
			return null;
		}
	}

	public static byte[] decrypt(byte[] content, String pk) {
		if(content == null || StringUtil.isEmpty(pk)) return null;
		try {
			Key secureKey = new SecretKeySpec(pk.getBytes(Constants.DFT_CHARSET), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secureKey);
			byte[] plainText = cipher.doFinal(content);
			return plainText;
		} catch (Exception e) {
			logger.error("aes decrypt error, content:" + content + " pk:" + pk, e);
			return null;
		}
	}
	
	public static String encryptHex(String content, String pk) {
		try {
			return toHex(encrypt(content.getBytes(Constants.DFT_CHARSET), pk));
		} catch (UnsupportedEncodingException e) {
			logger.error("aes encryptHex error, content:" + content + " pk:" + pk, e);
			return null;
		}
	}

	public static String decryptHex(String content, String pk) {
		try {
			return new String(decrypt(hexToByte(content), pk), Constants.DFT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			logger.error("aes decryptHex error, content:" + content + " pk:" + pk, e);
			return null;
		}
	}
	
	public static String encryptBase64(String content, String pk) {
		try {
			return Base64.encodeBase64String((encrypt(content.getBytes(Constants.DFT_CHARSET), pk)));
		} catch (UnsupportedEncodingException e) {
			logger.error("aes encryptBase64 error, content:" + content + " pk:" + pk, e);
			return null;
		}
	}

	public static String decryptBase64(String content, String pk) {
		try {
			return new String(decrypt(Base64.decodeBase64(content), pk), Constants.DFT_CHARSET);
		} catch (UnsupportedEncodingException e) {
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

	private static byte[] hexToByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		return result;
	}
}
