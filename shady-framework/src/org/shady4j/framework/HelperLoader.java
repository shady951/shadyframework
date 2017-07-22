package org.shady4j.framework;

import org.shady4j.framework.helper.AopHelper;
import org.shady4j.framework.helper.BeanHelper;
import org.shady4j.framework.helper.ClassHelper;
import org.shady4j.framework.helper.ControllerHelper;
import org.shady4j.framework.helper.IocHelper;
import org.shady4j.framework.util.ClassUtil;

/**
 * 加载相应的Helper类
 * @author tc
 * @since 1.0.0
 *
 */
public final class HelperLoader {
	
	public static void init() {
		//AopHelper要在IocHelper之前加载，因为首先要通过AopHelper获取代理实例，再进行依赖注入
		Class<?>[] classList = {
				ClassHelper.class,
				BeanHelper.class,
				AopHelper.class,
				IocHelper.class,
				ControllerHelper.class};
		//加载含有static块的类
		for(Class<?> clazz : classList) {
			ClassUtil.loadClass(clazz.getName(), true);
		}
	}
	
}
