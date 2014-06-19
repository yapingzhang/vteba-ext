package com.vteba.${packages}.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.vteba.service.generic.impl.GenericServiceImpl;
import com.vteba.tm.hibernate.IHibernateGenericDao;
import com.vteba.${packages}.dao.spi.${className}Dao;
import com.vteba.${packages}.model.${className};
import com.vteba.${packages}.service.spi.${className}Service;

/**
 * ${tableName}Service实现。
 * @author yinlei
 * date ${currentDate}
 */
@Named
public class ${className}ServiceImpl extends GenericServiceImpl<${className}, ${pk}> implements ${className}Service {
	private ${className}Dao ${smallClassName}DaoImpl;
	
	@Inject
	@Override
	public void setHibernateGenericDaoImpl(
			IHibernateGenericDao<${className}, ${pk}> ${smallClassName}DaoImpl) {
		this.hibernateGenericDaoImpl = ${smallClassName}DaoImpl;
		this.${smallClassName}DaoImpl = (${className}Dao) ${smallClassName}DaoImpl;
		this.springJdbcTemplate = this.${smallClassName}DaoImpl.getSpringJdbcTemplate();
	}


}
