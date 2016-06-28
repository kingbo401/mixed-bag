package com.kingbosky.commons.spring.jdbc.orm;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.kingbosky.commons.util.MapObjectConvertor;

public class JdbcOrmTemplate {
	private NamedParameterJdbcDaoSupport jdbcDaoSupport = new NamedParameterJdbcDaoSupport();
	private boolean mapUnderscoreToCamelCase = true;

	public final void setDataSource(DataSource dataSource) {
		jdbcDaoSupport.setDataSource(dataSource);
	}
	
	public boolean isMapUnderscoreToCamelCase() {
		return mapUnderscoreToCamelCase;
	}

	public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
		this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
	}

	public int insert(String sql, Object obj){
		Map<String, Object> params = convertObjectToMap(obj);
		return jdbcDaoSupport.getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	public long insertGetGeneratedKey(String sql, Object obj){
		Map<String, Object> params = convertObjectToMap(obj);
		KeyHolder generatedKeyHolder = new GeneratedKeyHolder();  
		jdbcDaoSupport.getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(params), generatedKeyHolder);
		return generatedKeyHolder.getKey().longValue();
	}
	
	public int update1(String sql, Map<String, ?> params){
		return jdbcDaoSupport.getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	public int update0(String sql, Object... params){
		return jdbcDaoSupport.getJdbcTemplate().update(sql, params);
	}

	public int update(String sql, Object obj){
		Map<String, Object> params = convertObjectToMap(obj);
		return jdbcDaoSupport.getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	public int[] batchUpdate1(String sql, List<Map<String, ?>> mapList){
		if(mapList == null || mapList.isEmpty()) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		Map<String, Object>[] paramsArray = (Map<String, Object>[])Array.newInstance(map.getClass(), mapList.size());
		mapList.toArray(paramsArray);
		return jdbcDaoSupport.getNamedParameterJdbcTemplate().batchUpdate(sql, paramsArray);
	}

	
	public int[] batchUpdate(String sql, List<?> paramsList){
		if(paramsList == null || paramsList.isEmpty()) return null;
		int len = paramsList.size();
		List<Map<String, ?>> mapList = new ArrayList<Map<String, ?>>();
		for(int i = 0; i < len; i++){
			Object object = paramsList.get(i);
			mapList.add(convertObjectToMap(object));
		}
		return batchUpdate1(sql, mapList);
	}
	
	public <T> List<T> queryForObjectList(String sql, Class<T> clazz, Object obj){
		if (clazz == null) {
			return null;
		}
		List<Map<String, Object>> mapList = jdbcDaoSupport.getNamedParameterJdbcTemplate().queryForList(sql, convertObjectToMap(obj));
		return convertToObjectList(mapList, clazz);
	}
	
	public <T> List<T> queryForObjectList0(String sql, Class<T> clazz, Object... params) {
		if (clazz == null) {
			return new ArrayList<T>();
		}
		List<Map<String, Object>> mapList = jdbcDaoSupport.getJdbcTemplate().queryForList(sql, params);
		return convertToObjectList(mapList, clazz);
	}
	
	public <T> List<T> queryForObjectList1(String sql, Class<T> clazz, Map<String, ?> params){
		if (clazz == null) {
			return null;
		}
		List<Map<String, Object>> mapList = jdbcDaoSupport.getNamedParameterJdbcTemplate().queryForList(sql, params);
		return convertToObjectList(mapList, clazz);
	}
	
	public long queryForLong0(String sql, Object... params){
		return queryForObject0(sql, long.class, params);
	}
	
	public long queryForLong1(String sql, Map<String, ?> params){
		return queryForObject1(sql, long.class, params);
	}
	
	public long queryForLong(String sql, Object obj){
		return queryForObject(sql, long.class, obj);
	}
	
	public int queryForInt0(String sql, Object... params){
		return queryForObject0(sql, int.class, params);
	}
	
	public int queryForInt1(String sql, Map<String, ?> params){
		return queryForObject1(sql, int.class, params);
	}
	
	public int queryForInt(String sql, Object obj){
		return queryForObject(sql, int.class, obj);
	}
	
	public <T> T queryForObject1(String sql, Class<T> clazz, Map<String, ?> params){
		if (clazz == null) {
			return null;
		}
		Map<String, Object> map = null;
		try {
			map = jdbcDaoSupport.getNamedParameterJdbcTemplate().queryForMap(sql, params);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return convertMapToObject(map, clazz);
	}

	public <T> T queryForObject0(String sql, Class<T> clazz, Object... params){
		if (clazz == null) {
			return null;
		}
		Map<String, Object> tempMap = null;
		try {
			tempMap = jdbcDaoSupport.getJdbcTemplate().queryForMap(sql, params);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return convertMapToObject(tempMap, clazz);
	}
	public <T> T queryForObject(String sql, Class<T> clazz, Object obj){
		if (clazz == null) {
			return null;
		}
		Map<String, Object> map = null;
		try {
			map = jdbcDaoSupport.getNamedParameterJdbcTemplate().queryForMap(sql, convertObjectToMap(obj));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return convertMapToObject(map, clazz);
	}
	
	public List<Map<String, Object>> queryForList1(String sql, Map<String, ?> params){
		return jdbcDaoSupport.getNamedParameterJdbcTemplate().queryForList(sql, params);
	}

	public List<Map<String, Object>> queryForList0(String sql, Object... params){
		return jdbcDaoSupport.getJdbcTemplate().queryForList(sql, params);
	}

	public List<Map<String, Object>> queryForList(String sql, Object obj){
		return jdbcDaoSupport.getNamedParameterJdbcTemplate().queryForList(sql, convertObjectToMap(obj));
	}
	
	public Map<String, Object> queryForMap1(String sql, Map<String, ?> params){
		try{
			return jdbcDaoSupport.getNamedParameterJdbcTemplate().queryForMap(sql, params);
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	public Map<String, Object> queryForMap0(String sql, Object... params) {
		try{
			return jdbcDaoSupport.getJdbcTemplate().queryForMap(sql, params);
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	public Map<String, Object> queryForMap(String sql, Object obj) {
		try{
			return jdbcDaoSupport.getNamedParameterJdbcTemplate().queryForMap(sql, convertObjectToMap(obj));
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	private <T> List<T> convertToObjectList(List<Map<String, Object>> mapList, Class<T> clazz) {
		List<T> resultList = new ArrayList<T>();
		Map<String, Object> map = null;
		T obj = null;
		Iterator<Map<String, Object>> iter = mapList.iterator();
		while (iter.hasNext()) {
			map = (Map<String, Object>) iter.next();
			obj = convertMapToObject(map, clazz);
			resultList.add(obj);
		}
		return resultList;
	}
	
	private <T> T convertMapToObject(Map<String, Object> dataMap, Class<T> clazz){
		return MapObjectConvertor.convertMapToObject(dataMap, clazz, mapUnderscoreToCamelCase);
	}
	
	private Map<String, Object> convertObjectToMap(Object obj){
		return MapObjectConvertor.convertObjectToMap(obj);
	}
}
