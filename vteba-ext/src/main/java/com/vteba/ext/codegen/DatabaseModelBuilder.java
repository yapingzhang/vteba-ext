package com.vteba.ext.codegen;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private DB db;
	private boolean mongo = false;
	private String schema;
	private String catalog;
	private boolean genKey;
	private List<String> keyList;
	
	public DatabaseModelBuilder(String rootPath, String configFilePath) {
		
		String propPath = rootPath + configFilePath;
        loader = new PropertiesLoader(propPath);
        try {
            if (db == DB.MySQL) {
                Class.forName("com.mysql.jdbc.Driver");
            } else if (db == DB.Oracle) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } else if (db == DB.PostgreSQL) {
                Class.forName("org.postgresql.Driver");
            }
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        
        url = loader.getProperty("jdbc.url");
        user = loader.getProperty("jdbc.username");
        password = loader.getProperty("jdbc.password");
        
        //user = DESUtils.getDecrypt(user);
        //password = DESUtils.getDecrypt(password);
	}
	
	public List<String> getKeyList() {
		return keyList;
	}

	public DatabaseModelBuilder setGenKey(boolean genKey) {
		this.genKey = genKey;
		return this;
	}

	public DatabaseModelBuilder setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	public DatabaseModelBuilder setDb(DB db) {
	    this.db = db;
	    return this;
	}
	
	public DatabaseModelBuilder setMongo(boolean mongo) {
	    this.mongo = mongo;
	    return this;
	}
	
	public DatabaseModelBuilder setPojo(boolean pojo) {
		this.pojo = pojo;
		return this;
	}
	
	public DatabaseModelBuilder setSchema(String schema) {
        this.schema = schema;
        return this;
    }
	
	public DatabaseModelBuilder setCatalog(String catalog) {
        this.catalog = catalog;
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
		
		String sql = "select * from ";
		if (catalog != null) {
		    sql += catalog + ".";
		}
		if (schema != null) {
		    sql += schema + ".";
		}
		
		sql += tableName + " where 1 = 2";
		
		try {
		    DatabaseMetaData dsmetaData = conn.getMetaData();
		    ResultSet keyResultSet = dsmetaData.getPrimaryKeys(catalog, schema, tableName);
		    List<String> keyList = getPrimaryKey(keyResultSet);
		    if (genKey) {
		    	this.keyList = keyList;
		    	return;
		    }
		    //dsmetaData.getColumns(null, null, tableName, null);
		    
		    ResultSet indexResultSet = dsmetaData.getIndexInfo(catalog, schema, tableName, false, true);
		    Set<String> indexInfoSet = getIndexInfo(indexResultSet);
		    
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			//头部导入
			Set<String> importSets = new HashSet<String>();
			//定义的filed
			List<String> fieldList = new ArrayList<String>();
			//getter setter
			List<MethodPart> getsetMethodList = new ArrayList<MethodPart>();
			
			List<MethodBean> methodList = new ArrayList<MethodBean>();
			
			List<String> annotationList = new ArrayList<String>();
			
			List<MethodBean> getBeanList = new ArrayList<MethodBean>();
			
			if (mongo) {
                annotationList.add("@Document");
                importSets.add("import org.springframework.data.mongodb.core.mapping.Document;");
            }
			
			if (!pojo) {
                importSets.add("import javax.persistence.Column;");
            }
			
			for (int i = 1; i <= columnCount; i++) {
				//int types = metaData.getColumnType(i);
				String columnName = metaData.getColumnName(i);
				String columnClazzName = metaData.getColumnClassName(i);
				
				if (!columnClazzName.startsWith("java.lang.")) {
				    if (columnClazzName.equals("java.sql.Timestamp")) {
				        columnClazzName = "java.util.Date";
				    }
				    importSets.add("import " + columnClazzName + ";");
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
				    boolean nullable = metaData.isNullable(i) == 1 ? true : false;
				    int length = metaData.getColumnDisplaySize(i);
				    int precision = metaData.getPrecision(i);
				    int scale = metaData.getScale(i);
				    
				    if (keyList.contains(columnName)) {// 是否主键
				        if (keyList.size() != 1) {
				            methodPart.addAnnotation("//联合主键，或者没有主键，请自行设定");
				        } else {
				            methodPart.addAnnotation("@Id");
				            methodPart.addAnnotation("\t@Column(name = \"" + columnName + "\", unique = true, nullable = false, length = " + length + ")");
				            importSets.add("import javax.persistence.Id;");
				        }
				    } else {
				        StringBuilder sb = new StringBuilder("@Column(name = \"");
				        sb.append(columnName).append("\"");
				        
				        if (!nullable) {
				            sb.append(", nullable = false");
				        }
				        
				        if (indexInfoSet.contains(columnName)) {// 唯一索引
				            sb.append(", unique = true");
				        }
				        
				        if (fieldType.equals("String")) {
				            sb.append(", length = ").append(length).append(")");
	                        methodPart.addAnnotation(sb.toString());
	                    } else if (fieldType.equals("Date")) {
	                        methodPart.addAnnotation("@Temporal(TemporalType.DATE)");
	                        sb.append(", length = ").append(length).append(")");
	                        methodPart.addAnnotation("\t" + sb.toString());
	                        
	                        importSets.add("import javax.persistence.Temporal;");
	                        importSets.add("import javax.persistence.TemporalType;");
	                        
	                    } else if (fieldType.equals("Double") || fieldType.equals("BigDecimal")) {
	                        sb.append(", precision = ").append(precision).append(", scale = ").append(scale).append(")");
	                        methodPart.addAnnotation(sb.toString());
	                    } else if (fieldType.equals("Long") || fieldType.equals("Integer")) {
	                        sb.append(", length = ").append(length).append(")");
	                        methodPart.addAnnotation(sb.toString());
	                    } else {
	                        sb.append(")");
                            methodPart.addAnnotation(sb.toString());
	                    }
				    }
				}
				
				getsetMethodList.add(methodPart);
				
				MethodBean methodBean = new MethodBean();
				methodBean.setMethodName("set" + capFieldName);
				match(methodBean, columnClazzName, columnName);
				methodList.add(methodBean);
				
				MethodBean getBean = new MethodBean();
				getBean.setMethodName("get" + capFieldName + "()");
				getBean.setColumnName(columnName);
				getBeanList.add(getBean);
			}
			velocityContext.put("importList", importSets);
			velocityContext.put("fieldList", fieldList);
			velocityContext.put("getsetMethodList", getsetMethodList);
			velocityContext.put("methodList", methodList);
			velocityContext.put("getBeanList", getBeanList);
			velocityContext.put("annotationList", annotationList);
			velocityContext.put("pojo", pojo);
			velocityContext.put("id", CaseUtils.toCapCamelCase(keyList.get(0)));
			velocityContext.put("idColumn", keyList.get(0));
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
	
	public List<String> getPrimaryKey(ResultSet rs) {
	    List<String> keyList = new ArrayList<String>();
	    String key = null;
	    try {
            for (;rs.next();) {
//                System.out.print(rs.getString("TABLE_CAT"));
//                System.out.print(rs.getString("TABLE_SCHEM"));
//                System.out.print(rs.getString("TABLE_NAME"));
//                System.out.print(rs.getString("PK_NAME"));
//                System.out.print(rs.getShort("KEY_SEQ"));
                key = rs.getString("COLUMN_NAME");
                if (key != null) {
                    keyList.add(key);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
	    return keyList;
	}
	
	public Set<String> getIndexInfo(ResultSet rs) {
        Set<String> sets = new HashSet<String>();
        String columnName = null;
        try {
            for (;rs.next();) {
                boolean nonUnique = rs.getBoolean("NON_UNIQUE");
                if (!nonUnique) {
                    columnName = rs.getString("COLUMN_NAME");
                    if (columnName != null) {
                        sets.add(columnName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sets;
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
