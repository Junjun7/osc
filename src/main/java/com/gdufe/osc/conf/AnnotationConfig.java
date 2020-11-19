package com.gdufe.osc.conf;

import lombok.extern.slf4j.Slf4j;
import net.paoding.rose.jade.context.spring.JadeBeanFactoryPostProcessor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author changwenbo
 * @date 2020/7/11 12:29
 */
@Configuration
@EnableScheduling
@EnableCaching
@Slf4j
public class AnnotationConfig {

	@Bean
	public JadeBeanFactoryPostProcessor jadeBeanFactoryPostProcessor() {
		log.info("starting....");
		return new JadeBeanFactoryPostProcessor();
	}

}
