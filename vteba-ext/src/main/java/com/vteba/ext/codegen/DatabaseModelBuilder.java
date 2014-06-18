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
import com.vteba.utils.common.CamelCaseUtils;
import com.vteba.utils.common.PropertiesLoader;
import com.vteba.utils.cryption.DESUtils;

public class DatabaseModelBuilder {
	private PropertiesLoader loader;
	private String url;
	private String user;
	private String password;
	private String tableName;
	
	public DatabaseModelBuilder(String rootPath) {
		String propPath = rootPath + "src\\main\\resources\\jndi.properties";
		loader = new PropertiesLoader(propPath);
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
		
		url = loader.getProperty("jdbc.skmbwurl");
		user = loader.getProperty("jdbc.username");
		password = loader.getProperty("jdbc.password");
		
		user = DESUtils.getDecryptString(user);
		password = DESUtils.getDecryptString(password);
		
	}
	
	public DatabaseModelBuilder setTableName(String tableName) {
		this.tableName = tableName;
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
			
			for (int i = 1; i <= columnCount; i++) {
				//int types = metaData.getColumnType(i);
				String columnName = metaData.getColumnName(i);
				String columnClazzName = metaData.getColumnClassName(i);
				
				if (!columnClazzName.startsWith("java.lang.")) {
					importList.add("import " + columnClazzName + ";");
				}
				
				String fieldType = columnClazzName.substring(columnClazzName.lastIndexOf(".") + 1);
				
				String fieldName = CamelCaseUtils.toCamelCase(columnName);
				fieldList.add("private " + fieldType + " " + fieldName + ";");
				
				MethodPart methodPart = new MethodPart();
				methodPart.setFieldType(fieldType);
				methodPart.setMethodName(StringUtils.capitalize(fieldName));
				methodPart.setMethodParam(fieldName);
				getsetMethodList.add(methodPart);
			}
			velocityContext.put("importList", importList);
			velocityContext.put("fieldList", fieldList);
			velocityContext.put("getsetMethodList", getsetMethodList);
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
	
	public static void main(String[] args) {
		String path = "file:C:\\Users\\zy\\git\\vteba-ext\\vteba-ext\\";//
		new DatabaseModelBuilder(path).setTableName("emp_user");
	}
}
