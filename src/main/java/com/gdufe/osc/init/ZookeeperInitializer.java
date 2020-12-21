package com.gdufe.osc.init;

import com.gdufe.osc.utils.CacheToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
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

	private void init() throws Exception {
		final NodeCache nodeCache = new NodeCache(client, "/workspace/osc/cookie");
		// 如果设置为true,则会缓存当前节点信息，否则只有在节点改变时才会获取节点数据
		nodeCache.start(false);
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			// 这里监听的是节点的增删查改，而不是event事件
			@Override
			public void nodeChanged() throws Exception {
				String ck = new String(nodeCache.getCurrentData().getData());
				ck = ck.replaceAll("\\*", "\"");
				log.info("当前节点 = {}", ck);
				if (StringUtils.isNotEmpty(ck)) {
					CacheToken.putCK(ck);
				}
			}
		});
	}
}
