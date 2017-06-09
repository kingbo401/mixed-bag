package com.kingbosky.commons.redis.jedis.pool;

import redis.clients.jedis.Jedis;

public interface JedisResourcePool {
	public Jedis getResource(String key);
}
