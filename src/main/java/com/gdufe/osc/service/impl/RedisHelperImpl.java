package com.gdufe.osc.service.impl;

import com.alibaba.fastjson.JSON;
import com.gdufe.osc.service.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: yizhen
 * @Date: 2018/12/18 12:26
 */
@Service
public class RedisHelperImpl<V> implements RedisHelper<V> {

	@Autowired(required = false)
	private RedisTemplate<String, V> redisTemplate;

	@Override
	public V get(String key, Class<V> clazz) {

		byte[] execute = redisTemplate.execute((RedisCallback<byte[]>)
				conn -> conn.get(key.getBytes()));
		if (execute == null) {
			return null;
		}
		return JSON.parseObject(execute, clazz);
	}

	/**
	 * 原子操作
	 * @param key
	 * @return
	 */
	@Override
	public int incr(String key) {
		Long cnt = redisTemplate.execute((RedisCallback<Long>) conn ->
				conn.incr(key.getBytes()));
		return Integer.parseInt(cnt.toString());
	}

	@Override
	public Boolean set(String key, V value) {

		String json = JSON.toJSONString(value);
		Boolean execute = redisTemplate.execute((RedisCallback<Boolean>) conn ->
				conn.set(key.getBytes(), json.getBytes()));
		return execute;
	}

	@Override
	public Boolean setEx(String key, Long seconds, V value) {

		String json = JSON.toJSONString(value);
		Boolean execute = redisTemplate.execute((RedisCallback<Boolean>) conn ->
				conn.setEx(key.getBytes(), seconds, json.getBytes()));
		return execute;
	}

	@Override
	public Boolean isExist(String key) {

		boolean flag = redisTemplate.execute((RedisCallback<Boolean>) conn ->
				conn.exists(key.getBytes()));
		return flag;
	}
}
