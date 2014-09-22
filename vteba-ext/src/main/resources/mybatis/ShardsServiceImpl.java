package ${packages}.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.vteba.tx.jdbc.params.DeleteBean;
import com.vteba.tx.jdbc.params.QueryBean;
import com.vteba.tx.jdbc.params.UpdateBean;

import ${packages}.dao.${className}Dao;
import ${packages}.model.${className};
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
	public int count(QueryBean params) {
		return ${smallClassName}Dao.count(params);
	}

	@Override
	public int deleteBatch(DeleteBean params) {
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
	public List<${className}> queryForList(QueryBean params) {
		return ${smallClassName}Dao.queryForList(params);
	}

	@Override
	public ${className} get(${pk} id) {
		return ${smallClassName}Dao.get(id);
	}

	@Override
	public int updateBatch(UpdateBean params) {
		return ${smallClassName}Dao.updateBatch(params);
	}

	@Override
	public int updateById(UpdateBean params) {
		return ${smallClassName}Dao.updateById(params);
	}

    @Override
    public int countBy(QueryBean params) {
        return ${smallClassName}Dao.countBy(params);
    }

    @Override
    public int deleteBulks(DeleteBean params) {
        return ${smallClassName}Dao.deleteBulks(params);
    }

    @Override
    public List<${className}> queryList(QueryBean params) {
        return ${smallClassName}Dao.queryList(params);
    }

    @Override
    public List<${className}> pagedForList(QueryBean params) {
        return ${smallClassName}Dao.pagedForList(params);
    }

    @Override
    public List<${className}> pagedList(QueryBean params) {
        return ${smallClassName}Dao.pagedList(params);
    }

    @Override
    public int updateBulks(UpdateBean params) {
        return ${smallClassName}Dao.updateBulks(params);
    }

}
