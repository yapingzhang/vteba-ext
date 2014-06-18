package com.vteba.ext.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.vteba.utils.common.CamelCaseUtils;

/**
 * 代码生成器builder。
 * @author yinlei
 * @date 2013-6-18
 */
public class CodeBuilder {
	private String rootPath;
	private String schema;
	private String className;
	private String tableDesc;
	private KeyType keyType;
	private String module;
	private String tableName;
	
	private VelocityEngine velocityEngine;
	
	/**
	 * 使用项目路径构造CodeBuilder。
	 * @param rootPath 项目根路径
	 */
	public CodeBuilder(String rootPath) {
		this.rootPath = rootPath;
		String templateBasePath = rootPath + "template";
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
	
//	public CodeBuilder rootPath(String rootPath) {
//		this.rootPath = rootPath;
//		return this;
//	}
//	
//	public String rootPath() {
//		return this.rootPath;
//	}
	
	public CodeBuilder schema(String schema) {
		this.schema = schema;
		return this;
	}
	
	public String schema() {
		return this.schema;
	}
	
	public CodeBuilder className(String className) {
		this.className = className;
		return this;
	}
	
	public String className() {
		return this.className;
	}
	
	public CodeBuilder tableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
		return this;
	}
	
	public String tableDesc() {
		return this.tableDesc;
	}
	
	public CodeBuilder keyType(KeyType keyType) {
		this.keyType = keyType;
		return this;
	}
	
	public KeyType keyType() {
		return this.keyType;
	}
	
	public CodeBuilder module(String module) {
		this.module = module;
		return this;
	}
	
	public String module() {
		return this.module;
	}
	
	public CodeBuilder tableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	/**
	 * 生成代码。
	 */
	public void build() {
		boolean genDao = true;
		boolean genService = true;//（依赖dao）
		boolean genAction = true;//（依赖service）
		boolean genMapper = true;
		
		if (schema == null) {
			System.err.println("schema为空，请调用schema(String schema)方法设置schema。");
			return;
		}
		if (className == null) {
			System.err.println("className为空，请调用className(String className)方法设置className。");
			return;
		}
		if (keyType == null) {
			System.err.println("keyType为空，请调用keyType(KeyType keyType)方法设置keyType。");
			return;
		}
		if (tableDesc == null) {
			System.err.println("tableDesc为空，请调用tableDesc(String tableDesc)方法设置tableDesc。");
			return;
		}
		if (module == null) {
			System.err.println("module为空，请调用module(String module)方法设置module。");
			return;
		}
		if (rootPath == null) {
			System.err.println("rootPath为空，调用构造函数时参数rootPath不能为null。");
		}
		
		VelocityContext context = new VelocityContext();
		context.put("schema", schema);
		context.put("className", className);
		context.put("tableName", tableDesc);
		context.put("pk", keyType.name());
		
		String smallClassName = StringUtils.uncapitalize(className);
		context.put("smallClassName", smallClassName);
		
		String packages = module;
		String pgk = packages.replace(".", "/") + "/";

		context.put("packages", packages);
		context.put("currentDate", DateFormat.getDateTimeInstance().format(new Date()));

		//String rootPath = "/home/yinlei/downloads/vteba/";
		//String rootPath = projectRootPath;
		
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
		
		if (tableName != null) {
			new DatabaseModelBuilder("file:" + rootPath).setTableName(tableName).buildParam(context);
			generateFile(context, modelTemplateName, targetJavaFile + "model/" + className);//model
		}
		
		
		//*************产生mybatis mapper*******************//
		
//		List<MethodBean> methodList = new ArrayList<MethodBean>();
//
//		String fullPack = "com.vteba." + module + ".model." + className;
//		Class<?> clazz = null;
//		try {
//			clazz = Class.forName(fullPack);
//		} catch (ClassNotFoundException e) {
//			System.err.println(e.getMessage());
//			return;
//		}
//
//		MethodAccess methodAccess = MethodAccess.get(clazz);
//		String[] methodNames = methodAccess.getMethodNames();
//		Class<?>[][] paramTypes = methodAccess.getParameterTypes();
//		int i = 0;
//		for (String methodName : methodNames) {
//			if (methodName.startsWith("set")) {
//				MethodBean methodBean = new MethodBean();
//				methodBean.setMethodName(methodName);
//				matchResultSet(methodBean, paramTypes[i][0]);
//				methodList.add(methodBean);
//			}
//			i++;
//		}
//		context.put("methodList", methodList);		
//		
//		if (genMapper) {
//			generateFile(context, mapperTemplateName, targetJavaFile + "dao/mapper/" + className);//mybatis mapper
//		}
		
		if (genMapper && tableName != null) {
			generateFile(context, mapperTemplateName, targetJavaFile + "dao/mapper/" + className);//mybatis mapper
		}
	}
	
	/**
	 * 产生java文件
	 * @param context VelocityContext
	 * @param templateName 模版文件名
	 * @param baseJavaFilePath 要产生的java文件路径
	 * @author yinlei
	 * date 2013-8-31 上午12:35:21
	 */
	public void generateFile(VelocityContext context, String templateName,
			String baseJavaFilePath) {
		try {
			File file = null;
			if (templateName.equals("Model.java")) {
				file = new File(baseJavaFilePath + ".java");
			} else {
				file = new File(baseJavaFilePath + templateName);
			}
			if (!file.exists()) {
				new File(file.getParent()).mkdirs();
			}

			Template template = velocityEngine.getTemplate(templateName, "UTF-8");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			template.merge(context, writer);
			template.process();
			writer.flush();
			writer.close();
			fos.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void matchResultSet(MethodBean methodBean, Class<?> clazz) {
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
