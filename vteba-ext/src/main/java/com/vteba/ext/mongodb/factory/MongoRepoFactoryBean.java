package com.vteba.ext.mongodb.factory;

import java.io.Serializable;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * Mongodb Repository FactoryBean
 * @author yinlei
 * @date 2013年10月19日 下午11:52:26
 * @param <R> MongoRepository
 * @param <T> 实体
 * @param <ID> ID
 */
public class MongoRepoFactoryBean<R extends MongoRepository<T, ID>, T, ID extends Serializable> 
		extends MongoRepositoryFactoryBean<R, T, ID> {
	
	protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {
		return new MongoRepoFactory(operations);
	}
}
