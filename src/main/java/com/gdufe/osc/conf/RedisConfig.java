package com.gdufe.osc.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * @Author: yizhen
 * @Date: 2018/12/18 12:22
 */
@Configuration
public class RedisConfig {

	@Bean("redisTemplate")
	public <K, V>RedisTemplate<K, V> redisTemplate(
			RedisConnectionFactory redisConnectionFactory) {

		RedisTemplate<K, V> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}
}
