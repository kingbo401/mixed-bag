package com.kingbo401.commons.util;

import java.util.List;
import java.util.Set;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * bean mapper工具类
 * @author tianqiongbo195
 *
 */
public class BeanMapper {
	/**
     * 默认Mapper工厂
     */
    private static final MapperFactory MAPPER_FACTORY_DEFAULT = new DefaultMapperFactory.Builder().build();

    /**
     * 默认Mapper实例
     */
    private static final MapperFacade MAPPER_FACADE_DEFAULT = MAPPER_FACTORY_DEFAULT.getMapperFacade();

    /**
     * 映射实体
     *
     * @param toClass 映射类对象
     * @param source    数据（对象）
     * @return 映射类对象
     */
    public static <E, T> E map(Class<E> toClass, T source) {
    	if(source == null){
            return null;
        }
        return MAPPER_FACADE_DEFAULT.map(source, toClass);
    }

    /**
     * 映射list
     *
     * @param toClass 映射类对象
     * @param source    数据（集合）
     * @return 映射类对象
     */
    public static <E, T> List<E> mapAsList(Class<E> toClass, Iterable<T> source) {
    	 if (source == null){
             return null;
         }
    	 return MAPPER_FACADE_DEFAULT.mapAsList(source, toClass);
    }
    
    /**
     * 映射Set
     *
     * @param toClass 映射类对象
     * @param source    数据（集合）
     * @return 映射类对象
     */
    public static <E, T> Set<E> mapAsSet(Class<E> toClass, Iterable<T> source) {
    	 if (source == null){
             return null;
         }
    	 return MAPPER_FACADE_DEFAULT.mapAsSet(source, toClass);
    }
    
    /**
     * 返回默认MapperFacade
     * @return
     */
    public static MapperFacade getDefaultMapperFacade() {
    	return MAPPER_FACADE_DEFAULT;
    }
}
