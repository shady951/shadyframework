package org.shady4j.framework;
/**
 * 提供相关配置常量
 * @author tc
 * @since		1.0.0
 */
public interface ConfigConstant {

	String CONFIG_FILE = "shady.properties";
	
	String JDBC_DRIVER = "shady.framework.jdbc.driver";
	String JDBC_URL = "shady.framework.jdbc.url";
	String JDBC_USERNAME = "shady.framework.jdbc.username";
	String JDBC_PASSWORD = "shady.framework.jdbc.password";
	
	String APP_BASE_PACKAGE = "shady.framework.app.base_package";
	String APP_JSP_PATH = "shady.framework.app.jsp_path";
	String APP_ASSET_PATH = "shady.framework.app.asset_path";
}
