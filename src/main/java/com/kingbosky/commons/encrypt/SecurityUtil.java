package com.kingbosky.commons.encrypt;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingbosky.commons.util.HexUtil;

public final class SecurityUtil {

	private final static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

	/**
	 * 唯一识别码
	 * @return
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		StringBuilder sb = new StringBuilder();
		sb.append(uuid.substring(0, 8));
		sb.append(uuid.substring(9, 13));
		sb.append(uuid.substring(14, 18));
		sb.append(uuid.substring(19, 23));
		sb.append(uuid.substring(24));
		return sb.toString();
	}

	/**
	 * MD5加密
	 * @param source
	 * @return
	 */
	public static String md5(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes("UTF-8"));
			byte bytes[] = md.digest();
			return HexUtil.toHexString(bytes);
		} catch (Exception e) {
			logger.error("md5 Exceptoion", e);
		}
		return null;
	}
	
	/**
	 * 安全哈希签名
	 * @param source
	 * @return
	 */
	public static String sha1(String source){
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			byte[] bytes = messageDigest.digest(source.getBytes("UTF-8"));
			return HexUtil.toHexString(bytes);
		} catch (Exception e) {
			logger.error("sha1 Exceptoion", e);
		}
		return null;
	}
	
	/**
	 * HmacMD5
	 * @param key
	 * @param params
	 * @return
	 */
	public static String hmacMD5(String key, String... strs){
		try {
			Mac mac = Mac.getInstance("HmacMD5");
	        mac.init(new SecretKeySpec(key.getBytes(), "HmacMD5"));
	        for(String str : strs){
	            mac.update(str.getBytes());
	        }
	        return HexUtil.toHexString(mac.doFinal()); 
		} catch (Exception e) {
			logger.error("hmacMD5 Exceptoion", e);
			return "";
		}
	}

	public static String desEncrypt(String str, String key){
		try{
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
			byte[] bytes = cipher.doFinal(str.getBytes());
			String base64 = Base64.encodeBase64String(bytes);
			return base64;
		}catch(Exception e){
			logger.error("desEncrypt Exceptoion", e);
			return null;
		}
	}

	public static String desDecrypt(String entry, String key){
		try{
			byte[] bytes = Base64.decodeBase64(entry);
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
			byte[] ret = cipher.doFinal(bytes);
			String str = new String(ret, "UTF-8");
			return str;
		}catch(Exception e){
			logger.error("desDecrypt Exceptoion", e);
			return null;
		}
	}
}
