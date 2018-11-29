package com.kingbo401.commons.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

/**
 * java包扫描
 */
public class PackageScanUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(PackageScanUtil.class);
    //扫描  scanPackages 下的文件的匹配符
    protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";


    /**
     * 结合spring的类扫描方式
     * 根据需要扫描的包路径及相应的注解，获取method集合
     * 仅返回public方法，如果方法是非public类型的，不会被返回
     * 可以扫描工程下的class文件及jar中的class文件
     *
     * @param scanPackages
     * @param annotation
     * @return
     */
    public static Set<Method> scanPackageMethod(String scanPackages, Class<? extends Annotation> annotation) {
        //获取所有的类
        Set<String> clazzSet = scanPackageClass(scanPackages);
        Set<Method> methods = new HashSet<Method>();
        //遍历类，查询相应的annotation方法
        for (String clazz : clazzSet) {
            try {
                Set<Method> ms = findClassMethod(clazz, annotation);
                if (ms != null) {
                    methods.addAll(ms);
                }
            } catch (ClassNotFoundException exception) {
            }
        }
        return methods;
    }
    
    /**
     * 获取类中有注解的public方法
     * @param fullClassName
     */
    public static Set<Method> findClassMethod(String fullClassName, Class<? extends Annotation> anno) throws ClassNotFoundException {
        Set<Method> methodSet = new HashSet<Method>();
        Class<?> clazz = Class.forName(fullClassName);
        if(clazz.isAnnotationPresent(Ignore.class)){
        	return methodSet;
        }
        Method[] methods = clazz.getDeclaredMethods();
        if(methods == null || methods.length == 0){
        	return methodSet;
        }
        for (Method method : methods) {
            if (method.getModifiers() != Modifier.PUBLIC) {
                continue;
            }
            if(method.isAnnotationPresent(Ignore.class)){
            	continue;
            }
            if(clazz.isAnnotationPresent(anno) || method.isAnnotationPresent(anno)){
            	methodSet.add(method);
            }
        }
        return methodSet;
    }
    
    /**
     * 结合spring的类扫描方式
     * 根据需要扫描的包路径，获取method集合
     * 仅返回public方法，如果方法是非public类型的，不会被返回
     * 可以扫描工程下的class文件及jar中的class文件
     *
     * @param scanPackages
     * @param annotation
     * @return
     */
    public static Set<Method> scanPackageMethod(String scanPackages) {
        //获取所有的类
        Set<String> clazzSet = scanPackageClass(scanPackages);
        Set<Method> methods = new HashSet<Method>();
        //遍历类，查询相应的annotation方法
        for (String clazz : clazzSet) {
            try {
                Set<Method> ms = findClassMethod(clazz);
                if (ms != null) {
                    methods.addAll(ms);
                }
            } catch (ClassNotFoundException exception) {
            }
        }
        return methods;
    }
    /**
     * 获取类中的public方法
     * @param fullClassName
     */
    public static Set<Method> findClassMethod(String fullClassName) throws ClassNotFoundException {
        Set<Method> methodSet = new HashSet<Method>();
        Class<?> clazz = Class.forName(fullClassName);
        if(clazz.isAnnotationPresent(Ignore.class)){
        	return methodSet;
        }
        Method[] methods = clazz.getDeclaredMethods();
        if(methods == null || methods.length == 0){
        	return methodSet;
        }
        for (Method method : methods) {
            if (method.getModifiers() != Modifier.PUBLIC) {
                continue;
            }
            if(method.isAnnotationPresent(Ignore.class)){
            	continue;
            }
            methodSet.add(method);
        }
        return methodSet;
    }

    /**
     * 根据扫描包的,查询下面的所有类
     *
     * @param scanPackages 扫描的package路径
     * @return
     */
    @SuppressWarnings("unchecked")
	public static Set<String> scanPackageClass(String scanPackages) {
        if (StringUtil.isBlank(scanPackages)) {
            return Collections.EMPTY_SET;
        }
        //验证及排重包路径,避免父子路径多次扫描
        Set<String> packages = optimizePackage(scanPackages);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        Set<String> clazzSet = new HashSet<String>();
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
                    String clazz = loadClassName(metadataReaderFactory, resource);
                    clazzSet.add(clazz);
                }
            } catch (Exception e) {
                LOGGER.error("获取包下面的类信息失败,package:" + basePackage, e);
            }

        }
        return clazzSet;
    }

    /**
     * 排重、检测package父子关系，避免多次扫描
     *
     * @param scanPackages
     * @return 返回检查后有效的路径集合
     */
    @SuppressWarnings("unchecked")
	private static Set<String> optimizePackage(String scanPackages) {
        if (StringUtil.isBlank(scanPackages)) {
            return Collections.EMPTY_SET;
        }
        Set<String> packages = new HashSet<String>();
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
     *
     * @param metadataReaderFactory spring中用来读取resource为class的工具
     * @param resource              这里的资源就是一个Class
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
}
