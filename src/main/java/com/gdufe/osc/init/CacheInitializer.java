package com.gdufe.osc.init;

import com.gdufe.osc.exception.NetworkException;
import com.gdufe.osc.scheduled.CronTaskByFreshToken;
import com.gdufe.osc.service.RedisService;
import com.gdufe.osc.utils.CacheToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:34
 */
@Slf4j
@Component
public class CacheInitializer implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private RedisService redisService;
	@Autowired
	private CronTaskByFreshToken cronTaskByFreshToken;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {
			init();
		} catch (Exception e) {
			log.error("init error e = {}", e);
		}
	}

	private void init() throws NetworkException {
		// fresh token by code
		cronTaskByFreshToken.refreshCache();
		String token = redisService.getToken();
		String freshToken = redisService.getFreshToken();
		CacheToken.putToken(token);
		CacheToken.putFreshToken(freshToken);
	}
}
