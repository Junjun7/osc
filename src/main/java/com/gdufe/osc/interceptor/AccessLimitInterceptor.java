package com.gdufe.osc.interceptor;

import com.alibaba.fastjson.JSON;
import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.enums.OscResultEnum;
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
		/**
		 * 这个记得加上lock
		 * 不加lock，在每个线程的工作内存都有一个limit，起不到限流的作用了
		 */
		synchronized (lock) {
			boolean isOk = accessLimitService.tryAcquire();
			if (isOk) {
				log.info("本次请求正常通过");
				return true;
			} else {
				log.error("请求过快，请稍后再试");
				// 返回前端请求过快 稍后再试
				OscResult<String> result = new OscResult<>();
				result = result.fail(OscResultEnum.LIMIT_EXCEPTION);
				response.setCharacterEncoding("UTF-8");
				response.setHeader("content-type", "application/json;charset=UTF-8");
				response.getWriter().print(JSON.toJSONString(result));
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
