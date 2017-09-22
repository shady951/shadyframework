package org.shady4j.framework.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.shady4j.framework.util.CastUtil;
import org.shady4j.framework.util.CollectionUtil;
import org.shady4j.framework.util.StringUtil;

/**
 * 请求参数对象
 * @author tc
 * @since 1.0.0
 *
 */
public class Param {
	
	private List<FormParam> formParamList;
	private List<FileParam> fileParamList;
	
	public Param(List<FormParam> formParamList) {
		this.formParamList = formParamList;
	}

	public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
		this.formParamList = formParamList;
		this.fileParamList = fileParamList;
	}
	
	/**
	 *	获取表单参数映射 
	 */
	public Map<String, Object> getFieldMap() {
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		if(CollectionUtil.isNotEmpty(formParamList)) {
			for(FormParam formParam : formParamList) {
				String fieldName = formParam.getFieldName();
				Object fieldValue = formParam.getFieldValue();
				if(fieldMap.containsKey(fieldName)) {
					fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
				}
				fieldMap.put(fieldName, fieldValue);
			} 
		}
		return fieldMap;
	}
	
	/**
	 * 获取上传文件映射
	 */
	public Map<String, List<FileParam>> getFileMap() {
		Map<String, List<FileParam>> fileMap = new HashMap<String, List<FileParam>>();
		if(CollectionUtil.isNotEmpty(fileParamList)) {
			for(FileParam fileparam : fileParamList) { //稍作优化
				String fieldName = fileparam.getFieldName();
				if(!fileMap.containsKey(fieldName)) {
					fileMap.put(fieldName, new ArrayList<FileParam>());
				}
				fileMap.get(fieldName).add(fileparam);
			}
		}
		return fileMap;
	}
	
	/**
	 *	获取所有上传文件
	 */
	public List<FileParam> getFileList(String fieldName) {
		return getFileMap().get(fieldName);
	}
	
	/**
	 *	获取唯一上传文件
	 *	@return 若列表文件数为1，则返回该文件，否则返回null 
	 */
	public FileParam getFile(String fieldName)	{
		List<FileParam> fileParamList = getFileList(fieldName);
		if(CollectionUtil.isNotEmpty(fileParamList) && fileParamList.size() == 1) {
			return fileParamList.get(0);
		}
		return null;
	}
	
	/**
	 * 验证参数是否为空
	 * @return 当文件列表和表单列表均为空时，返回true，否则返回flase
	 */
	public boolean isEmpty() {
		return CollectionUtil.isEmpty(formParamList) && CollectionUtil.isEmpty(fileParamList);
	}
	
	/**
	 *	根据参数名获取String型表单参数
	 */
	public String getString(String name) {
		return CastUtil.castString(getFieldMap().get(name));
	}
	
	/**
	 *	根据参数名获取double型表单参数
	 */
	public double getDouble(String name) {
		return CastUtil.castDouble(getFieldMap().get(name));
	}
	
	/**
	 *	根据参数名获取long型表单参数
	 */
	public long getLong(String name) {
		return CastUtil.castLong(getFieldMap().get(name));
	}
	
	/**
	 *	根据参数名获取int型表单参数
	 */
	public int getInt(String name) {
		return CastUtil.castInt(getFieldMap().get(name));
	}
	
	/**
	 *	根据参数名获取boolean型表单参数
	 */
	public boolean getboolean(String name) {
		return CastUtil.castBoolean(getFieldMap().get(name));
	}
	
}
