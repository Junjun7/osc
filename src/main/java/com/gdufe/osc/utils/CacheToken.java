package com.gdufe.osc.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:20
 */
@Component
public class CacheToken {

	private static  String url = "https://www.oschina.net/action/openapi/token?callback=json&client_id=sW9a1Tf8AP8IIbUydQrr&client_secret=jkaHxvkGmrbpjZcebnDUhQbF6ieu9Qqc&grant_type=refresh_token&dataType=json&redirect_uri=https://www.wenber.com&refresh_token=";

	@Autowired
	private HttpMethod httpMethod;

	/**永不过期，cron定时任务自动更新token，最大放入量为100 **/
	public static Cache<String, String> cache = CacheBuilder.newBuilder()
			.maximumSize(100)
			.build();

	public static void putToken(String token) {
		cache.put("token", token);
	}

	public static void putFreshToken(String freshToken) {
		cache.put("freshToken", freshToken);
	}

	public static String getToken() {

		return cache.getIfPresent("token");
	}

	public static String getFreshToken() {

		return cache.getIfPresent("freshToken");
	}
}








