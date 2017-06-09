package com.kingbosky.commons.redis;

import java.util.Map;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

@Service
public class StringClient extends BaseClient {
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
	
	public String hmget(final String key) {
		return doExecute(key, new Operation<String>() {
			@Override
			public String execute(Jedis jedis) {
				return jedis.get(key);
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
}
