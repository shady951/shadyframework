package org.shady4j.framework;

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
		Class<?>[] classList = {
				BeanHelper.class,
				ClassHelper.class,
				ControllerHelper.class,
				IocHelper.class};
		//加载含有static块的类
		for(Class<?> clazz : classList) {
			ClassUtil.loadClass(clazz.getName(), true);
		}
	}
	
}
