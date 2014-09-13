package com.vteba.cache.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.vteba.utils.serialize.ProtoUtils;

/**
 * 基于Protobuf的Redis序列化器。性能比JBoss和kryo都高，但是低于原生protobuf。
 * @author yinlei
 * @since 2013-12-13 15:16
 * @param <T> 要被序列化的类型
 */
public class ProtoRedisSerializer<T> implements RedisSerializer<T> {

	public ProtoRedisSerializer() {
		
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		return ProtoUtils.fromBytes(bytes);
	}

	@Override
	public byte[] serialize(T entity) throws SerializationException {
		return ProtoUtils.toBytes(entity);
	}

}
