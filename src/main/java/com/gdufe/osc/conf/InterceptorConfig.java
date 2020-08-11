package com.gdufe.osc.conf;

import com.gdufe.osc.interceptor.AccessLimitInterceptor;
import com.gdufe.osc.interceptor.IPBlockInterceptor;
import com.gdufe.osc.interceptor.LogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: yizhen
 * @Date: 2018/12/24 15:45
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Bean
	public AccessLimitInterceptor accessLimitInterceptor() {
		return new AccessLimitInterceptor();
	}
	@Bean
	public IPBlockInterceptor ipBlockInterceptor() {
		return new IPBlockInterceptor();
	}
	@Bean
	public LogInterceptor logInterceptor() {
		return new LogInterceptor();
	}

	// 配置拦截规则
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(logInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/spring/**")
				.excludePathPatterns("/task/**");
	}
}










