package org.shady4j.framework.util;

/**
 * 转型操作工具类
 * @author tc
 * @since 1.0.0
 */
public final class CastUtil {

	/**
	 * 转为String型
	 */
	public static String castString(Object obj) {
		return CastUtil.castString(obj, "");
	}

	/**
	 * 转为String型(提供默认值)
	 */
	public static String castString(Object obj, String defaultValue) {
		return obj != null ? String.valueOf(obj) : defaultValue;
	}

	/**
	 * 转为double型
	 */
	public static double castDouble(Object obj) {
		return CastUtil.castDouble(obj, 0);
	}
	
	/**
	 * 转为double型(提供默认值)
	 */
	public static double castDouble(Object obj, double defaultValue) {
		double doubleValue = defaultValue;
		if(obj != null) { //提高性能
			String str = castString(obj);
			if(StringUtil.isNotEmpty(str)){
				try {
					doubleValue = Double.parseDouble(str);
				} catch (NumberFormatException e) {
					doubleValue = defaultValue;
				}
			}
		}
		return doubleValue;
	}
	
	/**
	 * 转为long型
	 */
	public static long castLong(Object obj) {
		return CastUtil.castLong(obj, 0);
	}
	
	/**
	 * 转为long型(提供默认值)
	 */
	public static long castLong(Object obj, long defaultValue) {
		long longValue = defaultValue;
		if(obj != null) { //提高性能
			String str = castString(obj);
			if(StringUtil.isNotEmpty(str)){
				try {
					longValue = Long.parseLong(str);
				} catch (NumberFormatException e) {
					longValue = defaultValue;
				}
			}
		}
		return longValue;
	}
	
	/**
	 * 转为int型
	 */
	public static int castInt(Object obj) {
		return CastUtil.castInt(obj, 0);
	}
	
	/**
	 * 转为int型(提供默认值)
	 */
	public static int castInt(Object obj, int defaultValue) {
		int intValue = defaultValue;
		if(obj != null) { //提高性能
			String str = castString(obj);
			if(StringUtil.isNotEmpty(str)){
				try {
					intValue = Integer.parseInt(str);
				} catch (NumberFormatException e) {
					intValue = defaultValue;
				}
			}
		}
		return intValue;
	}
	
	/**
	 * 转为boolean型
	 */
	public static boolean castBoolean(Object obj) {
		return CastUtil.castBoolean(obj, false);
	}
	
	/**
	 * 转为int型(提供默认值)
	 */
	public static boolean castBoolean(Object obj, boolean defaultValue) {
		boolean booleanValue = defaultValue;
		if(obj != null) {
			booleanValue = Boolean.parseBoolean(castString(obj));
		}
		return booleanValue;
	}
}
