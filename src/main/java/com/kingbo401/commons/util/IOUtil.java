package com.kingbo401.commons.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.kingbo401.commons.constant.Constants;
import com.kingbo401.commons.exception.MixedBagException;
public class IOUtil {

	private final static IOUtil instance = new IOUtil();
	private final static int PER_READ_BYTES = 4096;
	
	private IOUtil() {}
	
	public static IOUtil instance() {
		return instance;
	}
	
	public static void mkdirs(String path) {
		mkdirs(new File(path));
	}
	
	public static void mkdirs(File file) {
		if (!file.isDirectory() && !file.mkdirs())
			throw new MixedBagException("can't create directory " + file);
	}
	
	public static byte[] readFile(File file) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			int n;
			byte[] b = new byte[PER_READ_BYTES];
			while ((n = bis.read(b, 0, PER_READ_BYTES)) > 0) {
				baos.write(b, 0, n);
			}
		} catch (IOException e) {
			throw new MixedBagException("can't read file " + file, e);
		} finally {
			CloseUtil.close(bis);
		}
		return baos.toByteArray();
	}
	
	public static void writeFile(File file, byte[] fileBytes) {
		writeFile(file, fileBytes, false);
	}
	
	public static void writeFile(File file, byte[] fileBytes, boolean append) {
		BufferedOutputStream bos = null;
		try {
			mkdirs(file.getParentFile());
			bos = new BufferedOutputStream(new FileOutputStream(file, append));
			bos.write(fileBytes);
		} catch (IOException e) {
			throw new MixedBagException("can't write file " + file, e);
		} finally {
			CloseUtil.close(bos);
		}
	}
	
	public static void copyFile(File from, File to) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			mkdirs(to.getParentFile());
			bis = new BufferedInputStream(new FileInputStream(from));
			bos = new BufferedOutputStream(new FileOutputStream(to));
			int n;
			byte[] b = new byte[PER_READ_BYTES];
			while ((n = bis.read(b, 0, PER_READ_BYTES)) > 0) {
				bos.write(b, 0, n);
			}
		} catch (IOException e) {
			throw new MixedBagException("can't copy ", e);
		} finally {
			CloseUtil.close(bis, bos);
		}
	}
	
	public static List<String> readTextFileByLine(File textFile) {
		BufferedReader br = null;
		List<String> ret = new ArrayList<String>();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(textFile), Constants.DFT_CHARSET));
			String line;
			while ((line = br.readLine()) != null) {
				ret.add(StringUtil.getString(line));
			}
		} catch (Exception e) {
			throw new MixedBagException("can't read file" + textFile, e);
		} finally {
			CloseUtil.close(br);
		}
		return ret;
	}
	
	public static void writeTextFile(List<String> contents, File outputFile) {
		BufferedWriter bw = null;
		try {
			mkdirs(outputFile.getParentFile());
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile, false), Constants.DFT_CHARSET));
			for (String line : contents) {
				bw.write(line);
				bw.newLine();
			}
		} catch (Exception e) {
			throw new MixedBagException("write file failed " + outputFile, e);
		} finally {
			CloseUtil.close(bw);
		}
	}
	
	private static final int CONTENT_MAX_READ_TIMES = 5;
	public static byte[] readBytes(InputStream is, int maxLength) {
		return readBytes(is, maxLength, true);
	}
	public static byte[] readBytes(InputStream is, int maxLength, boolean closeFlag) {
		byte[] data = new byte[maxLength];
		try {
			int readLength = is.read(data, 0, maxLength);
			int count = 0;
			while (readLength < maxLength) {
				//读取次数超过最大设置读取次数时还没有读取全部请求内容，返回错误
				if ((++count) >= CONTENT_MAX_READ_TIMES)
					throw new MixedBagException("can't get full request content(" + readLength + "," + maxLength + ")");
				readLength += is.read(data, readLength, maxLength - readLength);
			}
		} catch (IOException e) {
			throw new MixedBagException("获得请求内容发生异常", e);
		} finally {
			if (closeFlag) CloseUtil.close(is);
		}
		return data;
	}
	
	public static byte[] readBytes(InputStream is) {
		return readBytes(is, true);
	}
	
	public static byte[] readBytes(InputStream is, boolean closeFlag) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[4096];
		int n;
		try {
			while ((n = is.read(b, 0, b.length)) != -1) {
				baos.write(b, 0, n);
			}
		} catch (IOException e) {
			throw new MixedBagException("read input stream error.", e);
		} finally {
			if (closeFlag) CloseUtil.close(is);
		}
		
		return baos.toByteArray();
	}
}
