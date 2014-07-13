package com.vteba.cache.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.vteba.utils.serialize.Kryos;

/**
 * 基于Kryo的RedisSerializer。
 * @author yinlei
 * @date 2013年10月20日 下午8:49:01
 * @param <T> 要被序列化的对象
 */
public class KryoRedisSerializer<T> implements RedisSerializer<T> {

	@Override
	public byte[] serialize(T t) throws SerializationException {
		return Kryos.get().serialize(t);
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		return Kryos.get().deserialize(bytes);
	}

}
