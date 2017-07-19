package org.shady4j.framework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回视图对象
 * @author tc
 * @since 1.0.0
 *
 */
public class View {

	/**
	 * 视图路径
	 */
	private String path;
	
	/**
	 * 模型数据
	 */
	private Map<String, Object> model;
	
	public View(String path) {
		this.path = path;
		model = new HashMap<String, Object>();
	}
	
	public String getPath() {
		return path;
	}
	
	public View addModel(String key, Object obj) {
		model.put(key, obj);
		return this;
	}
	
	public Map<String, Object> getModel() {
		return model;
	}
}
