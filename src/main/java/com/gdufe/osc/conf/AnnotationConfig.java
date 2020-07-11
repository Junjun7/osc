package com.gdufe.osc.conf;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author changwenbo
 * @date 2020/7/11 12:29
 */
@Configuration
@EnableScheduling
@EnableCaching
public class AnnotationConfig {
}
