package com.gdufe.osc.init;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:34
 */
@Slf4j
@Component
public class ZookeeperSentinelConfigInitializer implements InitializingBean {

	@Value("zk.host")
	private String url;

	private String path = "/workspace/osc/sentinel/rules";

	@Override
	public void afterPropertiesSet() {
		try {
			init();
		} catch (Exception e) {
			log.error("init error e = {}", e);
		}
	}

	private void init() throws Exception {

		log.info("zk url = {}", url);

		ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(url, path,
				source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
				}));
		FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
	}
}
