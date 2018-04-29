package com.kingbo401.commons.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kingbo401.commons.lock.ILock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

@Service
public class RedisClient extends BaseClient {
	private final Logger logger = LoggerFactory.getLogger(RedisClient.class);
	private ILock distributedLock; 
	
	public ILock getDistributedLock() {
		return distributedLock;
	}

	public void setDistributedLock(ILock distributedLock) {
		this.distributedLock = distributedLock;
	}
	
	
	public Long del(final String key){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.del(key);
			}
		});
	}
	
	public void del(final List<String> keys){
		doExecute(null, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.del((String[])keys.toArray());
			}
		});
	}
	
	public Boolean exists(final String key) {
		return doExecute(key, new Operation<Boolean>(){
			@Override
			public Boolean execute(Jedis jedis) {
				return jedis.exists(key);
			}
		});
	}
	
	public Long decrBy(final String key, final long num){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.decrBy(key, num);
			}
		});
	}
	
	public String get(final String key) {
		return doExecute(key, new Operation<String>() {
			@Override
			public String execute(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}
	
	public String set(final String key, final String value){
		return doExecute(key, new Operation<String>(){
			@Override
			public String execute(Jedis jedis) {
				return jedis.set(key, value);
			}
		});
	}
	
	public <T> T objectGet(final String key) {
		return doExecute(key, new Operation<T>() {
			@Override
			public T execute(Jedis jedis) {
				return decode(jedis.get(getBytes(key)));
			}
		});
	}
	
	public String objectSet(final String key, final Object value){
		return doExecute(key, new Operation<String>(){
			@Override
			public String execute(Jedis jedis) {
				return jedis.set(getBytes(key), encode(value));
			}
		});
	}
	
	public Long decr(final String key){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.decr(key);
			}
		});
	}
	
	public Long incrBy(final String key, final long num){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.incrBy(key, num);
			}
		});
	}
	
	public Long incr(final String key){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.incr(key);
			}
		});
	}
	
	public Long setnx(final String key, final String value){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.setnx(key, value);
			}
		});
	}
	
	public String setex(final String key, final int seconds, final String value) {
		return doExecute(key, new Operation<String>(){
			@Override
			public String execute(Jedis jedis) {
				return jedis.setex(key, seconds, value);
			}
		});
	}
	
	public Long objectSetnx(final String key, final Object value){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.setnx(getBytes(key), encode(value));
			}
		});
	}
	
	public String objectSetex(final String key, final int seconds, final Object value) {
		return doExecute(key, new Operation<String>(){
			@Override
			public String execute(Jedis jedis) {
				return jedis.setex(getBytes(key), seconds, encode(value));
			}
		});
	}
	
	public Long expire(final String key, final int seconds){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.expire(key, seconds);
			}
		});
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
	
	public List<String> hmget(final String key, final String... fields) {
		return doExecute(key, new Operation<List<String>>() {
			@Override
			public List<String> execute(Jedis jedis) {
				return jedis.hmget(key, fields);
			}
		});
	}
	
	public String hmset(final String key, final Map<String, String> hash){
		return doExecute(key, new Operation<String>(){
			@Override
			public String execute(Jedis jedis) {
				return jedis.hmset(key, hash);
			}
		});
	}
	
	public String hget(final String key, final String field) {
		return doExecute(key, new Operation<String>() {
			@Override
			public String execute(Jedis jedis) {
				return jedis.hget(key, field);
			}
		});
	}
	
	public Long hset(final String key, final String field, final String value){
		return doExecute(key, new Operation<Long>(){
			@Override
			public Long execute(Jedis jedis) {
				return jedis.hset(key, field, value);
			}
		});
	}
	
	public Long hdel(final String key, final String... fields) {
		return doExecute(key, new Operation<Long>() {
			@Override
			public Long execute(Jedis jedis) {
				return jedis.hdel(key, fields);
			}
		});
	}
	
	public Boolean hexists(final String key, final String field) {
		return doExecute(key, new Operation<Boolean>() {
			@Override
			public Boolean execute(Jedis jedis) {
				return jedis.hexists(key, field);
			}
		});
	}
	
	public Map<String, String> hgetAll(final String key) {
		return doExecute(key, new Operation<Map<String, String>>() {
			@Override
			public Map<String, String> execute(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		});
	}
}
