package com.gdufe.osc.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 14:15
 */
@Service
public interface RedisService {

	void putToken(String token);

	void putFreshToken(String freshToken);

	String getToken();

	String getFreshToken();
}
