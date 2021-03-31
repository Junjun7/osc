package com.gdufe.osc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: yizhen
 * @Date: 2018/12/24 15:42
 */
@Slf4j
public class LogInterceptor extends AbstractInterceptor {

	private static final String SESSION_KEY = "sessionId";

	private static final int MIN = 9999;
	private static final int MAX = 10000;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String sessionId = request.getSession().getId();
		int randomId = ThreadLocalRandom.current().nextInt(MIN, MAX);
		String val = join("-", sessionId, randomId + "");
		MDC.put(SESSION_KEY, val);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
		MDC.remove(SESSION_KEY);
	}
}
