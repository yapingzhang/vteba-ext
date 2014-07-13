package com.vteba.${packages}.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.vteba.service.generic.impl.BaseServiceImpl;
import com.vteba.tm.hibernate.BaseGenericDao;
import com.vteba.${packages}.dao.spi.${className}Dao;
import com.vteba.${packages}.model.${className};
import com.vteba.${packages}.service.spi.${className}Service;

/**
 * ${tableName}Service实现。
 * @author yinlei
 * date ${currentDate}
 */
@Named
public class ${className}ServiceImpl extends BaseServiceImpl<${className}, ${pk}> implements ${className}Service {
	private ${className}Dao ${smallClassName}DaoImpl;
	
	@Inject
	@Override
	public void setBaseGenericDaoImpl(
			BaseGenericDao<${className}, ${pk}> ${smallClassName}DaoImpl) {
		this.BaseGenericDaoImpl = ${smallClassName}DaoImpl;
		this.${smallClassName}DaoImpl = (${className}Dao) ${smallClassName}DaoImpl;
		this.springJdbcTemplate = this.${smallClassName}DaoImpl.getSpringJdbcTemplate();
	}


}
