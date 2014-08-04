package com.vteba.ext.codegen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import com.vteba.ext.codegen.model.MethodPart;
import com.vteba.utils.common.CaseUtils;
import com.vteba.utils.common.PropertiesLoader;

public class DatabaseModelBuilder {
	private PropertiesLoader loader;
	private String url;
	private String user;
	private String password;
	private String tableName;
	private boolean pojo = true;
	
	public DatabaseModelBuilder(String rootPath) {
		String propPath = rootPath + "src\\main\\resources\\config.properties";
		loader = new PropertiesLoader(propPath);
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
		
		url = loader.getProperty("jdbc.url");
		user = loader.getProperty("jdbc.username");
		password = loader.getProperty("jdbc.password");
		
		//user = DESUtils.getDecrypt(user);
		//password = DESUtils.getDecrypt(password);
		
	}
	
	public DatabaseModelBuilder setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	public DatabaseModelBuilder setPojo(boolean pojo) {
		this.pojo = pojo;
		return this;
	}
	
	public void buildParam(VelocityContext velocityContext) {
		if (tableName == null) {
			System.err.println("tableName不能为空，请设置。");
			return;
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		String sql = "select * from " + tableName + " where 1 = 2";
		
		try {
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			//头部导入
			List<String> importList = new ArrayList<String>();
			//定义的filed
			List<String> fieldList = new ArrayList<String>();
			//getter setter
			List<MethodPart> getsetMethodList = new ArrayList<>();
			
			List<MethodBean> methodList = new ArrayList<>();
			
			for (int i = 1; i <= columnCount; i++) {
				//int types = metaData.getColumnType(i);
				String columnName = metaData.getColumnName(i);
				String columnClazzName = metaData.getColumnClassName(i);
				
				if (!columnClazzName.startsWith("java.lang.")) {
					importList.add("import " + columnClazzName + ";");
				}
				
				String fieldType = columnClazzName.substring(columnClazzName.lastIndexOf(".") + 1);
				
				String fieldName = CaseUtils.toCamelCase(columnName);
				fieldList.add("private " + fieldType + " " + fieldName + ";");
				
				MethodPart methodPart = new MethodPart();
				methodPart.setFieldType(fieldType);
				String capFieldName = StringUtils.capitalize(fieldName);
				methodPart.setMethodName(capFieldName);
				methodPart.setMethodParam(fieldName);
				
				if (!pojo) {// 如果不是pojo，那么使用jpa的注解
					methodPart.setAnnotation("");
				}
				
				getsetMethodList.add(methodPart);
				
				MethodBean methodBean = new MethodBean();
				methodBean.setMethodName("set" + capFieldName);
				match(methodBean, columnClazzName, columnName);
				methodList.add(methodBean);
			}
			velocityContext.put("importList", importList);
			velocityContext.put("fieldList", fieldList);
			velocityContext.put("getsetMethodList", getsetMethodList);
			velocityContext.put("methodList", methodList);
			rs.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void match(MethodBean methodBean, String type, String field) {
		if (type.equals("java.lang.Integer") || type.equals("int")) {
			methodBean.setRsName("getInt(\"" + field + "\")");
		} else if (type.equals("java.lang.Long") || type.equals("long")) {
			methodBean.setRsName("getLong(\"" + field + "\")");
		} else if (type.equals("java.lang.String")) {
			methodBean.setRsName("getString(\"" + field + "\")");
		} else if (type.equals("java.lang.Double") || type.equals("double")) {
			methodBean.setRsName("getDouble(\"" + field + "\")");
		} else if (type.equals("java.lang.Timestamp")) {
			methodBean.setRsName("getTimestamp(\"" + field + "\")");
		} else if (type.equals("java.util.Date") || type.equals("java.sql.Date")) {
			methodBean.setRsName("getDate(\"" + field + "\")");
		} else if (type.equals("java.sql.Timestamp")) {
			methodBean.setRsName("getTimestamp(\"" + field + "\")");
		} else if (type.equals("java.lang.Boolean") || type.equals("boolean")) {
			methodBean.setRsName("getBoolean(\"" + field + "\")");
		} else if (type.equals("java.math.BigInteger")) {
			methodBean.setRsName("getBigDecimal(\"" + field + "\").toBigInteger()");
		} else if (type.equals("java.math.BigDecimal")) {
			methodBean.setRsName("getBigDecimal(\"" + field + "\")");
		} else if (type.equals("java.lang.Float") || type.equals("float")) {
			methodBean.setRsName("getFloat(\"" + field + "\")");
		} else if (type.equals("java.lang.Short") || type.equals("short")) {
			methodBean.setRsName("getShort(\"" + field + "\")");
		} else if (type.equals("java.lang.Byte") || type.equals("byte")) {
			methodBean.setRsName("getByte(\"" + field + "\")");
		}
	}
	
	public static void main(String[] args) {
		System.out.println(int.class.getName());
		//String path = "file:C:\\Users\\zy\\git\\vteba-ext\\vteba-ext\\";//
		//new DatabaseModelBuilder(path).setTableName("emp_user");
	}
}
