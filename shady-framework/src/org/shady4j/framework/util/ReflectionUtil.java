package org.shady4j.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 * @author tc
 * @since 1.0.0
 *
 */
public final class ReflectionUtil {

	private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);
	
	/**
	 * 创建实例
	 */
	public static Object newInstance(Class<?> clazz) {
		Object instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			logger.error("new instance failure!!", e);
			throw new RuntimeException(e);
		}
		return instance;
	}
	
	/**
	 * 调用方法 
	 */
	public static Object invokeMethod(Object obj, Method method, Object...args) {
		Object result = null;
		try {
			method.setAccessible(true);
			result = method.invoke(obj, args);
		} catch (Exception e) {
			logger.error("invoke method failure!!", e);
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * 设置成员变量
	 */
	public static void setField(Object obj, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			logger.error("set field failure!!", e);
			throw new RuntimeException(e);
		}
	}
	
}