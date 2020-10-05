package com.kingbo401.commons.util;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

/**
 * BeanCopier  
 * 1、当源对象和目标对象的属性名称相同、类型不同,那么名称相同而类型不同的属性不会被拷贝
 *    注意，原始类型（int，short，char）和 他们的包装类型，在这里都被当成了不同类型，因此不会被拷贝
 * 2、源类和目标类有相同的属性（两者的getter都存在），但是目标类的setter不存在，此时会抛出NullPointerException
 * @author tianqiongbo195
 *
 */
public class BeanCopierUtil {
	/**
     * BeanCopier的缓存
     */
    private static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();
 
    /**
     * BeanCopier的copy
     * @param source 源文件的
     * @param target 目标文件
     */
    public static void copy(Object source, Object target) {
        BeanCopier beanCopier = getBeanCopier(source.getClass(), target.getClass(), false);
        beanCopier.copy(source, target, null);
    }
    
    /**
     * BeanCopier的copy
     * @param source 源文件的
     * @param target 目标文件
     * @param converter
     */
    public static void copy(Object source, Object target, Converter converter) {
        BeanCopier beanCopier = getBeanCopier(source.getClass(), target.getClass(), true);
        beanCopier.copy(source, target, converter);
    }
 
    /**
     * 生成key
     * @param srcClazz 源文件的class
     * @param tgtClazz 目标文件的class
     * @param useConverter 是否使用converter
     * @return string
     */
    private static String generateKey(Class<?> srcClazz, Class<?> tgtClazz, boolean useConverter) {
        return srcClazz.getName() +  "_" + tgtClazz.getName() + "_" + useConverter;
    }
    
    /**
     * 获取BeanCopier
     * @param srcClazz
     * @param tgtClazz
     * @param useConverter
     * @return
     */
    public static BeanCopier getBeanCopier(Class<?> srcClazz, Class<?> tgtClazz, boolean useConverter) {
    	String key = generateKey(srcClazz, tgtClazz, useConverter);
    	BeanCopier beanCopier = BEAN_COPIER_CACHE.get(key);
        if (beanCopier == null) {
        	beanCopier = BeanCopier.create(srcClazz, tgtClazz, useConverter);
        	BEAN_COPIER_CACHE.put(key, beanCopier);
        }
        return beanCopier;
    }
    
    /**
     * spring BeanUtils.copyProperties
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
    	copyProperties(source, target, false);
    }
    
    /**
     * spring BeanUtils.copyProperties
     * @param source
     * @param target
     * @param ignoreNull 是否忽略空属性
     */
    public static void copyProperties(Object source, Object target, boolean ignoreNull) {
    	if (ignoreNull) {
    		BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
		} else {
			BeanUtils.copyProperties(source, target);
		}
    }
    
    /**
     * 获取对象空属性名称
     * @param source
     * @return
     */
	private static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) {
				emptyNames.add(pd.getName());
			}
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}
}
