package ${packages}.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.vteba.service.generic.impl.GenericServiceImpl;
import com.vteba.tx.hibernate.IHibernateGenericDao;
import ${packages}.dao.spi.${className}Dao;
import ${packages}.model.${className};
import ${packages}.service.spi.${className}Service;

/**
 * ${tableName}Service实现。
 * @author yinlei
 * date ${currentDate}
 */
@Named
public class ${className}ServiceImpl extends GenericServiceImpl<${className}, ${pk}> implements ${className}Service {
	protected ${className}Dao ${smallClassName}DaoImpl;
	
	@Inject
	@Override
	public void setHibernateGenericDaoImpl(
			IHibernateGenericDao<${className}, ${pk}> ${smallClassName}DaoImpl) {
		this.hibernateGenericDaoImpl = ${smallClassName}DaoImpl;
		this.${smallClassName}DaoImpl = (${className}Dao) ${smallClassName}DaoImpl;
	}


}
