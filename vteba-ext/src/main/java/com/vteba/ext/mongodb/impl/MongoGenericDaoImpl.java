package com.vteba.ext.mongodb.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.util.Assert;

import com.vteba.ext.mongodb.spi.MongoGenericDao;

/**
 * Mongodb泛型接口抽象实现
 * @author yinlei
 * @date 2013年10月19日 上午12:11:51
 */
public class MongoGenericDaoImpl<T, ID extends Serializable> implements MongoGenericDao<T, ID> {
	
	private MongoOperations mongoOperations;
	private MongoEntityInformation<T, ID> entityInformation;

	public MongoGenericDaoImpl() {
		
	}
	
	/**
	 * Creates a ew {@link MongoGenericDaoImpl} for the given {@link MongoEntityInformation} and {@link MongoTemplate}.
	 * 
	 * @param metadata must not be {@literal null}.
	 * @param template must not be {@literal null}.
	 */
	public MongoGenericDaoImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {

		Assert.notNull(mongoOperations);
		Assert.notNull(metadata);

		this.entityInformation = metadata;
		this.mongoOperations = mongoOperations;
	}

	public <S extends T> S save(S entity) {

		Assert.notNull(entity, "Entity must not be null!");

		mongoOperations.save(entity, entityInformation.getCollectionName());
		return entity;
	}

	public <S extends T> List<S> save(Iterable<S> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		List<S> result = new ArrayList<S>();

		for (S entity : entities) {
			save(entity);
			result.add(entity);
		}

		return result;
	}

	public T findOne(ID id) {
		Assert.notNull(id, "The given id must not be null!");
		return mongoOperations.findById(id, entityInformation.getJavaType(), entityInformation.getCollectionName());
	}

	private Query getIdQuery(Object id) {
		return new Query(getIdCriteria(id));
	}

	private Criteria getIdCriteria(Object id) {
		return where(entityInformation.getIdAttribute()).is(id);
	}

	public boolean exists(ID id) {

		Assert.notNull(id, "The given id must not be null!");
		return mongoOperations.exists(getIdQuery(id), entityInformation.getJavaType(),
				entityInformation.getCollectionName());
	}

	public long count() {
		return mongoOperations.getCollection(entityInformation.getCollectionName()).count();
	}

	public void delete(ID id) {
		Assert.notNull(id, "The given id must not be null!");
		mongoOperations.remove(getIdQuery(id), entityInformation.getJavaType(), entityInformation.getCollectionName());
	}

	public void delete(T entity) {
		Assert.notNull(entity, "The given entity must not be null!");
		delete(entityInformation.getId(entity));
	}

	public void delete(Iterable<? extends T> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		for (T entity : entities) {
			delete(entity);
		}
	}

	public void deleteAll() {
		mongoOperations.remove(new Query(), entityInformation.getCollectionName());
	}

	public List<T> findAll() {
		return findAll(new Query());
	}

	public Iterable<T> findAll(Iterable<ID> ids) {

		Set<ID> parameters = new HashSet<ID>();
		for (ID id : ids) {
			parameters.add(id);
		}

		return findAll(new Query(new Criteria(entityInformation.getIdAttribute()).in(parameters)));
	}

	public Page<T> findAll(final Pageable pageable) {

		Long count = count();
		List<T> list = findAll(new Query().with(pageable));

		return new PageImpl<T>(list, pageable, count);
	}

	public List<T> findAll(Sort sort) {
		return findAll(new Query().with(sort));
	}

	private List<T> findAll(Query query) {

		if (query == null) {
			return Collections.emptyList();
		}

		return mongoOperations.find(query, entityInformation.getJavaType(), entityInformation.getCollectionName());
	}

	/**
	 * Returns the underlying {@link MongoOperations} instance.
	 * 
	 * @return
	 */
	public MongoOperations getMongoOperations() {
		return this.mongoOperations;
	}

	public void setMongoOperations(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	/**
	 * @return the entityInformation
	 */
	protected MongoEntityInformation<T, ID> getEntityInformation() {
		return entityInformation;
	}
	
	public void update(T entity) {
		
	}

	public T get(ID id, Class<T> entityClass) {
		return mongoOperations.findById(id, entityClass);
	}
	
}
