package com.gdufe.osc.utils;

import com.gdufe.osc.common.TokenConsts;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:20
 */
@Component
public class CacheToken {

	private static final int NUM = 1000;

	/**
	 * 永不过期，最大放入量为1000
	 * cron定时任务自动更新token
	 */
	public static Cache<String, String> cache = CacheBuilder.newBuilder()
			.maximumSize(NUM)
			.build();

	public static void putToken(String token) {

		cache.put(TokenConsts.TOKEN, token);
	}

	public static void putFreshToken(String freshToken) {

		cache.put(TokenConsts.FRESH_TOKEN, freshToken);
	}

	public static String getToken() {

		return cache.getIfPresent(TokenConsts.TOKEN);
	}

	public static String getFreshToken() {

		return cache.getIfPresent(TokenConsts.FRESH_TOKEN);
	}

	public static void putCK(String ck) {
		cache.put("ck", ck);
	}

	public static String getCK() {
		return cache.getIfPresent("ck");
	}
}








