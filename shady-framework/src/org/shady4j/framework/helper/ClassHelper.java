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
	
	private static final Set<Class<?>> CLASS_SET;

	/**
	 * 定义类集合(用于存放所加载的类)
	 */
	static{
		String basePackage = ConfigHelper.getJdbcAppBasePackage();
		CLASS_SET = ClassUtil.getClassSet(basePackage);
	}
	
	/**
	 * 获取应用包名下的所有类 
	 */
	public static Set<Class<?>> getClassSet() {
		return CLASS_SET;
	}
	
	/**
	 * 获取应用包名下所有注释过Service的类
	 */
	public static Set<Class<?>> getServiceClassSet() {
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> clazz : CLASS_SET) {
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
		//<
		System.out.println("CLASS_SET size:"+CLASS_SET.size());
		//>
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> clazz : CLASS_SET) {
			if(clazz.isAnnotationPresent(Controller.class)) {
				classSet.add(clazz);
			}
		}
		return classSet;
	}
	
	/**
	 * 获取应用包名下所有Bean类
	 */
	public static Set<Class<?>> getBeanClassSet() {
		Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
		beanClassSet.addAll(getControllerClassSet());
		beanClassSet.addAll(getServiceClassSet());
		return beanClassSet;
	}
	
	/**
	 * 获取应用包名下某父类(或接口)的所有子类
	 */
	public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for(Class<?> clazz : CLASS_SET) {
			if(superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
				classSet.add(clazz);
			}
		}
		return classSet;
	}
	
}
