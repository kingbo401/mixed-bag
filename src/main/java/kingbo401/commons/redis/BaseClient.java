package kingbo401.commons.redis;

import java.io.UnsupportedEncodingException;
import java.util.List;

import kingbo401.commons.hessian.HessianSerializeUtil;
import kingbo401.commons.redis.jedis.pool.JedisResourcePool;
import redis.clients.jedis.Jedis;

public class BaseClient {
	protected final String DFT_CHARSET = "UTF-8";
	
	private JedisResourcePool jedisResourcePool;
	
	public JedisResourcePool getJedisResourcePool() {
		return jedisResourcePool;
	}

	public void setJedisResourcePool(JedisResourcePool jedisResourcePool) {
		this.jedisResourcePool = jedisResourcePool;
	}

	protected <T> T doExecute(String key, Operation<T> operation) {
		Jedis jedis = null;
		try {
			jedis = jedisResourcePool.getResource(key);
			return operation.execute(jedis);
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}

	protected interface Operation<T> {
		T execute(Jedis jedis);
	}
	
	protected <T> T decode(byte[] data) {
		return HessianSerializeUtil.deserialize(data);
	}
	
	protected byte[] encode(Object value) {
		return HessianSerializeUtil.serialize(value);
	}
	
	protected byte[] getBytes(String str) {
		try {
			return str.getBytes(DFT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
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
//	private boolean isBaseDataType(Class<?> clazz) {
//		return (clazz.equals(String.class) || clazz.equals(Integer.class)
//				|| clazz.equals(Byte.class) || clazz.equals(Long.class)
//				|| clazz.equals(Double.class) || clazz.equals(Float.class)
//				|| clazz.equals(Character.class) || clazz.equals(Short.class)
//				|| clazz.equals(BigDecimal.class)
//				|| clazz.equals(BigInteger.class)
//				|| clazz.equals(Boolean.class) || clazz.isPrimitive());
//	}
}
