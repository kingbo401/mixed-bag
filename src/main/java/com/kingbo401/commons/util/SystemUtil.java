package com.kingbo401.commons.util;

public class SystemUtil {
	
	private static final SystemUtil instance = new SystemUtil();
	public static enum OSType {WINDOWS, LINUX;}
	
	private SystemUtil() {}
	
	public static SystemUtil instance() {
		return instance;
	}
	
	public static String getUserDir() {
		return System.getProperty("user.dir");
	}
	
	public static String getLineSeparator() {
		return System.getProperty("line.separator");
	}
	
	public static String getPathSeparator() {
		return System.getProperty("path.separator");
	}
	
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
	
	public static String[] getClassPath() {
		return System.getProperty("java.class.path").split(getPathSeparator());
	}
	
	public static OSType getOperationSystem() {
		String osName = System.getProperty("os.name").toLowerCase();
		return (osName.indexOf("windows") != -1) ? OSType.WINDOWS : OSType.LINUX;
	}
	
	public static String getSystemCharset() {
		return System.getProperty("sun.jnu.encoding");
	}
	
//	public static void main(String[] args) throws Exception {
//		for (String path : getClassPath()) {
//			File file = new File(path);
//			if (file.isDirectory()) {
//				file = new File(file, "org/apache/log4j/Logger.class");
//			} else if (file.isFile()) {
//				JarFile jarFile = new JarFile(file);
//				Enumeration<JarEntry> enu = jarFile.entries();
//				int count = 0;
//				while (enu.hasMoreElements()) {
//					count++;
//					JarEntry entry = enu.nextElement();
//					System.out.println(entry.getName());
//				}
//				System.out.println(count);
////				file = new File(file.getAbsolutePath() + "!org\\apache\\log4j\\Logger.class");
//			}
//			System.out.println(file.getAbsolutePath());
//			if (file.isFile()) {
//				System.out.println("true:" + file.getAbsolutePath());
//			}
//
//		}
//	}
}
