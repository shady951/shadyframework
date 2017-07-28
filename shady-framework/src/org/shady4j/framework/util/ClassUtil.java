package org.shady4j.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类操作工具类
 * @author tc
 * @since 1.0.0
 *3
 */
public final class ClassUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);
	
	/**
	 * 获取类加载器
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader(); //ques:与class.getClassLoader()的区别
	}
	
	/**
	 *加载类 
	 */
	public static Class<?> loadClass(String className, boolean isInitialized) {
		Class<?> clazz;
		try {
			//isInitialized可设置为false以提高加载类的性能
			clazz = Class.forName(className, isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			LOGGER.error("load class failure!!", e);
			throw new RuntimeException(e);
		}
		return clazz;
	}
	
	/**
	 * 获取指定包名下的所有类
	 */
	public static Set<Class<?>> getClassSet(String packageName) {
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		try {
			//获得项目中符合packageName路径的所有绝对url
			Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
			while(urls.hasMoreElements()) {
				URL url = urls.nextElement();
				//<
				System.out.println(url);
				//>
				if(url != null) {
					String protocol = url.getProtocol();
					if(protocol.equals("file")) {
						/*
						 * URL类本身不会根据RFC2396中定义的转义机制对任何URL组件进行编码或解码。
						 * 调用者有责任编码任何需要在调用URL之前进行转义的字段，并对从URL返回的任何转义字段进行解码。
						 * 此外，由于URL不具有URL自动转义的功能， 因此不能识别同一URL的编码或解码形式之间的等同性。
						 * 来自google翻译，详见java.net.URL类注释。
						 */
						//获取绝对路径
						String packagePath = url.getPath().replaceAll("%20", " "); //ques:既然没使用正则，为什么不用replace() 
						addClass(classSet, packagePath, packageName);
					} else if (protocol.equals("jar")) {
						JarURLConnection juc = (JarURLConnection )url.openConnection();
						if(juc != null) {
							JarFile jarFile = juc.getJarFile();
							if(jarFile != null) {
								Enumeration<JarEntry> jarEntries = jarFile.entries(); //// 枚举获得JAR文件内的实体,即相对路径
								while(jarEntries.hasMoreElements()) {
									JarEntry jarEntry = jarEntries.nextElement();
									String jarEntryName = jarEntry.getName();
									if(jarEntryName.endsWith("class")) {
										String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
										doAddClass(classSet, className);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("get class set failure!!", e);
			throw new RuntimeException(e);
		}
		return classSet;
	}

	private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
		//通过过滤得到class文件和目录
		//ques:为何不用相对路径查找资源，我想可能是该项目本身是框架，相对路径找不到项目资源
		//此类下的new File("a.txt").getAbsolutePath();输出为 G:\Data\Workspaces\MyEclipse 2015 CI\shady-framework\a.txt
		File[] files = new File(packagePath).listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getName().endsWith("class") || pathname.isDirectory();
			}
		}); 
		if(ArrayUtil.isNotEmpty(files)) { 
           	for(File file : files) {
				String fileName = file.getName();
				if(file.isFile()) {
					String className = fileName.substring(0, fileName.lastIndexOf("."));
					if(StringUtil.isNotEmpty(packageName)) {
						className = packageName + "." + className;
					}
					doAddClass(classSet, className);
				} else {
					 String subPackageName = fileName;
					 if(StringUtil.isNotEmpty(packageName)) {
						 subPackageName = packageName + "." + fileName;
					 }
					 String subPackagePath = fileName;
					 if(StringUtil.isNotEmpty(packagePath)) { //这个if判断，个人认为没有必要
						 subPackagePath = packagePath + "/" + fileName;
					 }
					 addClass(classSet, subPackagePath, subPackageName);
				}
			}
		}
	}

	private static void doAddClass(Set<Class<?>> classSet, String className) {
		Class<?> clazz = loadClass(className, false);
		classSet.add(clazz);
	}
	
//	public static void main(String[] args) {
//		try {
//			Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("org/shady4j/framework/util/ClassUtil.class");
//			while(urls.hasMoreElements()) {
//				URL url = urls.nextElement();
//				System.out.println(url.getPath());
//				System.out.println(url.toString());
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}
