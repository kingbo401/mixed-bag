package com.kingbo401.commons.util;

import java.util.Collection;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * beancopy工具类
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
     * Mapper工厂,空字段不复制
     */
    private static final MapperFactory MAPPER_FACTORY_NOT_NULL = new DefaultMapperFactory.Builder()
            .mapNulls(false).build();

    /**
     * Mapper实例,空字段不复制
     */
    private static final MapperFacade MAPPER_FACADE_NOT_NULL = MAPPER_FACTORY_NOT_NULL.getMapperFacade();

    /**
     * 映射实体
     *
     * @param toClass 映射类对象
     * @param data    数据（对象）
     * @return 映射类对象
     */
    public static <E, T> E map(Class<E> toClass, T data) {
        return map(toClass, data, true);
    }

    /**
     * 映射实体
     *
     * @param toClass 映射类对象
     * @param data    数据（对象）
     * @param mapNull 是否复制空字段
     * @return 映射类对象
     */
    public static <E, T> E map(Class<E> toClass, T data, boolean mapNull) {
        if(data == null){
            return null;
        }
        if (mapNull){
            return MAPPER_FACADE_DEFAULT.map(data, toClass);
        } else {
            return MAPPER_FACADE_NOT_NULL.map(data, toClass);
        }
    }

    /**
     * 映射集合
     *
     * @param toClass 映射类对象
     * @param data    数据（集合）
     * @return 映射类对象
     */
    public static <E, T> List<E> mapAsList(Class<E> toClass, Collection<T> data) {
        return mapAsList(toClass, data, true);
    }

    /**
     * 映射集合
     *
     * @param toClass 映射类对象
     * @param data    数据（集合）
     * @return 映射类对象
     */
    public static <E, T> List<E> mapAsList(Class<E> toClass, Collection<T> data, boolean mapNull) {
        if (data == null || data.isEmpty()){
            return null;
        }
        if (mapNull){
            return MAPPER_FACADE_DEFAULT.mapAsList(data, toClass);
        } else {
            return MAPPER_FACADE_NOT_NULL.mapAsList(data, toClass);
        }
    }
}
