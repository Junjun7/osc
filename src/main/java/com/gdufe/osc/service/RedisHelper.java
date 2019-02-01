package com.gdufe.osc.service;

import org.springframework.stereotype.Service;

/**
 * @Author: yizhen
 * @Date: 2018/12/18 12:25
 */
@Service
public interface RedisHelper<V> {

	V get(String key, Class<V> clazz);

	int incr(String key);

	Boolean set(String key, V value);

	Boolean setEx(String key, Long seconds, V value);

	Boolean isExist(String key);
}
