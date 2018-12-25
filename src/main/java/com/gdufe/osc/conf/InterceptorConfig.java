package com.gdufe.osc.conf;

import com.gdufe.osc.interceptor.BlockListInterceptor;
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
	public BlockListInterceptor blockListInterceptor() {
		return new BlockListInterceptor();
	}

	// 配置拦截规则
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(blockListInterceptor())
				.addPathPatterns("/test/**")
				.excludePathPatterns("/test/token/**");
	}
}










