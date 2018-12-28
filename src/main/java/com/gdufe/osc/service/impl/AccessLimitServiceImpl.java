package com.gdufe.osc.service.impl;

import com.gdufe.osc.service.AccessLimitService;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

/**
 * @Author: yizhen
 * @Date: 2018/12/25 17:41
 */
@Service
public class AccessLimitServiceImpl implements AccessLimitService {

	private int limitCount = 1000;
	// 创建令牌
	private RateLimiter rateLimiter = RateLimiter.create(limitCount);

	@Override
	public boolean tryAcquire() {
		return rateLimiter.tryAcquire();
	}
}





