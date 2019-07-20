package com.kingbo401.commons.constant;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 常量合集
 * @author kingbo401
 * @date 2019/07/20
 */
public interface Constants {
	/** 默认字符 */
	public static final String DFT_CHARSET = "UTF-8";
    /** 日期格式 */
    public static final String DFT_DATE_FORMAT = "yyyy-MM-dd";
    /** 日期时间格式 */
    public static final String DFT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 时间格式 */
    public static final String DFT_TIME_FORMAT = "HH:mm:ss";
    
    /** 默认整数值 */
	public static final int DFT_INTEGER_VAL = 0;
	/** 默认字节值 */
	public static final byte DFT_BYTE_VAL = 0;
	/** 默认短整数值 */
	public static final short DFT_SHORT_VAL = 0;
	/** 默认长整数值 */
	public static final long DFT_LONG_VAL = 0L;
	/** 默认单精度浮点数值 */
	public static final float DFT_FLOAT_VAL = 0.0F;
	/** 默认双精度浮点数值 */
	public static final double DFT_DOUBLE_VAL = 0.0D;
	/** 默认布尔值 */
	public static final boolean DFT_BOOLEAN_VAL = false;
	/** 默认字符串值 */
	public static final String DFT_STRING_VAL = "".intern();
	/** 默认空字符串值 */
	public static final String DFT_NULL_STRING_VAL = "*NULL*".intern();
	/** 默认大整数数值 */
	public static final BigInteger DFT_BIGINTEGER_VAL = new BigInteger("0");
	/** 默认大小数数值 */
	public static final BigDecimal DFT_BIGDECIMAL_VAL = new BigDecimal("0");
	/** 默认日期数值 */
	public static final Date DFT_DATE_VAL = null;
	/** 默认时间戳值 */
	public static final Timestamp DFT_TIMESTAMP_VAL = null;
	/**
	 * 数据库表查询升序
	 */
	public static final String ORDER_ASC = "asc";
	/**
	 * 数据库表查询降序
	 */
	public static final String ORDER_DESC = "desc";
}
