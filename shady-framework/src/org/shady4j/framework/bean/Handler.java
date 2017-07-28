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
	private Method behaviorMethod;

	public Handler(Class<?> controllerClass, Method behaviorMethod) {
		this.controllerClass = controllerClass;
		this.behaviorMethod = behaviorMethod;
	}

	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public Method getBehaviorMethod() {
		return behaviorMethod;
	}
	
}
