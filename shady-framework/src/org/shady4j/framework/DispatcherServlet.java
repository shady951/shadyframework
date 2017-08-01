package org.shady4j.framework;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.shady4j.framework.helper.RequestHelper;
import org.shady4j.framework.helper.ServletHelper;
import org.shady4j.framework.helper.UploadHelper;
import org.shady4j.framework.util.JsonUtil;
import org.shady4j.framework.util.ReflectionUtil;
import org.shady4j.framework.util.StringUtil;

/**
 * 请求转发类
 * @author tc
 * @since 1.1.0
 *3
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{

	@Override
	public void init(ServletConfig config) throws ServletException {
		//初始化相关Helper类
		HelperLoader.init();
		//获取ServletContext对象，用于注册Servlet
		ServletContext servletContext = config.getServletContext();
		//注册处理JSP的Serlvet
		ServletRegistration jspServlet = servletContext.getServletRegistration("jsp"); // ques:各种注册有什么用,重点！
		jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
		//注册处理静态资源的默认Serlvet
		ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
		defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
		//加载文件上传模块
		UploadHelper.init(servletContext);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletHelper.init(request, response);
		try {
			//获取请求方法与请求路径
			String requestMethod = request.getMethod().toLowerCase();
			String requestPath = request.getPathInfo();
			//跳过/favicon.ico请求
			if(requestPath.equals("/favicon.ico")) { //ques:这是什么请求
				return;
			}
			//与带有Behavior注解的方法配对，获取其处理器
			Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
			if(handler != null) {
				//获取Controller类及其Bean实例
				Class<?> controllerClass = handler.getControllerClass();
				Object controllerObj = BeanHelper.getBean(controllerClass);
				Param param;
				if(UploadHelper.isMultipart(request)) {
					param = UploadHelper.creatParam(request);
				} else {
					param = RequestHelper.createParam(request);
				}
				/*
				//<7.27,添加处理json的功能
				if (request.getContentType().equals("application/json")) {
					String jsonString = StreamUtil.getString(request.getInputStream())
					JsonUtil.fromJson(jsonString, );
				}
				//>
				//调用带Behavior注解的配对方法
				 */
				Object result = ReflectionUtil.invokeMethod(controllerObj, handler.getBehaviorMethod(), param);
				if(result instanceof View) {
					handleViewResult((View) result, request, response);
				} else if(result instanceof Data) {
					handleDataResult((Data) result, request, response);
				}
			}
		} finally {
			ServletHelper.destroy();
		}
	}

	private void handleDataResult(Data data, HttpServletRequest request, HttpServletResponse response) throws IOException {
		//返回JSON数据
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

	private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String path = view.getPath();
		if(StringUtil.isNotEmpty(path)) {
			//以开头是否有"/"来判定是转发还是重定向
			if(path.startsWith("/")) {
				response.sendRedirect(request.getContextPath() + path);
			} else {
				Map<String, Object> medel = view.getModel();
				medel.putAll(view.getModel());
				for(Map.Entry<String, Object> medelEntry : medel.entrySet()) {
					request.setAttribute(medelEntry.getKey(), medelEntry.getValue());
				}
				request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
			}
		}
		
	}
	
}
