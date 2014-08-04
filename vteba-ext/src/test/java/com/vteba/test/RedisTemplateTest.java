package com.vteba.test;

import java.util.Date;

import javax.persistence.GeneratedValue;

import com.vteba.utils.date.DateUtils;


public class RedisTemplateTest {

    @GeneratedValue
    public static void main(String[] args) {
//        RedisTemplate<Serializable, Serializable> redisTemplate;
//        ShardedJedisPool shardedJedisPool;
//        ShardInfo<?> shardInfo;
//        JedisShardInfo jedisShardInfo;
//        JedisConnectionFactory dConnectionFactory;
        
        String dateString = DateUtils.toDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(dateString);
        
    }

}
