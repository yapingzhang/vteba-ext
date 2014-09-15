package ${packages}.dao.spi;

import com.vteba.cache.mongodb.spi.MongoGenericDao;
import ${packages}.model.${className};

/**
 * ${tableName}MongoDao接口。简化MongoDB操作。
 * @author yinlei
 * date ${currentDate}
 */
public interface ${className}MongoDao extends MongoGenericDao<${className}, ${pk}> {

}
