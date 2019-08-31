package com.kingbo401.commons.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

import com.kingbo401.commons.exception.MixedBagException;
import com.kingbo401.commons.util.Constants;
import com.kingbo401.commons.util.HexUtil;
import com.kingbo401.commons.util.StringUtil;

/**
 * MD5加密
 *
 * @author kingbo401
 * @date 2019/07/20
 */
public class MD5Util {

	private static final MD5Util instance = new MD5Util();
	
	private static final String DFT_ENC_NAME = "MD5";
	
	private MD5Util() {}
	
	public static MD5Util instance() {
		return instance;
	}
	
	/**
	 * 加密字符串
	 * @param content 待加密字符串
	 * @param encName 加密密钥，默认值MD5
	 * @return 加密后结果
	 */
	public static String encrypt(String content) {
		if (StringUtil.isEmpty(content))
			throw new MixedBagException("Empty MD5 source content.");
		return encrypt0(content, Constants.DFT_CHARSET);
	}
	
	public static String encrypt(String content, String charset) {
			if (StringUtil.isEmpty(content))
				throw new MixedBagException("Empty MD5 source content.");
		if (StringUtil.isEmpty(charset)) charset = Constants.DFT_CHARSET;
		return encrypt0(content, charset);
	}
	
	private static String encrypt0(String content, String charset) {
		try {
			MessageDigest md = MessageDigest.getInstance(DFT_ENC_NAME);
			md.update(content.getBytes(charset));
			return HexUtil.toHexString(md.digest());
		} catch (Exception e) {
			throw new MixedBagException(e);
		}
	}
	
	public static String encrypt(byte[] bytes) {
		if (bytes == null || bytes.length == 0)
			throw new MixedBagException("Empty MD5 source bytes.");
		try {
			MessageDigest md = MessageDigest.getInstance(DFT_ENC_NAME);
			md.update(bytes);
			return HexUtil.toHexString(md.digest());
		} catch (Exception e) {
			throw new MixedBagException("MD5 bytes Failed.", e);
		}
	}
	
	public static String fileMd5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance(DFT_ENC_NAME);
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
}
