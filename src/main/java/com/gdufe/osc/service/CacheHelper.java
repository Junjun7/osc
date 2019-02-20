package com.gdufe.osc.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yizhen
 * @Date: 2018/12/18 12:25
 */
@Service
public interface CacheHelper<V> {

	V get(String key);

	void put(String key, V value);
}
