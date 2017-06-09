package com.kingbosky.commons.redis.jedis.poll;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

public class JedisPollClient implements JedisPoll{
	private Pool<Jedis> pool;
	
	public JedisPollClient(){
		this("redis.properties");
	}

	public JedisPollClient(String configFile) {
		String path = JedisPollClient.class.getResource("/").getPath()
				+ configFile;
		if (pool != null)
			return;
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			prop.load(fis);
			String ip = prop.getProperty("ip");
			int port = NumberUtils.toInt(prop.getProperty("port"));
			int maxTotal = NumberUtils.toInt(prop.getProperty("maxTotal"));
			long maxWaitMillis = NumberUtils.toLong(prop
					.getProperty("maxWaitMillis"));
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
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Jedis getResource(String key) {//key供以后客户端集群扩展使用
		return pool.getResource();
	}
}
