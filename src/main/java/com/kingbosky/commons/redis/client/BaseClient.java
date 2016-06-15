package com.kingbosky.commons.redis.client;

import java.io.UnsupportedEncodingException;

import redis.clients.jedis.Jedis;

import com.kingbosky.commons.redis.JedisClient;
import com.kingbosky.commons.redis.SerializeUtil;

public class BaseClient {
	protected final String DFT_CHARSET = "UTF-8";
	protected <T> T doExecute(String key, Operation<T> operation) {
		Jedis jedis = null;
		try {
			jedis = JedisClient.getResource(key);
			return operation.execute(jedis);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}

	protected interface Operation<T> {
		T execute(Jedis jedis);
	}
	
	protected <T> T decode(byte[] data) {
		return SerializeUtil.deserialize(data);
	}
	
	protected byte[] encode(Object value) {
		return SerializeUtil.serialize(value);
	}
	
	protected byte[] getBytes(String str) {
		try {
			return str.getBytes(DFT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	private static boolean isBaseDataType(Class<?> clazz) {
//		return (clazz.equals(String.class) || clazz.equals(Integer.class)
//				|| clazz.equals(Byte.class) || clazz.equals(Long.class)
//				|| clazz.equals(Double.class) || clazz.equals(Float.class)
//				|| clazz.equals(Character.class) || clazz.equals(Short.class)
//				|| clazz.equals(BigDecimal.class)
//				|| clazz.equals(BigInteger.class)
//				|| clazz.equals(Boolean.class) || clazz.isPrimitive());
//	}
}
