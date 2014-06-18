package com.vteba.ext.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.vteba.lang.bytecode.MethodAccess;
import com.vteba.utils.common.CamelCaseUtils;

/**
 * 产生Dao和Service的代码引擎。
 * @author yinlei
 * date 2013-8-31 上午12:38:09
 */
public class CodeGenerator {
	static VelocityEngine velocityEngine;
	static {
		String templateBasePath = "D:\\git\\vteba\\vteba\\template";
		Properties properties = new Properties();
		properties.setProperty(Velocity.RESOURCE_LOADER, "file");
		properties.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templateBasePath);
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, "true");
		properties.setProperty("file.resource.loader.modificationCheckInterval", "30");
		properties.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
		properties.setProperty("runtime.log.logsystem.log4j.logger", "org.apache.velocity");
		properties.setProperty("directive.set.null.allowed", "true");
		velocityEngine = new VelocityEngine();
		velocityEngine.init(properties);
	}
	
	/**
	 * @param args
	 * @author yinlei
	 * date 2013-8-30 下午11:52:37
	 */
	public static void main(String[] args) {
		if (args == null || args.length != 5) {
			return;
		}
//		String schema = "skmbw";//数据库schema
//		String className = "Category";//实体类，类名
//		String tableName = "商品分类";//实体类对应的表的注释名
//		String pk = "Long";//String Long Integer，主键类型
//		String module = "home.index";//模块名
		
		String schema = args[0];//数据库schema
		String className = args[1];//实体类，类名
		String tableName = args[2];//实体类对应的表的注释名
		String pk = args[3];//String Long Integer，主键类型
		String module = args[4];//模块名
		
		String fullPack = "com.vteba." + module + ".model." + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullPack);
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			return;
		}
		
		VelocityContext context = new VelocityContext();
		context.put("schema", schema);
		context.put("className", className);
		context.put("tableName", tableName);
		context.put("pk", pk);
		
		String smallClassName = StringUtils.uncapitalize(className);
		context.put("smallClassName", smallClassName);
		
		String packages = module;
		String pgk = packages.replace(".", "/") + "/";

		context.put("packages", packages);
		context.put("currentDate", DateFormat.getDateTimeInstance().format(new Date()));

		//String rootPath = "/home/yinlei/downloads/vteba/";
		String rootPath = "D:\\git\\vteba\\vteba\\";
		
		String srcPath = "src/main/java/";
		
		String parentPackagePath = "com/vteba/";
		
//		String actionTemplateName = "Action.java";
//		String daoTemplateName = "Dao.java";
//		String daoImplTemplateName = "DaoImpl.java";
//		String serviceTemplateName = "Service.java";
//		String serviceImplTemplateName = "ServiceImpl.java";
		String mapperTemplateName = "Mapper.java";
		
		//String classPath = parentPackagePath + pgk + "dao/mapper/" + className;
		
		String targetJavaFile = rootPath + srcPath + parentPackagePath + pgk;
		
		List<MethodBean> methodList = new ArrayList<MethodBean>();
		
		MethodAccess methodAccess = MethodAccess.get(clazz);
		String[] methodNames = methodAccess.getMethodNames();
		Class<?>[][] paramTypes = methodAccess.getParameterTypes();
		int i = 0;
		for (String methodName : methodNames) {
			if (methodName.startsWith("set")) {
				MethodBean methodBean = new MethodBean();
				methodBean.setMethodName(methodName);
				matchResultSet(methodBean, paramTypes[i][0]);
				methodList.add(methodBean);
			}
			i++;
		}
		context.put("methodList", methodList);
		
		//generateFile(context, actionTemplateName, targetJavaFile + "action/" + className);
		//generateFile(context, daoTemplateName, targetJavaFile + "dao/spi/" + className);
		//generateFile(context, daoImplTemplateName, targetJavaFile + "dao/impl/" + className);
		//generateFile(context, serviceTemplateName, targetJavaFile + "service/spi/" + className);
		//generateFile(context, serviceImplTemplateName, targetJavaFile + "service/impl/" + className);
		
		generateFile(context, mapperTemplateName, targetJavaFile + "dao/mapper/" + className);
	}

	/**
	 * 产生java文件
	 * @param context VelocityContext
	 * @param templateName 模版文件名
	 * @param baseJavaFilePath 要产生的java文件路径
	 * @author yinlei
	 * date 2013-8-31 上午12:35:21
	 */
	public static void generateFile(VelocityContext context, String templateName,
			String baseJavaFilePath) {
		try {
			File file = new File(baseJavaFilePath + templateName);
			if (!file.exists()) {
				new File(file.getParent()).mkdirs();
			} else {
				
			}

			Template template = velocityEngine.getTemplate(templateName, "UTF-8");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			template.merge(context, writer);
			writer.flush();
			writer.close();
			fos.close();
		} catch (Exception e) {
			
		}
	}
	
	public static void matchResultSet(MethodBean methodBean, Class<?> clazz) {
		String underLine = CamelCaseUtils.toUnderScoreCase(methodBean.getMethodName().substring(3));
		if (clazz == Integer.class || clazz == Integer.TYPE) {
			methodBean.setRsName("getInt(\"" + underLine + "\")");
		} else if (clazz == Long.class || clazz == Long.TYPE) {
			methodBean.setRsName("getLong(\"" + underLine + "\")");
		} else if (clazz == String.class) {
			methodBean.setRsName("getString(\"" + underLine + "\")");
		} else if (clazz == Double.class || clazz == Double.TYPE) {
			methodBean.setRsName("getDouble(\"" + underLine + "\")");
		} else if (clazz == Timestamp.class) {
			methodBean.setRsName("getTimestamp(\"" + underLine + "\")");
		} else if (clazz == Date.class || clazz == java.sql.Date.class) {
			methodBean.setRsName("getDate(\"" + underLine + "\")");
		} else if (clazz == Boolean.class || clazz == Boolean.TYPE) {
			methodBean.setRsName("getBoolean(\"" + underLine + "\")");
		} else if (clazz == BigInteger.class) {
			methodBean.setRsName("getBigDecimal(\"" + underLine + "\").toBigInteger()");
		} else if (clazz == BigDecimal.class) {
			methodBean.setRsName("getBigDecimal(\"" + underLine + "\")");
		} else if (clazz == Float.class || clazz == Float.TYPE) {
			methodBean.setRsName("getFloat(\"" + underLine + "\")");
		} else if (clazz == Short.class || clazz == Short.TYPE) {
			methodBean.setRsName("getShort(\"" + underLine + "\")");
		} else if (clazz == Byte.class || clazz == Byte.TYPE) {
			methodBean.setRsName("getByte(\"" + underLine + "\")");
		}
	}
}
