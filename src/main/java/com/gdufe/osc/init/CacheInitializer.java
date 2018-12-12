package com.gdufe.osc.init;

import com.gdufe.osc.service.RedisService;
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

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		init();
	}

	/**
	 * code获取
	 * https://www.oschina.net/action/oauth2/authorize?state=cwb&response_type=code&client_id=sW9a1Tf8AP8IIbUydQrr&redirect_uri=https://www.wenber.com
	 */
	private void init() {
		String token = redisService.getToken();
		String freshToken = redisService.getFreshToken();
		CacheToken.cache.put("token", token);
		CacheToken.cache.put("freshToken", freshToken);
	}
}
