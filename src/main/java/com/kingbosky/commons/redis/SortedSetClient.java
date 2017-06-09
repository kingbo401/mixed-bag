package com.kingbosky.commons.redis;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import com.kingbosky.commons.lock.ILock;

@Service
public class SortedSetClient extends BaseClient{
	private final Logger logger = LoggerFactory.getLogger(SortedSetClient.class);
	private ILock distributedLock; 
	
	public ILock getDistributedLock() {
		return distributedLock;
	}

	public void setDistributedLock(ILock distributedLock) {
		this.distributedLock = distributedLock;
	}

	/**
	 * 添加排行榜成员
	 * @param key
	 * @param member
	 * @param score
	 */
	public Long zadd(final String key, final String member, final double score){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.zadd(key, score, member);
			}
		});
	}
	
	/**
	 * 给member加分
	 * @param key
	 * @param member
	 * @param num
	 */
	public void addScore(String key, String member, double num){
		String lockKey = "sortedset:lock:" + key + ":" + member;
		if(distributedLock.lock(lockKey)){
			try{
				Double score = zscore(key, member);
				if(score == null) score = 0d;
				zadd(key, member, score + num);
			}finally{
				distributedLock.unLock(lockKey);
			}
		}else {
			logger.error("SortedSetClient get lock error,key:" + lockKey);
		}
	}
	/**
	 * 删除
	 * @param key
	 * @param members
	 * @return
	 */
	public Long zrem(final String key, final String ...members){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.zrem(key, members);
			}
		});
	}
	
	/**
	 * 获取分数
	 * @param key
	 * @param member
	 * @return
	 */
	public Double zscore(final String key, final String member){
		return doExecute(key, new Operation<Double>(){
			@Override
			public Double execute(Jedis jedis) {
				return jedis.zscore(key, member);
			}
		});
	}
	
	/**
	 * 获取排名
	 * @param key
	 * @param member
	 * @return
	 */
	public Long zrank(final String key, final String member){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.zrank(key, member);
			}
		});
	}
	
	public Set<Tuple> zrangeWithScores(final String key, final int start, final int end){
		return doExecute(key, new Operation<Set<Tuple>>(){
			@Override
			public Set<Tuple> execute(Jedis jedis) {
				return jedis.zrangeWithScores(key, start, end);
			}
		});
	}
	
	public Set<Tuple> zrevrangeWithScores(final String key, final int start, final int end){
		return doExecute(key, new Operation<Set<Tuple>>(){
			@Override
			public Set<Tuple> execute(Jedis jedis) {
				return jedis.zrevrangeWithScores(key, start, end);
			}
		});
	}
	
	public Set<String> zrange(final String key, final int start, final int end){
		return doExecute(key, new Operation<Set<String>>(){
			@Override
			public Set<String> execute(Jedis jedis) {
				return jedis.zrange(key, start, end);
			}
		});
	}
	
	public Set<String> zrevrange(final String key, final int start, final int end){
		return doExecute(key, new Operation<Set<String>>(){
			@Override
			public Set<String> execute(Jedis jedis) {
				return jedis.zrevrange(key, start, end);
			}
		});
	}
	public Long zcard(final String key){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.zcard(key);
			}
		});
	}
	
	public Long zremrangeByRank(final String key, final long start, final long end){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.zremrangeByRank(key, start, end);
			}
		});
	}
}
