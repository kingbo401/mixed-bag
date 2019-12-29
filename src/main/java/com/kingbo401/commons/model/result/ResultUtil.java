package com.kingbo401.commons.model.result;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.kingbo401.commons.model.PageVO;

public class ResultUtil {
	public static <T> PojoResult<T> newPojoResult(T content){
		return new PojoResult<T>(content);
	}
	
	public static <T> PageResult<T> newPageResult(PageVO<T> content){
		return new PageResult<T>(content);
	}
	
	public static <T> ListResult<T> newListResult(List<T> content){
		return new ListResult<T>(content);
	}
	
	public static <T> CollectionResult<T> newCollectionResult(Collection<T> content){
		return new CollectionResult<T>(content);
	}
	
	public static <K, V> MapResult<K, V> newMapResult(Map<K, V> content){
		return new MapResult<K, V>(content);
	}
}
