package org.shady4j.framework.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.shady4j.framework.annotation.Behavior;
import org.shady4j.framework.bean.Handler;
import org.shady4j.framework.bean.Request;
import org.shady4j.framework.util.ArrayUtil;
import org.shady4j.framework.util.CollectionUtil;

/**
 * 控制器助手类
 * @author tc
 * @since 1.0.0
 *1
 */
public final class ControllerHelper {
	
	private static final Map<Request, Handler> BEHAVIOR_MAP = new HashMap<Request, Handler>();
	
	static {
		//获取所有带Controller注解的类
		System.out.println("controllerhelper static!");
		Set<Class<?>> controllerSet = ClassHelper.getControllerClassSet();
		System.out.println("11");
		if(CollectionUtil.isNotEmpty(controllerSet)) {
			System.out.println("12");
			for(Class<?> clazz : controllerSet) {
				//获取类下所有定义的方法
				Method[] methods = clazz.getDeclaredMethods();
				if(ArrayUtil.isNotEmpty(methods)){
					System.out.println("13");
					for(Method method : methods) {
						//判断当前方法是否带有Behavior注解
						if(method.isAnnotationPresent(Behavior.class)) {
							System.out.println("14");
							String behaviorValue = method.getAnnotation(Behavior.class).value();
							System.out.println("behaviorValue:"+behaviorValue);
							//匹配请求路径
							if(behaviorValue.matches("\\w+:/\\w*")) {
								System.out.println("matche success!");
								String[] valueArray = behaviorValue.split(":");
								if(ArrayUtil.isNotEmpty(valueArray) && valueArray.length == 2) {
									//冒号前面为请求方式，后面为请求路径
									Request request = new Request(valueArray[0], valueArray[1]);
									Handler handler = new Handler(clazz, method);
									BEHAVIOR_MAP.put(request, handler);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static Handler getHandler(String requestMethod, String requestPath) {
		Request request = new Request(requestMethod, requestPath);
		//<
		System.out.println(request);
		System.out.println("=======");
		for(Map.Entry<Request, Handler> s : BEHAVIOR_MAP.entrySet())	 {
			System.out.println(s.getKey());
			System.out.println(s.getValue());
			System.out.println("--------");
		}
		//>
		return BEHAVIOR_MAP.get(request); //ques:关于判断两个对象是否相同
	}
}
