package org.shady4j.framework.proxy;

/**
 * 代理接口
 * @author tc
 * @since 1.0.0
 *
 */
public interface Proxy {

	/**
	 * 执行链式代理
	 */
	Object doProxy(ProxyChain proxyChain) throws Throwable;
}
