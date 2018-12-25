package com.gdufe.osc.service;

import org.springframework.stereotype.Service;

/**
 * @Author: yizhen
 * @Date: 2018/12/25 17:40
 */
@Service
public interface AccessLimitService {

	boolean tryAcquire();
}
