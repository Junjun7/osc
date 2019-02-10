package com.gdufe.osc.utils;

import com.gdufe.osc.common.TokenConstant;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:20
 */
@Component
public class CacheToken {

	private static final int NUM = 100;

	/** 永不过期，cron定时任务自动更新token，最大放入量为100 **/
	public static Cache<String, String> cache = CacheBuilder.newBuilder()
			.maximumSize(NUM)
			.build();

	public static void putToken(String token) {

		cache.put(TokenConstant.TOKEN, token);
	}

	public static void putFreshToken(String freshToken) {

		cache.put(TokenConstant.FRESH_TOKEN, freshToken);
	}

	public static String getToken() {

		return cache.getIfPresent(TokenConstant.TOKEN);
	}

	public static String getFreshToken() {

		return cache.getIfPresent(TokenConstant.FRESH_TOKEN);
	}
}








