package com.vteba.ext.mongodb.spi;

import java.io.Serializable;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Mongodb泛型接口抽象
 * @author yinlei
 * @date 2013年10月19日 上午12:11:44
 */
public interface MongoGenericDao<T, ID extends Serializable> extends MongoRepository<T, ID> {
	
	/**
	 * 获得MongoOperations，进行更精细的操作
	 * @return MongoTemplate
	 * @author yinlei
	 * @date 2013年10月19日 下午10:34:24
	 */
	public MongoOperations getMongoOperations();
	
	/**
	 * 更新实体entity，session.update(entity)
	 * @param entity
	 */
	public void update(T entity);
	
	/**
	 * 根据ID get实体，立即命中数据库，为空时返回null，同JPA find()
	 * @param id 主键
	 * @param entityClass 对象类型
	 * @return 实体
	 */
	public T get(ID id, Class<T> entityClass);
	
	/**
	 * 根据ID删除实体
	 * @param id
	 */
	public void delete(ID id);
	
	/**
	 * 根据entity(带主键)删除实体，同JPA remove()
	 * @param entity
	 */
	public void delete(T entity);
}
