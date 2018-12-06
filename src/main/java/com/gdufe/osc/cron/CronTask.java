package com.gdufe.osc.cron;

import com.alibaba.fastjson.JSON;
import com.gdufe.osc.entity.AccessToken;
import com.gdufe.osc.service.RedisService;
import com.gdufe.osc.utils.CacheToken;
import com.gdufe.osc.utils.HttpMethod;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:36
 */
@Component
@Slf4j
public class CronTask {

	@Autowired
	private RedisService redisService;

	private static String url = "https://www.oschina.net/action/openapi/token?callback=json&client_id=sW9a1Tf8AP8IIbUydQrr&client_secret=jkaHxvkGmrbpjZcebnDUhQbF6ieu9Qqc&grant_type=refresh_token&dataType=json&redirect_uri=https://www.wenber.com&refresh_token=";

	// 每天凌晨3.30更新token
	@Scheduled(cron = "0 30 3 * * ?")
	public void refreshCache() {
		Stopwatch stopwatch = Stopwatch.createStarted();
		AccessToken accessToken = getAccessToken();
		String newToken = accessToken.getAccessToken();
		String newFreshToken = accessToken.getRefreshToken();
		freshGuavaCache(newToken, newFreshToken);
		freshRedisCache(newToken, newFreshToken);
		long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		log.info("refreshCache 执行花费时长： {}", duration);
		log.info("token：{}  -----  freshToken：{}", newToken, newFreshToken);
	}

	private AccessToken getAccessToken() {
		String freshToken = CacheToken.getFreshToken();
		String urlToken = url + freshToken;
		String data = HttpMethod.get(urlToken);
		return JSON.parseObject(data, AccessToken.class);
	}

	private void freshGuavaCache(String newToken, String newFreshToken) {
		CacheToken.putToken(newToken);
		CacheToken.putFreshToken(newFreshToken);
	}

	private void freshRedisCache(String newToken, String newFreshToken) {
		redisService.putToken(newToken);
		redisService.putFreshToken(newFreshToken);
	}
}
