package com.gdufe.osc.service.impl;

import com.gdufe.osc.service.CacheHelper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: yizhen
 * @date: 2019/2/20 17:56
 */
@Service
public class CacheHelperImpl<V> implements CacheHelper<V> {

	private static final int TIME = 24;
	private static final int NUM = 1000;

	// 过期25h，最大放入量为1000
	Cache<String, V> cache = CacheBuilder.newBuilder()
			.expireAfterWrite(TIME, TimeUnit.HOURS)
			.maximumSize(NUM)
			.build();

	@Override
	public V get(String key) {
		return cache.getIfPresent(key);
	}

	@Override
	public void put(String key, V value) {
		cache.put(key, value);
	}
}
