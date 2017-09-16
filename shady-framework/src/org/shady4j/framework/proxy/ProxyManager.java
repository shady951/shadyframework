package org.shady4j.framework.proxy;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理实例创建器
 * @author tc
 * @since 1.0.0
 *
 */
public class ProxyManager {

	@SuppressWarnings("unchecked")
	public static <T> T creatProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
		//创建代理实例
		return (T) Enhancer.create(targetClass, new MethodInterceptor() {
			public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams,
					MethodProxy methodProxy) throws Throwable {
				//将切面类的执行顺序用ProxyChain来链式执行
				return new ProxyChain(targetClass, targetObject, targetMethod, methodProxy, methodParams, proxyList).doProxyChains();
			}
		});
	}
}
