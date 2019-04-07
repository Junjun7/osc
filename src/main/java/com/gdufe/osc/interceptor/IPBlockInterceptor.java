package com.gdufe.osc.interceptor;

import com.alibaba.fastjson.JSON;
import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.enums.OscResultEnum;
import com.gdufe.osc.service.RedisHelper;
import com.gdufe.osc.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: yizhen
 * @Date: 2018/12/28 12:11
 */
@Slf4j
public class IPBlockInterceptor implements HandlerInterceptor {

	/** 10s内访问50次，认为是刷接口，就要进行一个限制 */
	private static final long TIME = 10;
	private static final long CNT = 50;
	private Object lock = new Object();

	@Autowired
	private RedisHelper<Integer> redisHelper;

	/**
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		synchronized (lock) {
			String ip = IPUtils.getClientIp(request);
			String url = request.getRequestURL().toString();
			String param = getAllParam(request);
			boolean isExist = redisHelper.isExist(ip);
			if (isExist) {
				// 如果存在,直接cnt++
				int cnt = redisHelper.incr(ip);
				if (cnt > IPBlockInterceptor.CNT) {
					OscResult<String> result = new OscResult<>();
					response.setCharacterEncoding("UTF-8");
					response.setHeader("content-type", "application/json;charset=UTF-8");
					result = result.fail(OscResultEnum.LIMIT_EXCEPTION);
					response.getWriter().print(JSON.toJSONString(result));
					log.error("ip = {}, 请求过快，被限制", ip);
					// 设置ip不过期 加入黑名单
					redisHelper.set(ip, --cnt);
					return false;
				}
				log.info("ip = {}, {}s之内第{}次请求{}，参数为{}，通过", ip, TIME, cnt, url, param);
			} else {
				// 第一次访问
				redisHelper.setEx(ip, IPBlockInterceptor.TIME, 1);
				log.info("ip = {}, {}s之内第1次请求{}，参数为{}，通过", ip, TIME, url, param);
			}
			return true;
		}
	}

	private String getAllParam(HttpServletRequest request) {
		Map<String, String[]> map = request.getParameterMap();
		StringBuilder sb = new StringBuilder("[");
		map.forEach((x, y) -> {
			String s = StringUtils.join(y, ",");
			sb.append(x + " = " + s + ";");
		});
		sb.append("]");
		return sb.toString();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
	}
}
