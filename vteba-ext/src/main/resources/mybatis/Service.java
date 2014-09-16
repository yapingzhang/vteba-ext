package ${packages}.service.spi;

import java.util.List;

import ${packages}.model.${className};
import ${packages}.model.${className}Bean;

/**
 * ${tableName}相关的业务service接口。
 * @author yinlei
 * @date ${currentDate}
 */
public interface ${className}Service {

    /**
     * 根据params所携带条件进行count计数。
     * @param params 查询条件
     * @return 统计的记录条数
     */
    public int count(${className}Bean params);

    /**
     * 根据params所携带条件进行计数，条件是等于，且是and关系。
     * @param params 查询条件
     * @return 统计的记录条数
     */
    public int countBy(${className} params);

    /**
     * 根据params所携带条件删除记录，适用于复杂条件。
     * @param params 查询条件
     * @return 删除的记录条数
     */
    public int deleteBatch(${className}Bean params);

    /**
     * 根据params所携带条件删除数据，条件是等于，且是and关系。
     * @param params 删除条件
     * @return 删除的记录条数
     */
    public int deleteBulks(${className} params);

    /**
     * 根据主键删除记录。
     * @param id 主键id
     * @return 删除的记录条数，1或0
     */
    public int deleteById(${pk} id);

    /**
     * 插入记录，只有非空字段才会插入到数据库。
     * @param record 要被保存的数据
     * @return 保存成功的记录条数，1或0
     */
    public int save(${className} record);

    /**
     * 根据params所携带条件查询数据，适用于复杂查询。
     * @param params 查询条件
     * @return ${tableName}实体list
     */
    public List<${className}> queryForList(${className}Bean params);

    /**
     * 根据params所携带条件查询数据，条件是等于，且是and关系。
     * @param params 查询条件
     * @return ${tableName}实体list
     */
    public List<${className}> queryList(${className} params);

    /**
     * 根据params所携带条件分页查询数据，适用于复杂查询。
     * @param params 查询条件
     * @return ${tableName}实体list
     */
    public List<${className}> pagedForList(${className}Bean params);

    /**
     * 根据params所携带条件分页查询数据，条件是等于，且是and关系。
     * @param params 查询条件
     * @return ${tableName}实体list
     */
    public List<${className}> pagedList(${className} params);

    /**
     * 根据主键查询数据。
     * @param id 主键
     * @return ${tableName}实体
     */
    public ${className} get(${pk} id);

    /**
     * 根据params所携带条件更新指定字段，适用于复杂条件。
     * @param record 要更新的数据
     * @param params update的where条件
     * @return 更新记录条数
     */
    public int updateBatch(${className} record, ${className}Bean params);

    /**
     * 根据params所携带条件更新指定字段，条件是等于，且是and关系。
     * @param record 要更新的数据
     * @param params update的where条件
     * @return 更新记录条数
     */
    public int updateBulks(${className} record, ${className} params);

    /**
     * 根据主键更新指定字段的数据。
     * @param record 要更新的数据，含有Id
     * @return 更新记录条数
     */
    public int updateById(${className} record);

}
