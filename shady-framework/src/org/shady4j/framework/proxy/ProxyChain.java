package org.shady4j.framework.proxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理链
 * @author tc
 * @since 1.0.0
 * 
 */
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理链
 * @author tc
 * @since 1.0.0
 *
 */
public class ProxyChain {

	//目标类
	private final Class<?> targetClass;
	//目标类实例
	private final Object targetObject;
	//目标类实例的方法
	private final Method targetMethod;
	//方法代理
	private final MethodProxy methodProxy;
	//目标类实例的方法的参数
	private final Object[] methodParams;
	//切面对象集合
	private List<Proxy> proxyList;
	//切面类计数
	private int proxyIndex = 0;

	public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams,
			List<Proxy> proxyList) {
		this.targetClass = targetClass;
		this.targetObject = targetObject;
		this.targetMethod = targetMethod;
		this.methodProxy = methodProxy;
		this.methodParams = methodParams;
		this.proxyList = proxyList;
	}

	/**
	 * 递归执行代理链 
	 */
	public Object doProxyChains() throws Throwable {
		Object result;
		//判断代理链是否到末尾
		if(proxyIndex < proxyList.size()) {
			//递归调用doProxy()来执行每个代理的begin(),before()方法
			result = proxyList.get(proxyIndex++).doProxy(this);
		} else {
			//当所有的代理执行完begin(),before()方法方法后，才执行目标类实例的原本方法
			result = methodProxy.invokeSuper(targetObject, methodParams);
		}
		return result;
	}
	
	public Class<?> getTargetClass() {
		return targetClass;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}

	public Object[] getMethodParams() {
		return methodParams;
	}
	

	

}
