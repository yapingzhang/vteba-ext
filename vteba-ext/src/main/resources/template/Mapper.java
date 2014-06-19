package com.vteba.${packages}.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vteba.${packages}.model.${className};

/**
 * ${tableName}Mapper
 * @author yinlei
 * @date ${currentDate}
 */
public class ${className}Mapper implements RowMapper<${className}> {

	@Override
	public ${className} mapRow(ResultSet rs, int rowNum) throws SQLException {
        ${className} ${smallClassName} = new ${className}();
    #foreach($c in $methodList)
	${smallClassName}.${c.methodName}(rs.${c.rsName});
    #end
	return ${smallClassName};
	}

}
