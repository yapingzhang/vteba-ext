package com.vteba.ext.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.vteba.utils.common.CaseUtils;

/**
 * 代码生成器builder。
 * @author yinlei
 * @date 2013-6-18
 */
public class CodeBuilder {
	private String rootPath;// 项目根路径
	private String schema;// 数据库schema
	private String catalog;// 数据库catalog
	private String className;// 要生成的实体类名
	private String tableDesc;// 实体注释
	private KeyType keyType;// 主键类型
	private String module;// 属于哪个模块
	private String tableName;// 表名
	
	private boolean genDao = true;
	private boolean genService = true;//（依赖dao）
	private boolean genAction = true;//（依赖service）
	private boolean genMapper = true;
	private boolean genModel = true;
	private boolean mongo = false;// 产生mongodb dao
	
	private boolean pojo = true;
	
	private DB db;// 数据库类型
	private String configFilePath;
	
	private VelocityEngine velocityEngine;
	
	/**
	 * 使用项目路径构造CodeBuilder。
	 * @param rootPath 项目根路径
	 * @param template 模板类型
	 */
	public CodeBuilder(String rootPath, TempType template) {
		this.rootPath = rootPath;
		//String templateBasePath = rootPath + "template";
		Properties properties = new Properties();
		//properties.setProperty(Velocity.RESOURCE_LOADER, "file");
		//properties.setProperty(Velocity.RESOURCE_LOADER, "classpath");
		
//		//设置velocity资源加载方式为jar
//		properties.setProperty(Velocity.RESOURCE_LOADER, "jar");
//		//设置velocity资源加载方式为file时的处理类
//        properties.setProperty("jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader");
//        //设置jar包所在的位置
//        properties.setProperty("jar.resource.loader.path", "jar:file:WEB-INF/lib/vteba-ext-1.0.1.jar");
        
        ProtectionDomain domain = getClass().getProtectionDomain();
		CodeSource codeSource = domain.getCodeSource();
		URL location = codeSource.getLocation();
		String path = location.getPath();
		//System.out.println(path);
		
//		URL url = getClass().getClassLoader().getResource("log4j.xml");
//		String aurl = url.getPath();
//		System.out.println(aurl);
//
//		URL pathUrl = getClass().getResource("");
//		System.out.println(pathUrl.getPath());
//		
//		URL pathUrl2 = getClass().getResource("/");
//		System.out.println(pathUrl2.getPath());
		
//        //设置velocity资源加载方式为class
//        properties.setProperty("resource.loader", "class");
//        //设置velocity资源加载方式为file时的处理类
//        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        
		//properties.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
		if (template == TempType.Generic) {
		    path = path + "/generictemp";
		} else if (template == TempType.Base) {
		    path = path + "/basetemplate";
		}
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
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
	
	/**
	 * 设置数据库catalog
	 * @param catalog 数据库catalog
	 * @return <b>this</b>
	 */
	public CodeBuilder setCatalog(String catalog) {
	    this.catalog = catalog;
		return this;
	}
	
	/**
     * 设置数据库schema
     * @param schema 数据库schema
     * @return <b>this</b>
     */
	public CodeBuilder schema(String schema) {
		this.schema = schema;
		return this;
	}
	
//	public String schema() {
//		return this.schema;
//	}
	
	/**
	 * 设置数据库表映射成的实体类文件名字
	 * @param className 实体类名
	 * @return <b>this</b>
	 */
	public CodeBuilder className(String className) {
		this.className = className;
		return this;
	}
	
//	public String className() {
//		return this.className;
//	}
	
	/**
	 * 设置表注释
	 * @param tableDesc 注释语句
	 * @return <b>this</b>
	 */
	public CodeBuilder tableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
		return this;
	}
	
//	public String tableDesc() {
//		return this.tableDesc;
//	}
	
	/**
	 * 设置数据库主键类型
	 * @param keyType 主键类型
	 * @return <b>this</b>
	 */
	public CodeBuilder keyType(KeyType keyType) {
		this.keyType = keyType;
		return this;
	}
	
//	public KeyType keyType() {
//		return this.keyType;
//	}
	
	/**
	 * 设置该模块的包路径，例如com.vteba.user / com.vteba.finance.account
	 * @param module 包路径
	 * @return <b>this</b>
	 */
	public CodeBuilder module(String module) {
		this.module = module;
		return this;
	}
	
//	public String module() {
//		return this.module;
//	}
	
	/**
	 * 设置数据库表名
	 * @param tableName 表名
	 * @return <b>this</b>
	 */
	public CodeBuilder tableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	/**
	 * 设置是否pojo，true是，false将产生JPA注解
	 * @param pojo true是，false否
	 * @return <b>this</b>
	 */
    public CodeBuilder setPojo(boolean pojo) {
        this.pojo = pojo;
        return this;
    }

    /**
     * 是否产生Dao文件
     * @param genModel true或false
     * @return <b>this</b>
     */
    public CodeBuilder setGenDao(boolean genDao) {
        this.genDao = genDao;
        return this;
    }
    
    /**
     * 是否产生Service文件，依赖于Dao
     * @param genModel true或false
     * @return <b>this</b>
     */
    public CodeBuilder setGenService(boolean genService) {
        if (!genDao) {
            throw new IllegalArgumentException("Service依赖于Dao，请生成Dao文件，调用setGenDao(true);");
        }
        this.genService = genService;
        return this;
    }
    
    /**
     * 是否产生Action文件，依赖于Service
     * @param genModel true或false
     * @return <b>this</b>
     */
    public CodeBuilder setGenAction(boolean genAction) {
        if (!genService) {
            throw new IllegalArgumentException("Action依赖于Servcie，请生成Service文件，调用setGenService(true);");
        }
        this.genAction = genAction;
        return this;
    }
    
    /**
     * 是否产生Mybatis Mapper文件
     * @param genModel true或false
     * @return <b>this</b>
     */
    public CodeBuilder setGenMapper(boolean genMapper) {
        this.genMapper = genMapper;
        return this;
    }
    
    /**
     * 是否产生mongo的dao文件
     * @param mongo true是
     */
    public CodeBuilder setMongo(boolean mongo) {
        this.mongo = mongo;
        return this;
    }

    /**
     * 是否产生模型文件
     * @param genModel true或false
     * @return <b>this</b>
     */
    public CodeBuilder setGenModel(boolean genModel) {
        this.genModel = genModel;
        return this;
    }

    /**
     * 设置数据库类型，支持mysql和oracle，PostgreSQL
     * @param db 数据库类型
     * @return <b>this</b>
     */
    public CodeBuilder setDb(DB db) {
        this.db = db;
        return this;
    }
    
    /**
     * 设置jdbc配置文件路径，和rootPath要构成一个完整的路径。
     * @param configFilePath 配置文件路径
     * @return <b>this</b>
     */
    public CodeBuilder setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
        return this;
    }
    
    /**
	 * 生成代码。
	 */
	public void build() {
		
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
		context.put("table", tableName);
		
		String smallClassName = StringUtils.uncapitalize(className);
		context.put("smallClassName", smallClassName);
		
		String packages = module;
		String pgk = packages.replace(".", "/") + "/";

		context.put("packages", packages);
		context.put("currentDate", DateFormat.getDateTimeInstance().format(new Date()));

		//String rootPath = "/home/yinlei/downloads/vteba/";
		//String rootPath = projectRootPath;
		
		String srcPath = "src/main/java/";
		
		//String parentPackagePath = "com/vteba/";
		String parentPackagePath = "";
		
		String actionTemplateName = "Action.java";
		String daoTemplateName = "Dao.java";
		String daoImplTemplateName = "DaoImpl.java";
		String serviceTemplateName = "Service.java";
		String serviceImplTemplateName = "ServiceImpl.java";
		String mapperTemplateName = "Mapper.java";
		String modelTemplateName = "Model.java";
		String mongoTemplateName = "MongoDao.java";
		
		//String classPath = parentPackagePath + pgk + "dao/mapper/" + className;
		
		String targetJavaFile = rootPath + srcPath + parentPackagePath + pgk;
		
		//*********************如果不想产生某种类型的文件，请注释掉**************************//
		if (genDao) {
			generateFile(context, daoTemplateName, targetJavaFile + "dao/spi/" + className);//dao接口
			generateFile(context, daoImplTemplateName, targetJavaFile + "dao/impl/" + className);//dao实现（不能单独产生）
			System.out.println("DAO文件产生完毕。");
		}
		if (mongo) {
            generateFile(context, mongoTemplateName, targetJavaFile + "dao/spi/" + className);//mongo dao接口
            System.out.println("Mongodb Dao文件产生完毕。");
        }
		if (genService) {
			generateFile(context, serviceTemplateName, targetJavaFile + "service/spi/" + className);//service接口
			generateFile(context, serviceImplTemplateName, targetJavaFile + "service/impl/" + className);//service实现（不能单独产生）
			System.out.println("Service文件产生完毕。");
		}
		if (genAction) {
			generateFile(context, actionTemplateName, targetJavaFile + "action/" + className);//action
			System.out.println("Action文件产生完毕。");
		}
		
		if (tableName != null && genModel) {
			DatabaseModelBuilder builder = new DatabaseModelBuilder("file:" + rootPath, configFilePath);
			builder.setDb(db).setTableName(tableName)
			.setPojo(pojo).setMongo(mongo)
			.setCatalog(catalog).setSchema(schema).buildParam(context);
			generateFile(context, modelTemplateName, targetJavaFile + "model/" + className);//model
			if (pojo) {
			    System.out.println("POJO Model文件产生完毕。");
			} else {
			    System.out.println("JPA Model文件产生完毕。可能需要调整关联关系和主键。");
			}
			if (mongo) {
                System.out.println("MongoDB Model文件产生完毕。");
            }
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
			System.out.println("Spring Jdbc Mapper文件产生完毕。");
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
