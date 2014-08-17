package ${packages}.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.vteba.tx.jdbc.spring.SpringJdbcTemplate;
import com.vteba.tx.jdbc.spring.impl.SpringGenericDaoImpl;
import com.vteba.tx.jdbc.spring.impl.SqlType;
import ${packages}.dao.spi.${className}SpringDao;
import ${packages}.model.${className};

/**
 * ${tableName}Spring Dao实现。
 * @author yinlei
 * date ${currentDate}
 */
@Named
public class ${className}SpringDaoImpl extends SpringGenericDaoImpl<${className}, ${pk}> implements ${className}SpringDao {

    public ${className}SpringDaoImpl() {
        super();
    }

    public ${className}SpringDaoImpl(Class<${className}> entityClass) {
        super(entityClass);
    }

	@Inject
	public void setSpringJdbcTemplate(SpringJdbcTemplate ${schema}JdbcTemplate) {
		this.springJdbcTemplate = ${schema}JdbcTemplate;
	}

    @Override
    public Object mapRows(ResultSet rs, String sql, Class<?> clazz) throws SQLException {
        if (clazz == ${className}.class) {
            return mapRows(rs, 0);
        }
        return null;
    }
    
	@Override
	public ${className} mapRows(ResultSet rs, int rowNum) throws SQLException {
        ${className} ${smallClassName} = new ${className}();
    #foreach($c in $methodList)
	${smallClassName}.${c.methodName}(rs.${c.rsName});
    #end
	return ${smallClassName};
	}

    @Override
    public Map<String, Object> mapBean(${className} entity, SqlType sqlType) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        StringBuilder columns = new StringBuilder();
        boolean append = true;
        
        if (entity != null) {
            switch (sqlType) {
                case INSERT:// 对于insert语句根本不需要考虑，是否要前缀
                    columns.append("insert into ").append(tableName).append("(");
                    
                    StringBuilder holders = new StringBuilder(") values(");
                    #foreach($c in $getBeanList)
if (entity.${c.methodName} != null) {
                        resultMap.put("${c.columnName}", entity.${c.methodName});
                        columns.append("${c.columnName},");
                        holders.append(":${c.columnName},");
                    }
                    #end
                    
                    columns.append(holders).append(")");
                    resultMap.put(SQL_KEY, columns.toString());
                    break;
                case SELECT:
                    columns.append("select * from ").append(tableName);
                    
                    buildWhere(entity, resultMap, columns, append);
                    break;
                case DELETE:
                    columns.append("delete from ").append(tableName);
                    
                    buildWhere(entity, resultMap, columns, append);
                    break;
                case UPDATE:
                    columns.append("update ").append(tableName);
                    #foreach($c in $getBeanList)
if (entity.${c.methodName} != null) {
                    	resultMap.put("${c.columnName}", entity.${c.methodName});
                    	#if($velocityCount == 1)
columns.append(" set ${c.columnName} = :${c.columnName}");
                        append = false;
                    #else
if (append) {
                    		columns.append(" set ${c.columnName} = :${c.columnName}");
                            append = false;
                        } else {
                        	columns.append(" , ${c.columnName} = :${c.columnName}");
                        }
                    #end}
                    #end
                    
                    if (entity.get${id}() != null) {
                        resultMap.put("${idColumn}", entity.get${id}());
                    } else {
                        throw new NullPointerException("update方法是根据ID更新实体，ID属性为空，请设置ID属性值；要么使用updateBatch。");
                    }
                    columns.append(" where ${idColumn} = :${idColumn}");
                    
                    resultMap.put(SQL_KEY, columns.toString());
                    break;
                case WHERE:
                    buildWhere(entity, resultMap, columns, append);
                    break;
                case UPDATESET:
                    columns.append("update ").append(tableName);
                    #foreach($c in $getBeanList)
if (entity.${c.methodName} != null) {
                    	resultMap.put("_${c.columnName}", entity.${c.methodName});
                    	#if($velocityCount == 1)
columns.append(" set ${c.columnName} = :_${c.columnName}");
                        append = false;
					#else
if (append) {
                    		columns.append(" set ${c.columnName} = :_${c.columnName}");
                            append = false;
                        } else {
                        	columns.append(" , ${c.columnName} = :_${c.columnName}");
                        }
					#end}
                    #end
                    
                    resultMap.put(SQL_KEY, columns.toString());
                    break;
                case NULL:

                default:
                	#foreach($c in $getBeanList)
if (entity.${c.methodName} != null) {
                        resultMap.put("${c.columnName}", entity.${c.methodName});
                    }
                    #end
break;
            }
        }
        return resultMap;
    }

    protected void buildWhere(${className} entity, Map<String, Object> resultMap, StringBuilder columns, boolean append) {
    	#foreach($c in $getBeanList)
if (entity.${c.methodName} != null) {
			resultMap.put("${c.columnName}", entity.${c.methodName});
        	#if($velocityCount == 1)
columns.append(" where ${c.columnName} = :${c.columnName}");
            append = false;
        #else
if (append) {
        		columns.append(" where ${c.columnName} = :${c.columnName}");
                append = false;
            } else {
            	columns.append(" and ${c.columnName} = :${c.columnName}");
            }
        #end}
        #end
resultMap.put(SQL_KEY, columns.toString());
    }

    public Map<String, Object> mapBean(Object entity) {
    	Map<String, Object> resultMap = new HashMap<>();
    	if (entity instanceof ${className}) {
    	    resultMap = mapBean((${className}) entity, SqlType.NULL);
    	}
    	return resultMap;
    }
}
