package org.shady4j.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;

import org.shady4j.framework.annotation.Inject;
import org.shady4j.framework.util.ArrayUtil;
import org.shady4j.framework.util.CollectionUtil;
import org.shady4j.framework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 依赖注入助手类
 * @author tc
 * @since 1.0.0 当前版本该类所创建管理的对象均为单例
 */
public final class IocHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IocHelper.class);
	
	static {
		//获取所有Bean类与Bean实例之间的映射关系
		Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
		if(CollectionUtil.isNotEmpty(beanMap)) {
			//遍历beanMap
			for(Map.Entry<Class<?>, Object> mapEntry : beanMap.entrySet()) {
				//从beanMap获取Bean类
				Class<?> clazz = mapEntry.getKey();
				//获取Bean类定义的所有成员变量
				Field[] fields = clazz.getDeclaredFields();
				if(ArrayUtil.isNotEmpty(fields)) {
					//遍历fields
					for(Field field : fields) {
						//判断当前fields是否带有Inject注解
						if(field.isAnnotationPresent(Inject.class)){
							//从beanMap获取Bean实例obj
							Object obj = mapEntry.getValue();
							//从beanMap获取obj实例中被Inject注解的实例
							Object injectObj = beanMap.get(field.getType());
							//判断injectObj实例是否被包含在beanMap中
							if(injectObj != null) {
								//用反射初始化obj实例中被Inject注解的成员变量
								ReflectionUtil.setField(obj, field, injectObj);
							}
						}
					}
				}
			}
		}
		LOGGER.info("IOC Initial success");
	}
}
