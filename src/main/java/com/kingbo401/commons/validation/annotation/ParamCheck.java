package com.kingbo401.commons.validation.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 方法参数校验
 * @author kingbo401
 * @date 2019年7月20日
 */
@Target({ METHOD})
@Retention(RUNTIME)
@Documented
public @interface ParamCheck {
    
	/**
	 * 校验分组
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Class[] value();
}
