package org.shady4j.framework.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 切面代理
 * @author tc
 * @since 1.0.0
 *
 */
public abstract class AspectProxy implements Proxy{

	private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);
	
	/**
	 * 执行切面逻辑
	 */
	public final Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result = null;
		Class<?> targetClass = proxyChain.getTargetClass();
		Method targetMethod = proxyChain.getTargetMethod();
		Object[] methodParams = proxyChain.getMethodParams();
		begin();
		try {
			if(intercept(targetClass, targetMethod, methodParams)) {
				before(targetClass, targetMethod, methodParams);
				result = proxyChain.doProxyChains();
				after(targetClass, targetMethod, methodParams, result);
			} else {
				result = proxyChain.doProxyChains();
			}
		} catch (Exception e) {
			LOGGER.error("proxy failure!!", e);
			error(targetClass, targetMethod, methodParams, e);
			throw e;
		} finally {
			end();
		}
		return result;
	}

	/**
	 * 在进入方法时执行
	 */
	public void begin() {
	}

	/**
	 * 用于设置拦截过滤条件
	 */
	public boolean intercept(Class<?> targetClass, Method targetMethod, Object[] methodParams) throws Throwable {
		return true;
	}
	
	/**
	 * 在目标方法调用前执行
	 */
	public void before(Class<?> targetClass, Method targetMethod, Object[] methodParams) throws Throwable {
	}
	
	/**
	 * 在目标方法调用后执行
	 */
	public void after(Class<?> targetClass, Method targetMethod, Object[] methodParams, Object result) throws Throwable {
	}
	
	/**
	 * 在抛出异常时执行
	 */
	public void error(Class<?> targetClass, Method targetMethod, Object[] methodParams, Throwable e) {
	}

	/**
	 * 在退出方法时执行
	 */
	public void end() {
	}
	
}
