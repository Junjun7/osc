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

//	@Value("${redis_password}")
//	private String password;
//
//	@Value("${redis_port}")
//	private String port;
//
//	@Autowired
//	private LettuceConnectionFactory lettuceConnectionFactory;

	@Bean("redisTemplate")
	public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<K, V> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}

	// 可以用以下方式换取redis默认工厂。
	// 只要返回RedisTemplate就行，springboot并不关心你怎么实现的。
	//	@Bean("redisTemplate")
//	public <K, V> RedisTemplate<K, V> redisTemplate() {
//		RedisStandaloneConfiguration standaloneConfiguration = lettuceConnectionFactory.getStandaloneConfiguration();
//		standaloneConfiguration.setPort(NumberUtils.toInt(port, 0));
//		standaloneConfiguration.setPassword(password);
//		RedisTemplate<K, V> template = new RedisTemplate<>();
//		template.setConnectionFactory(lettuceConnectionFactory);
//		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//		return template;
//	}
}
