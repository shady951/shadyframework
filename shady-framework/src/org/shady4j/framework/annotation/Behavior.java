package org.shady4j.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法注解
 * @author tc
 * @since 1.0.0
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Behavior {
	/**
	 * 请求类型
	 */
	String method() default "get";
	
	/**
	 * 请求路径
	 */
	String path();
}
