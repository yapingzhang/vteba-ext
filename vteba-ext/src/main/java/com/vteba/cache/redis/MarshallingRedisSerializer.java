package com.vteba.cache.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.vteba.utils.serialize.MarshaUtils;

/**
 * 基于JBoss Marshalling的Redis序列化器。性能比不注册的kryo高，但是低于protobuf。
 * @author yinlei
 * @since 2013-12-12 17:18
 * @param <T> 要被序列化的类型
 */
public class MarshallingRedisSerializer<T> implements RedisSerializer<T> {

    @Override
    public byte[] serialize(T t) throws SerializationException {
        return MarshaUtils.toBytes(t);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        return MarshaUtils.fromBytes(bytes);
    }

}
