package com.gdufe.osc.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Component;

/**
 * @author changwenbo
 * @date 2020/11/17 14:46
 */
@Component
@Slf4j
public class ZKUtils {

	private static CuratorFramework client = null;

	static {
		log.info("init client = {}", client);
		connectToZkServer();
		registryNodeCache();
		log.info("init success = {}", client);
	}

	public static boolean connectToZkServer() {
		client = CuratorFrameworkFactory.builder()
				.connectString("120.78.159.149")
				.retryPolicy(new ExponentialBackoffRetry(1000,3))
				.sessionTimeoutMs(50000)
				.connectionTimeoutMs(50000)
				.build();
		client.start();
		if (!client.getZookeeperClient().isConnected()) {
			log.info("zookeeper连接中......");
		} else {
			log.info("zookeeper连接成功......");
		}
		return true;
	}

	public static void registryNodeCache() {
		final NodeCache nodeCache = new NodeCache(client, "/test/test2");
		try {
			// 如果设置为true,则会缓存当前节点信息，否则只有在节点改变时才会获取节点数据
			nodeCache.start(false);
			nodeCache.getListenable().addListener(new NodeCacheListener() {
				// 这里监听的是节点的增删查改，而不是event事件
				@Override
				public void nodeChanged() throws Exception {
					log.info("当前节点 = {}", new String(nodeCache.getCurrentData().getData()));

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
