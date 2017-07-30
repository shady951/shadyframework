package org.shady4j.framework.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.shady4j.framework.annotation.Aspect;
import org.shady4j.framework.proxy.AspectProxy;
import org.shady4j.framework.proxy.Proxy;
import org.shady4j.framework.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 切面助手类
 * @author tc
 * @since 1.0.0
 * note：该类对书上的内容进行了精简，可能提高了耦合性。
 */
public final class AopHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);
	
	static {
		try {
			Map<Class<?>, List<Proxy>> targetMap = createTargetMap();
			for(Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
				Class<?> targetClass = targetEntry.getKey();
				//创建目标类的代理实例
				Object proxyObj = ProxyManager.creatProxy(targetEntry.getKey(), targetEntry.getValue());
				//将Bean类与Bean类实例的映射覆盖为Bean类与Bean类代理实例的映射
				BeanHelper.setBean(targetClass, proxyObj);
			}
		} catch (Exception e) {
			LOGGER.error("aop failure!!", e);
		}
	}
	
	/**
	 *	获取目标对象与切面对象集合的映射
	 */
	private static Map<Class<?>, List<Proxy>> createTargetMap() throws Exception {
		//获取切面对象集合
		Set<Class<?>> proxySet = ClassHelper.getClassSetBySuper(AspectProxy.class);
		//获取所有Bean实例
		Set<Class<?>> classSet = ClassHelper.getBeanClassSet();
		Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
		for(Class<?> proxyClass : proxySet) {
			//筛选切面对象，切面对象必须满足：1、继承AspectProxy。2、拥有Aspect注解
			if(proxyClass.isAnnotationPresent(Aspect.class))	 {
				//获取切面对象的注解类
				Aspect aspect = proxyClass.getAnnotation(Aspect.class);
				for(Class<?> clazz : classSet) {
					//判断目标对象的注解类，与aspect对象里面表示的注解类，是否为相同注解类
					if(clazz.isAnnotationPresent(aspect.Value()))	{
						//创建切面对象
						Proxy proxy = (Proxy)proxyClass.newInstance();
						//添加目标类与切面对象集合的映射
						if(targetMap.containsKey(clazz)) {
							targetMap.get(clazz).add(proxy);
						} else {
							List<Proxy> proxyList = new ArrayList<Proxy>();
							proxyList.add(proxy);
							targetMap.put(clazz, proxyList);
						}
					}
				}
			}
		}
		LOGGER.info("proxy set has " + proxySet.size() + " members");
		return targetMap;
	}
}
