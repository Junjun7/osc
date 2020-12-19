package com.gdufe.osc.conf;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "zk")
public class ZookeeperConfig {

	private String host;

	private int maxRetry;

	private int sessionTimeout;

	private int connectTimeout;

	@Bean
	public CuratorFramework curatorFramework() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 1);
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(host)
				.sessionTimeoutMs(sessionTimeout)
				.connectionTimeoutMs(connectTimeout)
				.retryPolicy(retryPolicy)
				.build();
		client.start();
		log.info("start zookeeper client = {}", client);
		return client;
	}

	@PreDestroy
	private void destroyClient(){
		curatorFramework().close();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getMaxRetry() {
		return maxRetry;
	}

	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
}
