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
 * @since 1.1.0
 * note1：将一个目标类配一个切面类实例修改为单例(即：一个切面类配所有目标类)，增强性能
 * note2：将以注解为切面修改为以类为切面，增强切面精细度
 * note3：对书上的内容进行了重构以精简代码，但可能提高了耦合。
 * note4：书上框架对于切面类并没有进行管理，导致切面类无法让成员变量依赖注入，现已做优化
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
	 *	获取目标类与切面类实例集合的映射
	 */
	private static Map<Class<?>, List<Proxy>> createTargetMap() throws Exception {
		//获取继承了AspectProxy类的对象集合
		Set<Class<?>> proxySet = ClassHelper.getClassSetBySuper(AspectProxy.class);
		//获取所有Bean类
		Set<Class<?>> classSet = ClassHelper.getBeanClassSet();
		//获取所有Bean类与实例的映射
		Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
		//创建目标class与切面类的映射
		Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
		for(Class<?> proxyClass : proxySet) {
			//筛选切面对象，切面对象必须满足：1、继承AspectProxy。2、拥有Aspect注解
			if(proxyClass.isAnnotationPresent(Aspect.class))	 {
				//获取切面类实例
				Proxy proxy = (Proxy)beanMap.get(proxyClass);
				//获取切面对象的注解类
				Aspect aspect = proxyClass.getAnnotation(Aspect.class);
				for(Class<?> clazz : classSet) {
					//判断目标对象的类全名，与aspect对象里面表示的类全名，是否相同，不区分大小写
					if(clazz.getCanonicalName().toLowerCase().equals(aspect.Value().toLowerCase())) {
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
		LOGGER.info("targetMap has " + targetMap.size() + " members");
		return targetMap;
	}
	
}
