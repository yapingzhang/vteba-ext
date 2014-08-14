package ${packages}.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.vteba.service.generic.impl.BaseServiceImpl;
import com.vteba.tx.hibernate.BaseGenericDao;
import ${packages}.dao.spi.${className}Dao;
import ${packages}.model.${className};
import ${packages}.service.spi.${className}Service;

/**
 * ${tableName}Service实现。
 * @author yinlei
 * date ${currentDate}
 */
@Named
public class ${className}ServiceImpl extends BaseServiceImpl<${className}, ${pk}> implements ${className}Service {
	protected ${className}Dao ${smallClassName}DaoImpl;
	
	@Inject
	@Override
	public void setBaseGenericDaoImpl(
			BaseGenericDao<${className}, ${pk}> ${smallClassName}DaoImpl) {
		this.baseGenericDaoImpl = ${smallClassName}DaoImpl;
		this.${smallClassName}DaoImpl = (${className}Dao) ${smallClassName}DaoImpl;
	}


}
