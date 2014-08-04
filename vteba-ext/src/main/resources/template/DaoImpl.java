package com.vteba.${packages}.dao.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.SessionFactory;

import com.vteba.tx.hibernate.impl.BaseGenericDaoImpl;
import com.vteba.tx.jdbc.spring.SpringJdbcTemplate;
import com.vteba.${packages}.dao.spi.${className}Dao;
import com.vteba.${packages}.model.${className};

/**
 * ${tableName}Dao实现。
 * @author yinlei
 * date ${currentDate}
 */
@Named
public class ${className}DaoImpl extends BaseGenericDaoImpl<${className}, ${pk}> implements ${className}Dao {
	
	public ${className}DaoImpl() {
		super();
	}

	public ${className}DaoImpl(Class<${className}> entityClass) {
		super(entityClass);
	}

	@Inject
	@Override
	public void setSessionFactory(SessionFactory ${schema}SessionFactory) {
		this.sessionFactory = ${schema}SessionFactory;
		
	}

	@Inject
	public void setSpringJdbcTemplate(SpringJdbcTemplate ${schema}JdbcTemplate) {
		this.springJdbcTemplate = ${schema}JdbcTemplate;
	}
}
