package com.gdufe.osc.init;

import com.gdufe.osc.exception.NetworkException;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:34
 */
@Slf4j
@Component
public class ZookeeperInitializer implements InitializingBean {

	@Autowired
	private CuratorFramework client;

	@Override
	public void afterPropertiesSet() {
		try {
			init();
		} catch (Exception e) {
			log.error("init error e = {}", e);
		}
	}

	private void init() throws NetworkException {
		log.info("client = {}", client);
	}
}
