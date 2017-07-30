package org.shady4j.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.shady4j.framework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bean助手类
 * @author tc
 * @since 1.0.0
 *1
 */
public final class BeanHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanHelper.class);
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
		LOGGER.info("bean map has " + BEAN_MAP.size() + " members");
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
	
	/**
	 * 设置Bean实例（代理类覆盖）
	 */
	public static void setBean(Class<?> clazz, Object obj) {
		BEAN_MAP.put(clazz, obj); //qeus:需要研究一下hashcode
	}
}
