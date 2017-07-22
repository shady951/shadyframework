package org.shady4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON工具类
 * @author tc
 * @since 1.0.0
 *1
 */
public final class JsonUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	/**
	 * 将POJO 转为JSON 
	 */
	public static <T> String toJson(T obj) { //ques:好好研究泛型
		String json;
		try {
			json = OBJECT_MAPPER.writeValueAsString(obj);
		} catch (Exception e) {
			LOGGER.error("convert POJO to JSON failure!!", e);
			throw new RuntimeException(e);
		}
		return json;
	}

	/**
	 * 将JSON转为POJO 
	 */
	public static <T> T fromJson(String json, Class<T> type) { //ques:好好研究泛型
		T pojo;
		try {
			pojo = OBJECT_MAPPER.readValue(json, type);
		} catch (Exception e) {
			LOGGER.error("convert JSON to POJO failure!!", e);
			throw new RuntimeException(e);
		}
		return pojo;
	}
}
