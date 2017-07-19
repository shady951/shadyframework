package org.shady4j.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.shady4j.framework.util.ReflectionUtil;

/**
 * Bean助手类
 * @author tc
 * @since 1.0.0
 *
 */
public final class BeanHelper {
	
	/**
	 * 定义Bean映射
	 */
	private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();
	
	static {
		Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
		for(Class<?> beanClass : beanClassSet) {
			Object obj = ReflectionUtil.newInstance(beanClass);
			BEAN_MAP.put(beanClass, obj);
		}
	}
	
	/**
	 * 获取Bean映射 
	 */
	public static Map<Class<?>, Object> getBeanMap() {
		return BEAN_MAP;
	}
	
	/**
	 * 获取Bean实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		if(!BEAN_MAP.containsKey(clazz)) {
			throw new RuntimeException("can not get bean by class:" + clazz);
		}
		return (T) BEAN_MAP.get(clazz);
	}
}
