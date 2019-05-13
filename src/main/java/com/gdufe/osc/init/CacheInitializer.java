package com.gdufe.osc.init;

import com.gdufe.osc.common.TokenConsts;
import com.gdufe.osc.scheduled.CronTask;
import com.gdufe.osc.service.RedisService;
import com.gdufe.osc.service.ZhiHuSpider;
import com.gdufe.osc.utils.CacheToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:34
 */
@Component
public class CacheInitializer implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private RedisService redisService;
	@Autowired
	private CronTask cronTask;
	@Autowired
	private ZhiHuSpider zhiHuSpider;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		init();
	}

	private void init() {
		// fresh token by code
		cronTask.refreshCache();
		String token = redisService.getToken();
		String freshToken = redisService.getFreshToken();
		CacheToken.putToken(token);
		CacheToken.putFreshToken(freshToken);
	}
}
