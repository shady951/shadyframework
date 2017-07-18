package org.shady4j.framework.helper;

import java.util.HashSet;
import java.util.Set;

import org.shady4j.framework.annotation.Controller;
import org.shady4j.framework.annotation.Service;
import org.shady4j.framework.util.ClassUtil;

/**
 * 类操作助手类
 * @author tc
 * @since 1.0.0
 */
public final class ClassHelper {
	
	private static final Set<Class<?>> class_Set;

	/**
	 * 定义类集合(用于存放所加载的类)
	 */
	static{
		String basePackage = ConfigHelper.getJdbcAppBasePackage();
		class_Set = ClassUtil.getClassSet(basePackage);
	}
	
	/**
	 * 获取应用包名下的所有类 
	 */
	public static Set<Class<?>> getClassSet() {
		return class_Set;
	}
	
	/**
	 * 获取应用包名下所有注释过Service的类
	 */
	public static Set<Class<?>> getServiceClassSet() {
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> clazz : class_Set) {
			if(clazz.isAnnotationPresent(Service.class)) {
				classSet.add(clazz);
			}
		}
		return classSet;
	}
	
	/**
	 * 获取应用包名下所有注释过Controller的类
	 */
	public static Set<Class<?>> getControllerClassSet() {
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> clazz : class_Set) {
			if(clazz.isAnnotationPresent(Controller.class)) {
				classSet.add(clazz);
			}
		}
		return classSet;
	}
	
	/**
	 * 获取应用包名下所有注释过Controller的类
	 */
	public static Set<Class<?>> getBeanClassSet() {
		Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
		beanClassSet.addAll(getControllerClassSet());
		beanClassSet.addAll(getServiceClassSet());
		return beanClassSet;
	}
	
}
