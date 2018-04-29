package com.kingbo401.commons.spring.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringBeanFactory implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringBeanFactory.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T)applicationContext.getBean(name);
	}
	
	public static <T> T getBean(String name, Class<T> clazz){
		return applicationContext.getBean(name, clazz);
	}
	
	public static <T> T getBean(Class<T> clazz){
		return applicationContext.getBean(clazz);
	}
} 
