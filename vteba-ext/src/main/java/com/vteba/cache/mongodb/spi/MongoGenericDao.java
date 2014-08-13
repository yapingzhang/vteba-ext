package com.vteba.cache.mongodb.spi;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Mongodb泛型接口抽象
 * @author yinlei
 * @date 2013年10月19日 上午12:11:44
 */
public interface MongoGenericDao<T, ID extends Serializable> extends MongoRepository<T, ID> {
    /**
     * 保存给定的实体，使用返回的实体做进一步的操作，因为保存操作可能改变被保存的实例。
     * 
     * @param entity 被保存的视图
     * @return 保存后的实体
     */
    public <S extends T> S save(S entity);

    /**
     * 保存所有给定的实体
     * 
     * @param entities 被保存的实体
     * @return 保存后的实体
     * @throws IllegalArgumentException 如果实体是null抛非法参数异常
     */
    public <S extends T> List<S> save(Iterable<S> entities);

    /**
     * 根据id查询实体
     * 
     * @param id 不能为 {@literal null}.
     * @return 查询到的实体 or {@literal null}，如果没有找到的话
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    public T findOne(ID id);

    /**
     * 查询id关联的实体是否存在
     * 
     * @param id 不能为 {@literal null}.
     * @return true id关联的实体存在，否则 {@literal false}
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    public boolean exists(ID id);

    /**
     * 返回该实体类型所有的实例
     * 
     * @return all entities
     */
    public List<T> findAll();

    /**
     * 返回IDs查询到得所有的实例
     * 
     * @param ids 主键列表
     * @return
     */
    public Iterable<T> findAll(Iterable<ID> ids);

    /**
     * 返回可用的实体数
     * 
     * @return the number of entities
     */
    public long count();

    /**
     * 根据id删除指定的实体
     * 
     * @param id 不能为 {@literal null}.
     * @throws IllegalArgumentException 如果 {@code id} 是 {@literal null}
     */
    public void delete(ID id);

    /**
     * 删除给定的实体
     * 
     * @param entity
     * @throws IllegalArgumentException 如果实体是 (@literal null}.
     */
    public void delete(T entity);

    /**
     * 删除所有给定的实体
     * 
     * @param entities
     * @throws IllegalArgumentException 如果实体 {@link Iterable} 是 (@literal null}.
     */
    public void delete(Iterable<? extends T> entities);

    /**
     * 返回该Repository托管的所有的实体。<em>慎用</em>。
     */
    public void deleteAll();
    
    /**
     * 根据排序条件，返回所有排序后的实体。
     * 
     * @param sort
     * @return all entities sorted by the given options
     */
    public List<T> findAll(Sort sort);

    /**
     * 返回严格满足分页条件的 {@link Page} 分页数据。
     * 
     * @param pageable 分页结果
     * @return 一页的实体数据
     */
    public Page<T> findAll(Pageable pageable);
    
	/**
	 * 获得MongoOperations，进行更精细的操作
	 * @return MongoTemplate
	 * @author yinlei
	 * @date 2013年10月19日 下午10:34:24
	 */
	public MongoOperations getMongoOperations();
	
	/**
	 * 根据ID查询实体
	 * @param id 主键，不能为null
	 * @param entityClass 对象类型
	 * @return 实体
	 */
	public T get(ID id, Class<T> entityClass);

}
