package com.kingbosky.commons.redis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.kingbosky.commons.util.NumberUtil;

public class JedisClient {
	private static JedisPool pool;
	static{
		init();;
	}
	
	public static void init() {
		if (pool != null)
			return;
		Properties prop = new Properties();
		InputStream ins = null;
		try {//redis.properties
			ins = JedisClient.class.getResourceAsStream("/redis.properties");
			prop.load(ins);
			String ip = prop.getProperty("ip");
			int port = NumberUtil.toInt(prop.getProperty("port"));
			int maxTotal = NumberUtil.toInt(prop.getProperty("maxTotal"));
			long maxWaitMillis = NumberUtil.toLong(prop.getProperty("maxWaitMillis"));
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(maxTotal);
			config.setMaxIdle(Math.max(config.getMaxTotal() / 10, 10));
			config.setMaxWaitMillis(maxWaitMillis);
			config.setTestOnBorrow(true);
			config.setTestWhileIdle(true);
			pool = new JedisPool(config, ip, port);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Jedis getResource(String key) {//key供以后客户端集群扩展使用
		return pool.getResource();
	}
}
