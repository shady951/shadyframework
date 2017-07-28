package org.shady4j.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shady4j.framework.bean.Data;
import org.shady4j.framework.bean.Handler;
import org.shady4j.framework.bean.Param;
import org.shady4j.framework.bean.View;
import org.shady4j.framework.helper.BeanHelper;
import org.shady4j.framework.helper.ConfigHelper;
import org.shady4j.framework.helper.ControllerHelper;
import org.shady4j.framework.util.ArrayUtil;
import org.shady4j.framework.util.CodeUtil;
import org.shady4j.framework.util.JsonUtil;
import org.shady4j.framework.util.ReflectionUtil;
import org.shady4j.framework.util.StreamUtil;
import org.shady4j.framework.util.StringUtil;

/**
 * 请求转发类
 * @author tc
 * @since 1.0.0
 *3
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{

	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("1");
		//初始化相关Helper类
		HelperLoader.init();
		//获取ServletContext对象，用于注册Servlet
		ServletContext servletContext = config.getServletContext();
		//注册处理JSP的Serlvet
		ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
		jspServlet.addMapping(ConfigHelper.getJdbcAppJspPath() + "*");
		//注册处理静态资源的默认Serlvet
		ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
		defaultServlet.addMapping(ConfigHelper.getJdbcAppAssetPath() + "*");
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("2");
		//获取请求方法与请求路径
		String requestMethod = request.getMethod().toLowerCase();
		String requestPath = request.getPathInfo();
		System.out.println("rm:"+requestMethod);
		System.out.println("rp:"+requestPath);
		//与带有Behavior注解的方法配对，获取其处理器
		Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
		if(handler != null) {
			//获取Controller类及其Bean实例
			Class<?> controllerClass = handler.getControllerClass();
			Object controllerObj = BeanHelper.getBean(controllerClass);
			//创建请求参数对象
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Enumeration<String> enmuration = request.getParameterNames();
			while(enmuration.hasMoreElements()) {
				String paramName = enmuration.nextElement();
				String paramValue = request.getParameter(paramName);
				paramMap.put(paramName, paramValue);
			}
			/*
			 *当form表单内容采用 enctype=multipart/form-data编码时
			 *调用request.getParameter()得不到数据,需要通过流获取。
			 * 参考地址:http://www.cnblogs.com/xiancheng/p/5524338.html
			 */
			String body = CodeUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
			if(StringUtil.isNotEmpty(body)) {
				String[] params = StringUtil.splitString(body, "&"); //qeus:感觉没有必要封装
				if(ArrayUtil.isNotEmpty(params)) {
					for(String param : params) {
						String[] array = StringUtil.splitString(param, "=");
						if(ArrayUtil.isNotEmpty(array) && array.length == 2) {
							String paramName = array[0];
							String paramValue = array[1];
							paramMap.put(paramName, paramValue);
						}
					}
				}
			}
			Param param = new Param(paramMap);
			//<7.27,添加处理json的功能
//			if (request.getContentType().equals("application/json")) {
//				String jsonString = StreamUtil.getString(request.getInputStream())
//				JsonUtil.fromJson(jsonString, );
//			}
			//>
			//调用带Behavior注解的配对方法
			System.out.println("3");
			Object result = ReflectionUtil.invokeMethod(controllerObj, handler.getBehaviorMethod(), param);
			System.out.println("4");
			if(result instanceof View) {
				//返回JSP页面
				View view = (View) result;
				String path = view.getPath();
				if(StringUtil.isNotEmpty(path)) {
					//guess:以开头是否有"/"来判定是转发还是重定向
					if(path.startsWith("/")) {
						response.sendRedirect(request.getContextPath() + path);
					} else {
						Map<String, Object> map = param.getMap();
						map.putAll(view.getModel()); //<修复bug>
						for(Map.Entry<String, Object> mapEntry : map.entrySet()) {
							request.setAttribute(mapEntry.getKey(), mapEntry.getValue());
						}
						request.getRequestDispatcher(ConfigHelper.getJdbcAppJspPath() + path).forward(request, response);
					}
				}
			} else if(result instanceof Data) {
				//返回JSON数据
				Data data = (Data) result;
				Object model = data.getModel();
				if(model != null) {
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					PrintWriter writer = response.getWriter();
					String json = JsonUtil.toJson(model);
					writer.write(json);
					writer.flush();
					writer.close();
				}
			}
		}
		System.out.println("5");
	}
	
}
