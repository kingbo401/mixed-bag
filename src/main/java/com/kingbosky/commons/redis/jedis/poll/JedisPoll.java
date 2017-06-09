package com.kingbosky.commons.redis.jedis.poll;

import redis.clients.jedis.Jedis;

public interface JedisPoll {
	public Jedis getResource(String key);
}
