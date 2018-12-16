package com.kingbo401.commons.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import com.kingbo401.commons.annotation.Ignore;
import com.kingbo401.commons.enums.ClazzType;

/**
 * java包扫描
 */
public class PackageScanUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(PackageScanUtil.class);
    //扫描  scanPackages 下的文件的匹配符
    protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    /**
     * 扫描包内类的public方法
     * @param scanPackages
     * @param classAnno
     * @param methodAnno
     * @return
     * @author kingbo401
     * @update 2018年12月16日 下午6:54:59
     */
    public static Set<Method> scanPackageClassMethod(String scanPackages, Class<? extends Annotation> classAnno, Class<? extends Annotation> methodAnno) {
        Set<Class<?>> clazzs = scanPackageClazz(scanPackages, classAnno, ClazzType.ClASS);
        Set<Method> methods = new HashSet<Method>();
        for (Class<?> clazz : clazzs) {
            Set<Method> ms = findClazzMethod(clazz, methodAnno);
            if (ms != null) {
                methods.addAll(ms);
            }
        }
        return methods;
    }
    
    /**
     * 扫描包内接口的方法
     * @param scanPackages
     * @param classAnno
     * @param methodAnno
     * @return
     * @author kingbo401
     * @update 2018年12月16日 下午6:55:28
     */
    public static Set<Method> scanPackageInterfaceMethod(String scanPackages, Class<? extends Annotation> classAnno, Class<? extends Annotation> methodAnno) {
        Set<Class<?>> clazzs = scanPackageClazz(scanPackages, classAnno, ClazzType.INTERFACE);
        Set<Method> methods = new HashSet<Method>();
        for (Class<?> clazz : clazzs) {
            Set<Method> ms = findClazzMethod(clazz, methodAnno);
            if (ms != null) {
                methods.addAll(ms);
            }
        }
        return methods;
    }
    
    /**
     * 查询clazz的public方法
     * @param clazz
     * @param anno
     * @return
     * @author kingbo401
     * @update 2018年12月16日 下午6:56:57
     */
    public static Set<Method> findClazzMethod(Class<?> clazz, Class<? extends Annotation> anno){
        Set<Method> rst = new HashSet<Method>();
        if(clazz.isAnnotationPresent(Ignore.class)){
        	return rst;
        }
        Method[] methods = clazz.getDeclaredMethods();
        if(methods == null || methods.length == 0){
        	return rst;
        }
        for (Method method : methods) {
            if (!clazz.isInterface() && method.getModifiers() != Modifier.PUBLIC) {
                continue;
            }
            if(method.isAnnotationPresent(Ignore.class)){
            	continue;
            }
            if(anno == null || clazz.isAnnotationPresent(anno) || method.isAnnotationPresent(anno)){
            	rst.add(method);
            }
        }
        return rst;
    }
    

    private static final Map<String, Set<Class<?>>> packasgeClazzCache = new ConcurrentHashMap<String, Set<Class<?>>>();
   
    /**
     * 扫描包内所有类
     * @param scanPackages
     * @return
     * @author kingbo401
     * @update 2018年12月16日 下午6:58:23
     */
	private static Set<Class<?>> scanPackageClazz(String scanPackages) {
    	Set<Class<?>> rst = packasgeClazzCache.get(scanPackages);
        if (CollectionUtil.isNotEmpty(rst)) {
            return rst;
        }
        rst = new HashSet<Class<?>>();
        //验证及排重包路径,避免父子路径多次扫描
        Set<String> packages = optimizePackage(scanPackages);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        for (String basePackage : packages) {
            if (StringUtil.isBlank(basePackage)) {
                continue;
            }
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage)) + "/" + DEFAULT_RESOURCE_PATTERN;
            try {
                Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    //检查resource，这里的resource都是class
                    String clazzName = loadClassName(metadataReaderFactory, resource);
                    Class<?> clazz = Class.forName(clazzName);
                    if(!clazz.isAnnotationPresent(Ignore.class)){
                    	rst.add(clazz);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("获取包下面的类信息失败,package:" + basePackage, e);
            }

        }
        packasgeClazzCache.put(scanPackages, rst);
        return rst;
    }
    
	/**
	 * 扫描包内制定类型，有注解的类
	 * @param scanPackages
	 * @param anno 可为空
	 * @param type
	 * @return
	 * @author kingbo401
	 * @update 2018年12月16日 下午6:58:42
	 */
    public static Set<Class<?>> scanPackageClazz(String scanPackages, Class<? extends Annotation> anno, ClazzType type){
    	Set<Class<?>> clazzs = scanPackageClazz(scanPackages);
    	Set<Class<?>> rst = new HashSet<Class<?>>();
    	if(CollectionUtil.isEmpty(clazzs)){
    		return rst;
    	}
    	for(Class<?> clazz : clazzs){
    		if(anno != null && !clazz.isAnnotationPresent(anno)){
    			continue;
    		}
    		if(type.equals(ClazzType.ClASS) && clazz.isInterface()){
    			continue;
    		}
    		if(type.equals(ClazzType.INTERFACE) && !clazz.isInterface()){
    			continue;
    		}
    		rst.add(clazz);
    	}
    	return rst;
    }

    /**
     * 排重、检测package父子关系，避免多次扫描
     * @param scanPackages
     * @return 返回检查后有效的路径集合
     */
	private static Set<String> optimizePackage(String scanPackages) {
        Set<String> packages = new HashSet<String>();
        if (StringUtil.isBlank(scanPackages)) {
            return packages;
        }
        //排重路径
        Collections.addAll(packages, scanPackages.split(","));
        for (String packageItem : packages) {
            if (StringUtil.isBlank(packageItem) || packageItem.equals(".") || packageItem.startsWith(".")) {
                continue;
            }
            if (packageItem.endsWith(".")) {
                packageItem = packageItem.substring(0, packageItem.length() - 1);
            }
            Iterator<String> packageIter = packages.iterator();
            boolean needAdd = true;
            while (packageIter.hasNext()) {
                String pack = packageIter.next();
                if (packageItem.startsWith(pack + ".")) {
                    //如果待加入的路径是已经加入的pack的子集，不加入
                    needAdd = false;
                } else if (pack.startsWith(packageItem + ".")) {
                    //如果待加入的路径是已经加入的pack的父集，删除已加入的pack
                    packageIter.remove();
                }
            }
            if (needAdd) {
                packages.add(packageItem);
            }
        }
        return packages;
    }


    /**
     * 加载资源，根据resource获取className
     * @param metadataReaderFactory spring中用来读取resource为class的工具
     * @param resource              
     * @throws IOException
     */
    private static String loadClassName(MetadataReaderFactory metadataReaderFactory, Resource resource) throws IOException {
        try {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (metadataReader != null) {
                    return metadataReader.getClassMetadata().getClassName();
                }
            }
        } catch (Exception e) {
            LOGGER.error("根据resource获取类名称失败", e);
        }
        return null;
    }
    
    /**
     * 获取方法名唯一标识
     * @param method
     * @return
     */
    public static String getMethodKey(Method method) {
		StringBuilder sb = new StringBuilder();
		sb.append(method.getDeclaringClass().getName());
		sb.append(".");
		sb.append(method.getName());
		sb.append("(");
		Class<?>[] params = method.getParameterTypes();
		for (int j = 0; j < params.length; j++) {
			sb.append(params[j].getCanonicalName());
			if (j < (params.length - 1)){
				sb.append(',');
			}
		}
		sb.append(")");
		return sb.toString();
	}
    
    public static void main(String[] args) {
    	System.out.println(scanPackageClazz("com.kingbo401.commons.sensitive"));
		System.out.println(scanPackageClazz("com.kingbo401.commons.sensitive", null, ClazzType.ClASS));
		System.out.println(scanPackageClazz("com.kingbo401.commons.sensitive", null, ClazzType.INTERFACE));
		System.out.println(scanPackageClassMethod("com.kingbo401.commons.sensitive", null, null));
	}
}
