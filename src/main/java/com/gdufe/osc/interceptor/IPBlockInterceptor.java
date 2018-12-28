package com.gdufe.osc.interceptor;

import com.alibaba.fastjson.JSON;
import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.service.RedisHelper;
import com.gdufe.osc.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: yizhen
 * @Date: 2018/12/28 12:11
 */
@Slf4j
@Component
public class IPBlockInterceptor implements HandlerInterceptor {

	private Object lock = new Object();
	// 10s内访问100次，认为是刷接口，就要进行一个限制
	private static final long TIME = 10;
	private static final long CNT = 100;

	@Autowired
	private RedisHelper<Integer> redisHelper;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		synchronized (lock) {
			String ip = IPUtils.getClientIp(request);
			boolean isExist = redisHelper.isExist(ip);
			if (isExist) {
				// 如果存在,不是第一次访问
				int cnt = redisHelper.get(ip, Integer.class);
				if (cnt >= IPBlockInterceptor.CNT) {
					OscResult<String> result = new OscResult<>();
					response.setCharacterEncoding("UTF-8");
					response.setHeader("content-type", "application/json;charset=UTF-8");
					result = result.fail("10s，请求超过100次，请不要刷接口");
					response.getWriter().print(JSON.toJSONString(result));
					log.info("ip = {}, 请求过快，被限制", ip);
					return false;
				}
				redisHelper.setEx(ip, IPBlockInterceptor.TIME, ++cnt);
				log.info("ip = {}, 10s之内第{}次请求，通过", ip, cnt);
			} else {
				// 第一次访问
				redisHelper.setEx(ip, IPBlockInterceptor.TIME, 1);
				log.info("ip = {}, 10s之内第1次请求，通过", ip);
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
	}

}
