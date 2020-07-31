package com.gdufe.osc.interceptor;

import com.gdufe.osc.annotation.Retry;
import com.gdufe.osc.utils.WeChatNoticeUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 重试AOP
 */
@Component
@Slf4j
@Aspect
public class RetryAspect {

	@Autowired
	private WeChatNoticeUtils weChatNoticeUtils;

	@Pointcut("@annotation(retry)")
	public void pointcutRetry(Retry retry) {}

	@Around(value = "pointcutRetry(retry)")
	public Object methodAround(ProceedingJoinPoint joinPoint, Retry retry) throws Exception {
		int times = 0;
		int maxRetry = retry.maxRetry();
		Throwable ex = null;
		do {
			String methodName = joinPoint.getSignature().getName();
			try {
				log.info("methodName = {}", methodName);
				return joinPoint.proceed();
			} catch (Throwable throwable) {
				log.error("调用方法name = {} 抛出异常，重试次数为 = {}", methodName, times);
				if (times == maxRetry) {
					weChatNoticeUtils.setMessage(String.format("重试了第 %d 次，仍然报错。", times));
				}
				ex = throwable;
			}
		} while (++times <= maxRetry);
		throw new Exception(ex);
	}
}

















