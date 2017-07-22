package org.shady4j.framework.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编码与解码操作工具类
 * @author tc
 * @since 1.0.0
 *1
 */
public final class CodeUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeUtil.class);
	
	/**
	 * 解码URL 
	 */
	public static String decodeURL(String source) {
		String target = null;
		try {
			target = URLDecoder.decode(source, "UTF-8");
		} catch (Exception e) {
			LOGGER.error("decode url failure!!", e);
			throw new RuntimeException(e);
		}
		return target;
	}

	/**
	 * 编码URL 
	 */
	public static String encodeURL(String source) { //qeus:研究下url编码
		String target = null;
		try {
			target = URLEncoder.encode(source, "UTF-8");
		} catch (Exception e) {
			LOGGER.error("encode url failure!!", e);
			throw new RuntimeException(e);
		}
		return target;
	}
	
}
