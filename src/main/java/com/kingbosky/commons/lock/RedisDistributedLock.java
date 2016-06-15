package com.kingbosky.commons.lock;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import com.kingbosky.commons.redis.JedisClient;

public class RedisDistributedLock implements ILock{
	private  int expire = 3;//3秒
	private final  Logger logger = LoggerFactory.getLogger(getClass());

	public RedisDistributedLock(){
		
	}
	
	public RedisDistributedLock(int expire){
		this.expire = expire;
	}

	@Override
	public boolean lock(String key) {
		int count = 0;
		while(true){
			Jedis jedis = null;
			try{
				jedis = JedisClient.getResource(key);
				if(!jedis.isConnected()){//防止redis挂的时候项目不可用
					logger.error("Redis Connect Exception");
					return true;
				}
//				if("OK".equals(jedis.set(key, "1", "NX", "PX", expire))){
//					logger.info("get lock : " + key);
//					return true;
//				}
				//线上redis是2.4版本，不支持上面的命令，运维不给改，哎；只能用下面的方式实现
				
				Transaction trans = jedis.multi();
				trans.setnx(key, "1");
				trans.expire(key, expire);
				List<Object> lst = trans.exec();
				if((Long)lst.get(0) == 1 && (Long) lst.get(1) != 1){
					//set成功，expire失败，删除key，这种概率几乎为0
					jedis.del(key);
				}
				if((Long)lst.get(0) == 1 && (Long) lst.get(1) == 1){
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
			jedis = JedisClient.getResource(key);
			jedis.del(key);
		}finally{
			if(jedis != null)
			jedis.close();;
		}
	}
}
