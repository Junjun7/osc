package com.gdufe.osc.service.impl;

import com.gdufe.osc.common.TokenConstant;
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

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public void putToken(String token) {

		stringRedisTemplate.opsForValue().set(TokenConstant.TOKEN, token);
	}

	@Override
	public void putFreshToken(String freshToken) {

		stringRedisTemplate.opsForValue().set(TokenConstant.FRESH_TOKEN, freshToken);
	}

	@Override
	public String getToken() {
		return stringRedisTemplate.opsForValue().get(TokenConstant.TOKEN);
	}

	@Override
	public String getFreshToken() {
		return stringRedisTemplate.opsForValue().get(TokenConstant.FRESH_TOKEN);
	}
}
