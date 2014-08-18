package com.vteba.cache.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis的基本操作，隔离RedisTemplate的依赖。
 * @author yinlei 
 * @since 2013-12-1 13:45
 * @param <K> 缓存键类型
 * @param <V> 缓存值类型
 */
public class RedisService<K extends Serializable, V extends Serializable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisService.class);
    
    private RedisTemplate<K, V> redisTemplate;
    
    /**
     * 将缓存值value以key为键放入缓存中。
     * @param key 键
     * @param value 值
     * @return true成功，false失败
     */
    public boolean set(final K key, final V value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            LOGGER.error("redis缓存值异常。", e.getMessage());
            return false;
        }
        return true;
    }
    
    /**
     * 将缓存值value以key为键放入缓存中。
     * @param key 键
     * @param value 值
     * @param timeout 超时时间，单位秒
     * @return true成功，false失败
     */
    public boolean set(final K key, final V value, final long timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("redis缓存值异常。", e.getMessage());
            return false;
        }
        return true;
    }
    
    /**
     * 删除换粗键key对应的缓存值
     * @param key 键
     */
    public void delete(final K key) {
        redisTemplate.delete(key);
    }
    
    /**
     * 获得key序列化器。
     * @return Key RedisSerializer
     */
    @SuppressWarnings("unchecked")
    protected RedisSerializer<K> getKeySerializer() {
        return (RedisSerializer<K>) redisTemplate.getKeySerializer();
    }
    
    /**
     * 判断缓存键是否存在
     * @param key 键
     * @return
     */
    public boolean exist(final K key) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(getKeySerializer().serialize(key));
            }
        });
    }
    
    /**
     * 将对象value以键key放入hash表id中
     * @param id hash表
     * @param key 键
     * @param value 值
     */
    public <HK, HV> void hashPut(final K id, final HK key, final HV value) {
        redisTemplate.opsForHash().put(id, key, value);
    }
    
    /**
     * 将对象values放入hash表id中
     * @param id hash表
     * @param key 键
     * @param values 值
     */
    public <HK, HV> void hashPut(final K id, final Map<HK, HV> values) {
        redisTemplate.opsForHash().putAll(id, values);
    }
    
    /**
     * 删除hash表id中的一个或者多个值
     * @param id hash表
     * @param keys hash键
     */
    public void hashDelete(final K id, final Object... keys) {
        redisTemplate.opsForHash().delete(id, keys);
    }
    
    /**
     * 获取hash表id中的键key对应的值
     * @param id hash表
     * @param key 键
     * @return 缓存值
     */
    public <HK, HV> HV hashGet(final K id, final HK key) {
        HashOperations<K, HK, HV> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(id, key);
    }
    
    /**
     * 获取hash表id中的对象List
     * @param id hash表
     * @return 换粗值List
     */
    public <HK, HV> List<HV> hashGetList(final K id) {
        HashOperations<K, HK, HV> hashOperations = redisTemplate.opsForHash();
        return hashOperations.values(id);
    }
    
    /**
     * 获取hash表id中的对象Map
     * @param id hash表
     * @return 对象Map
     */
    public <HK, HV> Map<HK, HV> hashGetMap(final K id) {
        HashOperations<K, HK, HV> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(id);
    }
}
