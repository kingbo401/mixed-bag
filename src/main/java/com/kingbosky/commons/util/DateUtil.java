package com.kingbosky.commons.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
	/** 单例 */
    private static final DateUtil instance = new DateUtil();

    private DateUtil() {}

    /**
     * 取得该类唯一实例
     * @return 该类唯一实例
     */
    public static DateUtil instance() {
        return instance;
    }
    
    /**
     * 将默认格式的字符串转换为日期对象
     * @param date 待转换字符串
     * @return 转换后日期对象
     * @see #parseDate(String, String, Date)
     * @see com.nx.commons.lang.Constants#DFT_DATE_FORMAT
     * @see com.nx.commons.lang.Constants#DFT_DATE_VAL
     */
    public static Date parseDate(String date) {
        return parseDate(date, Constants.DFT_DATE_FORMAT, Constants.DFT_DATE_VAL);
    }
    /**
     * 将yyyy-MM-dd HH:mm:ss格式的字符串转换为日期对象
     * @param date 待转换字符串
     * @return 转换后日期对象
     * @see #parseDate(String, String, Date)
     * @see com.nx.commons.lang.Constants#DFT_DATETIME_FORMAT
     * @see com.nx.commons.lang.Constants#DFT_DATE_VAL
     */
    public static Date parseDateTime(String date) {
        return parseDate(date, Constants.DFT_DATETIME_FORMAT, Constants.DFT_DATE_VAL);
    }
    /**
     * 将指定格式的字符串转换为日期对象
     * @param date 待转换字符串
     * @param format 日期格式
     * @return 转换后日期对象
     * @see #parseDate(String, String, Date)
     */
    public static Date parseDate(String date, String format) {
        return parseDate(date, format, Constants.DFT_DATE_VAL);
    }
    /**
     * 将指定格式的字符串转换为日期对象
     * @param date 日期对象
     * @param format 日期格式
     * @param dftVal 转换失败时的默认返回值
     * @return 转换后的日期对象
     */
    public static Date parseDate(String date, String format, Date dftVal) {
        if (StringUtil.isEmpty(date) || StringUtil.isEmpty(format)) return dftVal;
        Date d;
        try {
            d = new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            d = dftVal;
        }
        return d;
    }

    /**
     * 将日期对象格式化成yyyy-MM-dd格式的字符串
     * @param date 待格式化日期对象
     * @return 格式化后的字符串
     * @see #formatDate(Date, String, String)
     * @see com.nx.commons.lang.Constants#DFT_DATE_FORMAT
     */
    public static String formatDate(Date date) {
        return formatDate(date, Constants.DFT_DATE_FORMAT, null);
    }
    /**
     * 将日期对象格式化成yyyy-MM-dd HH:mm:ss格式的字符串
     * @param date 待格式化日期对象
     * @return 格式化后的字符串
     * @see #formatDate(Date, String, String)
     * @see com.nx.commons.lang.Constants#DFT_DATETIME_FORMAT
     */
    public static String formatDatetime(Date date) {
        return formatDate(date, Constants.DFT_DATETIME_FORMAT, null);
    }
    /**
     * 将日期对象格式化成HH:mm:ss格式的字符串
     * @param date 待格式化日期对象
     * @return 格式化后的字符串
     * @see #formatDate(Date, String, String)
     * @see com.nx.commons.lang.Constants#DFT_TIME_FORMAT
     */
    public static String formatTime(Date date) {
        return formatDate(date, Constants.DFT_TIME_FORMAT, null);
    }
    /**
     * 将日期对象格式化成指定类型的字符串
     * @param date 待格式化日期对象
     * @param format 格式化格式
     * @return 格式化后的字符串
     * @see #formatDate(Date, String, String)
     */
    public static String formatDate(Date date, String format) {
        return formatDate(date, format, null);
    }
    /**
     * 将日期对象格式化成指定类型的字符串
     * @param date 待格式化日期对象
     * @param format 格式化格式
     * @param dftVal 格式化失败时的默认返回空
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date, String format, String dftVal) {
        if (date == null || StringUtil.isEmpty(format)) return dftVal;
        String ret;
        try {
            ret = new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            ret = dftVal;
        }
        return ret;
    }
    
    /**
     * 将字符串转化为时间戳对象
     * @param date 时间戳字符串对象
     * @return 转化后的时间戳对象
     * @see #parseTimestamp(String, String, Timestamp)
     * @see com.nx.commons.lang.Constants#DFT_DATE_FORMAT
     * @see com.nx.commons.lang.Constants#DFT_DATE_VAL
     */
    public static Timestamp parseTimestamp(String date) {
    	return parseTimestamp(date, Constants.DFT_DATE_FORMAT, Constants.DFT_TIMESTAMP_VAL);
    }
    /**
     * 将字符串转化为时间戳对象
     * @param date 时间戳字符串对象
     * @param format 字符串格式
     * @return 转化后的时间戳对象
     * @see #parseTimestamp(String, String, Timestamp)
     * @see com.nx.commons.lang.Constants#DFT_DATE_VAL
     */
    public static Timestamp parseTimestamp(String date, String format) {
    	return parseTimestamp(date, format, Constants.DFT_TIMESTAMP_VAL);
    }
    /**
     * 将指定格式的字符串转换为时间戳
     * @param date 日期对象
     * @param format 日期格式
     * @param dftVal 转换失败时的默认返回值
     * @return 转换后的日期对象
     */
    public static Timestamp parseTimestamp(String date, String format, Timestamp dftVal) {
    	Date d = parseDate(date, format);
    	return (d == null) ? dftVal : new Timestamp(d.getTime());
    }
    /**
     * 将时间戳对象格式化成指定类型的字符串
     * @param date 待格式化时间戳对象
     * @return 格式化后的字符串
     * @see #formatTimestamp(Timestamp, String, String)
     * @see com.nx.commons.lang.Constants#DFT_DATE_FORMAT
     */
    public static String formatTimestamp(Timestamp date) {
    	return formatDate(date, Constants.DFT_DATE_FORMAT, null);
    }
    /**
     * 将时间戳对象格式化成指定类型的字符串
     * @param date 待格式化时间戳对象
     * @param format 格式化格式
     * @return 格式化后的字符串
     * @see #formatTimestamp(Timestamp, String, String)
     */
    public static String formatTimestamp(Timestamp date, String format) {
    	return formatDate(date, format, null);
    }
    /**
     * 将时间戳对象格式化成指定类型的字符串
     * @param date 待格式化时间戳对象
     * @param format 格式化格式
     * @param dftVal 转换失败时的默认返回值
     * @return 格式化后的字符串
     * @see #formatTimestamp(Timestamp, String, String)
     */
    public static String formatTimestamp(Timestamp date, String format, String dftVal) {
    	return formatDate(date, format, dftVal);
    }
    
    
    public static Timestamp timestamp() {
    	return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * d1 和 d2 是同一天
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isSameDate(Date d1, Date d2){
    	if(d1==null || d2==null) return false;
    	Calendar c1 = Calendar.getInstance();
    	c1.setTimeInMillis(d1.getTime());
    	Calendar c2 = Calendar.getInstance();
    	c2.setTimeInMillis(d2.getTime());
    	
    	return c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR)
    		&& c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH)
    		&& c1.get(Calendar.DAY_OF_MONTH)==c2.get(Calendar.DAY_OF_MONTH);
    }
}
