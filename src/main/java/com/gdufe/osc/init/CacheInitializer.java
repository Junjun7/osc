package com.gdufe.osc.init;

import com.gdufe.osc.exception.NetworkException;
import com.gdufe.osc.scheduled.CronTaskByFreshToken;
import com.gdufe.osc.service.RedisService;
import com.gdufe.osc.utils.CacheToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:34
 */
@Slf4j
@Component
public class CacheInitializer /*implements ApplicationListener<ApplicationReadyEvent>*/ {

	@Autowired
	private RedisService redisService;
	@Autowired
	private CronTaskByFreshToken cronTaskByFreshToken;

	public CacheInitializer() {
		log.info("redisService = " + redisService);
		log.info("cronTaskByFreshToken = " + cronTaskByFreshToken);
	}

	@PostConstruct
	public void onApplicationEvent() {
		log.info("start....");
		log.info("redisService = " + redisService);
		log.info("cronTaskByFreshToken = " + cronTaskByFreshToken);
		try {
			init();
		} catch (Exception e) {
			log.error("init error e = {}", e);
		}
		log.info("end....");
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
