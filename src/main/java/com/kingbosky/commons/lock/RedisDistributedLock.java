package com.kingbosky.commons.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.kingbosky.commons.redis.jedis.poll.JedisPoll;

public class RedisDistributedLock implements ILock{
	private  int expire = 3;//3秒
	private final  Logger logger = LoggerFactory.getLogger(getClass());
	private JedisPoll jedisPoll;
	
	public RedisDistributedLock(JedisPoll jedisPoll){
		this.jedisPoll = jedisPoll;
	}
	
	public RedisDistributedLock(JedisPoll jedisPoll, int expire){
		this.jedisPoll = jedisPoll;
		this.expire = expire;
	}

	@Override
	public boolean lock(String key) {
		int count = 0;
		while(true){
			Jedis jedis = null;
			try{
				jedis = jedisPoll.getResource(key);
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
			jedis = jedisPoll.getResource(key);
			jedis.del(key);
		}finally{
			if(jedis != null)
			jedis.close();;
		}
	}
	
	public JedisPoll getJedisPoll() {
		return jedisPoll;
	}

	public void setJedisPoll(JedisPoll jedisPoll) {
		this.jedisPoll = jedisPoll;
	}
}
