package ${packages}.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ${packages}.dao.${className}Dao;
import ${packages}.model.${className};
import ${packages}.model.${className}Bean;
import ${packages}.service.spi.${className}Service;

/**
 * ${tableName}相关的service业务实现。
 * @author yinlei
 * @date ${currentDate}
 */
@Named
public class ${className}ServiceImpl implements ${className}Service {
	
	@Inject
	private ${className}Dao ${smallClassName}Dao;

	@Override
	public int count(${className}Bean params) {
		return ${smallClassName}Dao.count(params);
	}

	@Override
	public int deleteBatch(${className}Bean params) {
		return ${smallClassName}Dao.deleteBatch(params);
	}

	@Override
	public int deleteById(${pk} id) {
		return ${smallClassName}Dao.deleteById(id);
	}

	@Override
	public int save(${className} record) {
		return ${smallClassName}Dao.save(record);
	}

	@Override
	public List<${className}> queryForList(${className}Bean params) {
		return ${smallClassName}Dao.queryForList(params);
	}

	@Override
	public ${className} get(${pk} id) {
		return ${smallClassName}Dao.get(id);
	}

	@Override
	public int updateBatch(${className} record, ${className}Bean params) {
		return ${smallClassName}Dao.updateBatch(record, params);
	}

	@Override
	public int updateById(${className} record) {
		return ${smallClassName}Dao.updateById(record);
	}

    @Override
    public int countBy(${className} params) {
        return ${smallClassName}Dao.countBy(params);
    }

    @Override
    public int deleteBulks(${className} params) {
        return ${smallClassName}Dao.deleteBulks(params);
    }

    @Override
    public List<${className}> queryList(${className} params) {
        return ${smallClassName}Dao.queryList(params);
    }

    @Override
    public List<${className}> pagedForList(${className}Bean params) {
        return ${smallClassName}Dao.pagedForList(params);
    }

    @Override
    public List<${className}> pagedList(${className} params) {
        return ${smallClassName}Dao.pagedList(params);
    }

    @Override
    public int updateBulks(${className} record, ${className} params) {
        return ${smallClassName}Dao.updateBulks(record, params);
    }

}
