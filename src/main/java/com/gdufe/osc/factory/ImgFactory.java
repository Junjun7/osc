package com.gdufe.osc.factory;

import com.gdufe.osc.service.strategy.BeautifulStrategy;
import com.gdufe.osc.service.strategy.ImgTypeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author changwenbo
 * @date 2020/9/18 17:21
 */
@Service
@Slf4j
public class ImgFactory {

	@Autowired
	private BeautifulStrategy beautifulStrategy;

	@Autowired
	private List<ImgTypeStrategy> imgTypeStrategyList;

	@Autowired
	private Map<String, ImgTypeStrategy> imgTypeStrategyMap;

	@PostConstruct
	public void init() {
		log.info("imgTypeStrategyList = {}", imgTypeStrategyList);
		log.info("imgTypeStrategyMap = {}", imgTypeStrategyMap);
	}

	public ImgTypeStrategy getService(String serviceName) {
		return imgTypeStrategyMap.getOrDefault(serviceName, beautifulStrategy);
	}
}
















