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
import com.vteba.utils.common.CaseUtils;

/**
 * 产生Dao和Service的代码引擎。
 * @author yinlei
 * date 2013-8-31 上午12:38:09
 */
public class CodeGenerator {
    static VelocityEngine velocityEngine;
	
	//********项目的跟路径，这个参数需要调整**********//
	static String projectRootPath = "C:\\Users\\zy\\git\\vteba\\vteba\\";
	//********项目的跟路径，这个参数需要调整**********//
	
	static {
		String templateBasePath = projectRootPath + "template";
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
		//**************调整这五个参数的值 start**************//
		
		String schema = "skmbw";//数据库schema
		String className = "Gaga";//实体类，类名
		String tableName = "商品分类";//实体类对应的表的注释名
		String pk = "Long";//String Long Integer，主键类型
		String module = "home.gaga";//模块名（包名的一部分）
		
		boolean genDao = true;
		boolean genService = true;//（依赖dao）
		boolean genAction = true;//（依赖service）
		boolean genMapper = true;
		//**************调整这五个参数的值 end**************//
		
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
		String rootPath = projectRootPath;
		
		String srcPath = "src/main/java/";
		
		String parentPackagePath = "com/vteba/";
		
		String actionTemplateName = "Action.java";
		String daoTemplateName = "Dao.java";
		String daoImplTemplateName = "DaoImpl.java";
		String serviceTemplateName = "Service.java";
		String serviceImplTemplateName = "ServiceImpl.java";
		String mapperTemplateName = "Mapper.java";
		String modelTemplateName = "Model.java";
		
		//String classPath = parentPackagePath + pgk + "dao/mapper/" + className;
		
		String targetJavaFile = rootPath + srcPath + parentPackagePath + pgk;
		
		//*********************如果不想产生某种类型的文件，请注释掉**************************//
		if (genDao) {
			generateFile(context, daoTemplateName, targetJavaFile + "dao/spi/" + className);//dao接口
			generateFile(context, daoImplTemplateName, targetJavaFile + "dao/impl/" + className);//dao实现（不能单独产生）
		}
		if (genService) {
			generateFile(context, serviceTemplateName, targetJavaFile + "service/spi/" + className);//service接口
			generateFile(context, serviceImplTemplateName, targetJavaFile + "service/impl/" + className);//service实现（不能单独产生）
		}
		if (genAction) {
			generateFile(context, actionTemplateName, targetJavaFile + "action/" + className);//action
		}
		
		generateFile(context, modelTemplateName, targetJavaFile + "model/" + className);//model
		
		
		//*************产生mybatis mapper*******************//
		
		List<MethodBean> methodList = new ArrayList<MethodBean>();
		
		String fullPack = "com.vteba." + module + ".model." + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullPack);
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			return;
		}
		
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
		
		if (genMapper) {
			generateFile(context, mapperTemplateName, targetJavaFile + "dao/mapper/" + className);//mybatis mapper
		}
		//*********************如果不想产生某种类型的文件，请注释掉**************************//
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
			File file = null;
			if (templateName.equals("Model.java")) {
				file = new File(baseJavaFilePath + ".java");
			}
			file = new File(baseJavaFilePath + templateName);
			if (!file.exists()) {
				new File(file.getParent()).mkdirs();
			}
			
			Template template = velocityEngine.getTemplate(templateName, "UTF-8");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			template.merge(context, writer);
			writer.flush();
			writer.close();
			fos.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void matchResultSet(MethodBean methodBean, Class<?> clazz) {
		String underLine = CaseUtils.underCase(methodBean.getMethodName().substring(3));
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
