package org.shady4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 * @author tc
 * @since 1.0.0
 */
public final class StringUtil {

	public static final String SEPARATOR = String.valueOf((char) 29); //ques:有什么用

	public static boolean isEmpty(String str) {
		if(str != null) {
			str = str.trim();
		}
		return StringUtils.isEmpty(str); //null也返回true
	}
	
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String[] splitString(String body, String string) {
		return body.split(string);
	}
	
}
