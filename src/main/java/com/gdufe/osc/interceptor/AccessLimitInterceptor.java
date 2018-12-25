package com.gdufe.osc.interceptor;

import com.gdufe.osc.service.AccessLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: yizhen
 * @Date: 2018/12/24 15:42
 */
@Slf4j
public class AccessLimitInterceptor implements HandlerInterceptor {

	@Autowired
	private AccessLimitService accessLimitService;
	private Object lock = new Object();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		synchronized (lock) {
			boolean isOk = accessLimitService.tryAcquire();
			if (isOk) {
				log.info("本次请求正常通过");
				return true;
			} else {
				log.error("请求过快，请稍后再试");
				return false;
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
	}
}
