package com.gdufe.osc.scheduled;

import com.gdufe.osc.entity.AccessToken;
import com.gdufe.osc.service.RedisService;
import com.gdufe.osc.utils.CacheToken;
import com.gdufe.osc.utils.HttpMethod;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:36
 */
@Component
@Slf4j
public class CronTaskByFreshToken {

	@Autowired
	private RedisService redisService;

	private static final String URL = "https://www.oschina.net/action/openapi/token?callback=json&client_id=sW9a1Tf8AP8IIbUydQrr&client_secret=jkaHxvkGmrbpjZcebnDUhQbF6ieu9Qqc&grant_type=authorization_code&dataType=json&redirect_uri=https://www.wenber.com&code=";

	/** 每天凌晨3.30更新token */
	@Scheduled(cron = "0 30 3 * * ?")
	public void refreshCache() {
		Stopwatch stopwatch = Stopwatch.createStarted();
		AccessToken accessToken = getAccessToken();
		String newToken = accessToken.getAccessToken();
		String newFreshToken = accessToken.getRefreshToken();
		log.info("token：{}  -----  freshToken：{}", newToken, newFreshToken);
		freshGuavaCache(newToken, newFreshToken);
		freshRedisCache(newToken, newFreshToken);
		long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		log.info("refreshCache 执行花费时长： {}", duration);
	}

	private AccessToken getAccessToken() {
		String urlToken = null;
		try {
			urlToken = URL + HttpMethod.getCode();
			log.info("code = {}", StringUtils.substringAfter(urlToken, "code="));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String data = HttpMethod.get(urlToken);
//		return JSON.parseObject(data, AccessToken.class);
		return new Gson().fromJson(data, AccessToken.class);
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
