package com.gdufe.osc.service.impl;

import com.gdufe.osc.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 14:15
 */
@Service
public class RedisServiceImpl implements RedisService {

	private static final String TOKEN = "token";
	private static final String FRESH_TOKEN = "freshToken";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public void putToken(String token) {
		stringRedisTemplate.opsForValue().set(TOKEN, token);
	}

	@Override
	public void putFreshToken(String freshToken) {
		stringRedisTemplate.opsForValue().set(FRESH_TOKEN, freshToken);
	}

	@Override
	public String getToken() {
		return stringRedisTemplate.opsForValue().get(TOKEN);
	}

	@Override
	public String getFreshToken() {
		return stringRedisTemplate.opsForValue().get(FRESH_TOKEN);
	}
}
