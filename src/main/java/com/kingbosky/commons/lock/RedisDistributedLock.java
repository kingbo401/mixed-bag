package com.kingbosky.commons.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.kingbosky.commons.redis.jedis.pool.JedisResourcePool;

public class RedisDistributedLock implements ILock{
	private  int expire = 3;//3秒
	private final  Logger logger = LoggerFactory.getLogger(getClass());
	private JedisResourcePool jedisResourcePool;
	
	public RedisDistributedLock(JedisResourcePool jedisResourcePool){
		this.jedisResourcePool = jedisResourcePool;
	}
	
	public RedisDistributedLock(JedisResourcePool jedisResourcePool, int expire){
		this.jedisResourcePool = jedisResourcePool;
		this.expire = expire;
	}

	@Override
	public boolean lock(String key) {
		int count = 0;
		while(true){
			Jedis jedis = null;
			try{
				jedis = jedisResourcePool.getResource(key);
				if(!jedis.isConnected()){//防止redis挂的时候项目不可用
					logger.error("Redis Connect Exception");
					return true;
				}
				if("OK".equals(jedis.set(key, "1", "NX", "EX", expire))){
					logger.info("get lock : " + key);
					return true;
				}
				
			}finally{
				if(jedis != null)
					jedis.close();
			}
			try {
				Thread.sleep(50);//暂停50毫秒，降低循环次数
				count++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(count >= 3){//最多重试三次
				return false;
			}
		}
	}

	@Override
	public void unLock(String key) {
		Jedis jedis = null;
		try{
			jedis = jedisResourcePool.getResource(key);
			jedis.del(key);
		}finally{
			if(jedis != null)
			jedis.close();;
		}
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public JedisResourcePool getJedisResourcePool() {
		return jedisResourcePool;
	}

	public void setJedisResourcePool(JedisResourcePool jedisResourcePool) {
		this.jedisResourcePool = jedisResourcePool;
	}
}
