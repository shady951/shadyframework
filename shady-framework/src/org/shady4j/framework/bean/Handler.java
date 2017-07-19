package org.shady4j.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装Behavior信息
 * @author tc
 * @since 1.0.0
 *
 */
public class Handler {

	/**
	 * Controller类
	 */
	private Class<?> controllerClass;
	
	/**
	 * Behavior方法
	 */
	private Method actionMethod;

	public Handler(Class<?> controllerClass, Method actionMethod) {
		this.controllerClass = controllerClass;
		this.actionMethod = actionMethod;
	}

	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public Method getActionMethod() {
		return actionMethod;
	}
	
}
