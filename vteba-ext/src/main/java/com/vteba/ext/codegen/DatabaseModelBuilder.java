package com.vteba.ext.codegen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.vteba.utils.common.PropertiesLoader;
import com.vteba.utils.cryption.DESUtils;

public class DatabaseModelBuilder {
	
	public DatabaseModelBuilder(String rootPath, String tableName) {
		String propPath = rootPath + "src\\main\\resources\\jndi.properties";
		PropertiesLoader loader = new PropertiesLoader(propPath);
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
		
		String url = loader.getProperty("jdbc.skmbwurl");
		String user = loader.getProperty("jdbc.username");
		String password = loader.getProperty("jdbc.password");
		
		user = DESUtils.getDecryptString(user);
		password = DESUtils.getDecryptString(password);
		
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
			for (int i = 1; i <= columnCount; i++) {
				int types = metaData.getColumnType(i);
				String columnName = metaData.getColumnName(i);
				String columnClazzName = metaData.getColumnClassName(i);
				System.out.println(columnClazzName);
			}
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
		DatabaseModelBuilder builder = new DatabaseModelBuilder(path, "emp_user");
	}
}
