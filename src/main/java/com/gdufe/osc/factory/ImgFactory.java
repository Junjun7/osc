package com.gdufe.osc.factory;

import com.gdufe.osc.service.strategy.BeautifulStrategy;
import com.gdufe.osc.service.strategy.ImgTypeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

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

	@PostConstruct
	public void init() {
		log.info("imgTypeStrategyList = {}", imgTypeStrategyList);
	}

	public ImgTypeStrategy getService(String serviceName) {

		return imgTypeStrategyList.stream()
				.filter(service -> serviceName.equalsIgnoreCase(service.getServiceName()))
				.findFirst()
				.orElse(beautifulStrategy);
	}
}
















