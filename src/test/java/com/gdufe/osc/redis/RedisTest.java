package com.gdufe.osc.redis;

import com.gdufe.osc.OscApplicationTests;
import com.gdufe.osc.common.TokenConsts;
import com.gdufe.osc.service.RedisHelper;
import com.gdufe.osc.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author changwenbo
 * @date 2019/11/6 20:17
 * token = 00b5c94e-14d8-4774-9018-9c1a78f2e998
 * freshToken = 4f09ec00-4336-4d42-9e8d-a6222c1a6433
 */
@Slf4j
public class RedisTest extends OscApplicationTests {

	@Autowired
	private RedisService redisService;

	@Autowired
	private RedisHelper<String> redisHelper;

	@Test
	public void get() {
		String token = redisService.getToken();
		String freshToken = redisService.getFreshToken();
		log.info("token = {}, freshToken = {}", token, freshToken);
		token = redisHelper.get(TokenConsts.TOKEN, String.class);
		freshToken = redisHelper.get(TokenConsts.FRESH_TOKEN, String.class);
		log.info("token = {}, freshToken = {}", token, freshToken);
	}

	@Test
	public void set() {
		redisService.putToken("00b5c94e-14d8-4774-9018-9c1a78f2e998");
		redisService.putFreshToken("4f09ec00-4336-4d42-9e8d-a6222c1a6433");
		Boolean flag = redisHelper.set(TokenConsts.TOKEN, "00b5c94e-14d8-4774-9018-9c1a78f2e998");
		log.info("flag = {}", flag);
		flag = redisHelper.setEx(TokenConsts.FRESH_TOKEN, 36000L, "00b5c94e-14d8-4774-9018-9c1a78f2e998");
		log.info("flag = {}", flag);
		flag = redisHelper.isExist(TokenConsts.TOKEN);
		log.info("flag = {}", flag);
	}

	@Test
	public void incr() {
		int id = redisHelper.incr("id");
		log.info("id = {}", id);
	}
}














