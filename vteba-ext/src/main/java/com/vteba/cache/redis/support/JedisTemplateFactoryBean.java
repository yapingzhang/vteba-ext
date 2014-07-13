package com.vteba.cache.redis.support;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.vteba.cache.redis.JedisTemplate;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class JedisTemplateFactoryBean implements InitializingBean, FactoryBean<JedisTemplate>{
	private static final int DEFAULT_MAX_IDLE = 20;
	private static final int DEFAULT_MAX_ACTIVE = 500;
	
	private JedisTemplate jedisTemplate;
	private String host;
	private int port;
	private int timeout;
	private int maxIdle;
	private int maxActive;
	private int checkingIntervalSecs;
	private int evictableIdleTimeSecs;
	private String encoding;
	
	@Override
	public JedisTemplate getObject() throws Exception {
		return this.jedisTemplate;
	}

	@Override
	public Class<JedisTemplate> getObjectType() {
		return JedisTemplate.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//配置参数验证
		if (host == null || host.equals("")) {
			throw new RuntimeException("请配置Redis主机地址");
		}
		
		if (port == 0) {
			throw new RuntimeException("请配置Redis端口");
		}
		
		if (timeout == 0) {//超时时间，单位毫秒
			timeout = Protocol.DEFAULT_TIMEOUT;
		} else {
			timeout = timeout * 1000;
		}
		
		if (maxIdle == 0) {//JedisPool最大空闲连接数
			maxIdle = DEFAULT_MAX_IDLE;
		}
		
		if (maxActive == 0) {//JedisPool中最大活动连接数
			maxActive = DEFAULT_MAX_ACTIVE;
		}
		
		//create jedis pool config
		JedisPoolConfig poolConfig = JedisUtils.createPoolConfig(maxIdle, maxActive);
		if (checkingIntervalSecs == 0) {//多长时间检查JedisPool中空闲的连接，单位毫秒
			poolConfig.setTimeBetweenEvictionRunsMillis(checkingIntervalSecs * 1000);
		}
		
		if (evictableIdleTimeSecs == 0) {//JedisPool中空闲的连接空闲多久，删除它，单位毫秒
			poolConfig.setMinEvictableIdleTimeMillis(evictableIdleTimeSecs * 1000);
		}
		
		// create jedis pool
		JedisPool jedisPool = new JedisPool(poolConfig, host, port, timeout);
		jedisTemplate = new JedisTemplate(jedisPool);
		
		if (encoding != null) {
			jedisTemplate.setEncoding(encoding);
		}
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public void setCheckingIntervalSecs(int checkingIntervalSecs) {
		this.checkingIntervalSecs = checkingIntervalSecs;
	}

	public void setEvictableIdleTimeSecs(int evictableIdleTimeSecs) {
		this.evictableIdleTimeSecs = evictableIdleTimeSecs;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
