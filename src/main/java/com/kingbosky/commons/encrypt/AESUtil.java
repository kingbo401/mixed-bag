package com.kingbosky.commons.encrypt;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.kingbosky.commons.exception.GeneralException;
import com.kingbosky.commons.util.Constants;
import com.kingbosky.commons.util.StringUtil;

public class AESUtil {

	private final static String HEX = "0123456789ABCDEF";

	public static String encrypt(String content, String pk) {
		try {
			Key secureKey = new SecretKeySpec(pk.getBytes(Constants.DFT_CHARSET), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secureKey);
			byte[] encryptedData = cipher.doFinal(content.getBytes(Constants.DFT_CHARSET));
			return toHex(encryptedData);
		} catch (Exception e) {
			throw new GeneralException(StringUtil.join(", ", "AES加密异常", content, pk), e);
		}
	}

	public static String decrypt(String content, String pk) {
		try {
			Key secureKey = new SecretKeySpec(pk.getBytes(Constants.DFT_CHARSET), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secureKey);
			byte[] plainText = cipher.doFinal(toByte(content));
			return StringUtil.newString(plainText);
		} catch (Exception e) {
			throw new GeneralException("AES解密异常 content:" + content + "pk" + pk, e);
		}
	}

	private static String toHex(byte[] buf) {
		if (buf == null) return Constants.DFT_STRING_VAL;
		StringBuilder result = new StringBuilder(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			result.append(HEX.charAt((buf[i] >> 4) & 0x0f)).append(HEX.charAt(buf[i] & 0x0f));
		}
		return result.toString();
	}

	private static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		return result;
	}
}
