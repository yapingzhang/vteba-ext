package com.vteba.ext.mongodb.factory;

import java.io.Serializable;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

import com.vteba.ext.mongodb.impl.MongoGenericDaoImpl;

/**
 * 定制化的Mongodb Repository Factory
 * @author yinlei
 * @date 2013年10月19日 下午11:52:17
 */
public class MongoRepoFactory extends MongoRepositoryFactory {
	private MongoOperations mongoOperations;
	
	public MongoRepoFactory(MongoOperations mongoOperations) {
		super(mongoOperations);
		this.mongoOperations = mongoOperations;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object getTargetRepository(RepositoryMetadata metadata) {
		MongoEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());
		return new MongoGenericDaoImpl(entityInformation, mongoOperations);
	}
	
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return MongoGenericDaoImpl.class;
	}
}
