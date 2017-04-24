package com.kingbosky.commons.redis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class JedisSentinelPollClient {
	private static JedisSentinelPollClient instance = new JedisSentinelPollClient();
	private Pool<Jedis> pool;

	private JedisSentinelPollClient() {
		String path = JedisSentinelPollClient.class.getResource("/").getPath()
				+ "redis_sentinel.properties";
		if (pool != null)
			return;
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			prop.load(fis);
			String masterName = prop.getProperty("masterName");
			String sentinel = prop.getProperty("sentinels");
			int maxTotal = NumberUtils.toInt(prop.getProperty("maxTotal"));
			int timeout = NumberUtils.toInt(prop.getProperty("timeout"));
			int database = NumberUtils.toInt(prop.getProperty("database"));
			long maxWaitMillis = NumberUtils.toLong(prop
					.getProperty("maxWaitMillis"));
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(maxTotal);
			config.setMaxIdle(Math.max(config.getMaxTotal() / 10, 10));
			config.setMaxWaitMillis(maxWaitMillis);
			config.setTestOnBorrow(true);
			config.setTestWhileIdle(true);
			Set<String> sentinels = new HashSet<String>();
			sentinels.addAll(Arrays.asList(sentinel.split(",")));
			pool = new JedisSentinelPool(masterName, sentinels, config, timeout, timeout, null, database, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Jedis getResource(String key) {//key供以后客户端集群扩展使用
		return instance.pool.getResource();
	}
}
