package com.gdufe.osc.factory;

import com.gdufe.osc.service.strategy.BeautifulStrategy;
import com.gdufe.osc.service.strategy.ImgTypeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author changwenbo
 * @date 2020/9/18 17:21
 */
@Service
@Slf4j
public class ImgFactory {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private BeautifulStrategy beautifulStrategy;

	private Map<String, ImgTypeStrategy> serviceMap;

	@PostConstruct
	public void init() {
		Map<String, ImgTypeStrategy> serviceMaps = applicationContext.getBeansOfType(ImgTypeStrategy.class);
		if (CollectionUtils.isEmpty(serviceMaps)) {
			return;
		}
		log.info("serviceMaps = {}", serviceMaps);
		serviceMap = new HashMap<>(2);

		serviceMaps.forEach((name, service) -> serviceMap.put(service.getServiceName(), service));
	}

	public ImgTypeStrategy getService(String serviceName) {
		return serviceMap.getOrDefault(serviceName, beautifulStrategy);
	}
}
















