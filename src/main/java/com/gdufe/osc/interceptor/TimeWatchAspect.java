package com.gdufe.osc.interceptor;

import com.gdufe.osc.annotation.TimeWatch;
import com.gdufe.osc.utils.GsonUtils;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: yizhen
 * @date: 2019/1/24 15:38
 */
@Component
@Slf4j
@Aspect
public class TimeWatchAspect {

	@Pointcut("execution(public * com.gdufe.osc.controller..*(..))")
	public void pointcutController() {}

	@Pointcut("@annotation(timeWatch)")
	public void pointcutTimeWatch(TimeWatch timeWatch) {}

	@Around(value = "pointcutController() && pointcutTimeWatch(timeWatch)")
	public Object methodAround(ProceedingJoinPoint joinPoint, TimeWatch timeWatch) {
		Object res = null;
		log.info("request = {}", GsonUtils.toJson(joinPoint.getArgs()));
		String methodName = joinPoint.getSignature().getName();
		try {
			// 前置通知
			Stopwatch stopwatch = Stopwatch.createStarted();
			// 执行目标方法
			res = joinPoint.proceed();
			// 后置通知
			long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
			log.info("response = {}", GsonUtils.toJson(res));
			log.info("{} 执行时长: {} ms", methodName, duration);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return res;
	}
}

















