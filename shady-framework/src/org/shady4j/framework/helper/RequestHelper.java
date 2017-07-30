package org.shady4j.framework.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.shady4j.framework.bean.FormParam;
import org.shady4j.framework.bean.Param;
import org.shady4j.framework.util.ArrayUtil;
import org.shady4j.framework.util.CodeUtil;
import org.shady4j.framework.util.StreamUtil;
import org.shady4j.framework.util.StringUtil;

/**
 * 请求助手类
 * @author tc
 * @since 1.1.0
 *
 */
public final class RequestHelper {
	
	/**
	 * 创建请求对象
	 */
	public static Param createParam(HttpServletRequest request) throws IOException {
		List<FormParam>	formParamList = new ArrayList<FormParam>();
		//获取默认编码格式提交的表单数据
		formParamList.addAll(parseParameterNames(request));
		//获取enctype=multipart/form-data编码格式提交的表单数据
		formParamList.addAll(parseInputStream(request));
		return new Param(formParamList);
	}

	private static Collection<? extends FormParam> parseParameterNames(HttpServletRequest request) {
		//创建包含请求参数表单的对象集合
		List<FormParam> formParamList = new ArrayList<FormParam>();
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String fieldName = paramNames.nextElement();
			String[] fieldValues = request.getParameterValues(fieldName);
			if(ArrayUtil.isNotEmpty(fieldValues)) {
				Object fieldValue;
				if(fieldValues.length == 1) {
					fieldValue = fieldValues[0];
				} else {
					StringBuilder sb = new StringBuilder("");
					for(int i = 0; i < fieldValues.length; i++) {
						sb.append(fieldValues[i]);
						if(i != fieldValues.length - 1) {
							sb.append(StringUtil.SEPARATOR);
						}
					}
					fieldValue = sb.toString();
				}
				formParamList.add(new FormParam(fieldName, fieldValue));
			}
		}
		return formParamList;
	}

	private static Collection<? extends FormParam> parseInputStream(HttpServletRequest request) throws IOException {
		List<FormParam> formParamList = new ArrayList<FormParam>();
		/*
		 *当form表单内容采用 enctype=multipart/form-data编码时
		 *调用request.getParameter()得不到数据,需要通过流获取。
		 */
		String body = CodeUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
		if(StringUtil.isNotEmpty(body)) {
			String[] params = StringUtil.splitString(body, "&");
			if(ArrayUtil.isNotEmpty(params)) {
				for(String param : params) {
					String[] array = StringUtil.splitString(param, "=");
					if(ArrayUtil.isNotEmpty(array) && array.length == 2) {
						String fieldName = array[0];
						String fieldValue = array[1];
						formParamList.add(new FormParam(fieldName, fieldValue));
					}
				}
			}
		}
		return formParamList;
	}
}
