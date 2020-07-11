package com.gdufe.osc.controller;

import com.gdufe.osc.annotation.TimeWatch;
import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.entity.DownloadImg;
import com.gdufe.osc.enums.OscResultEnum;
import com.gdufe.osc.service.ZhiHuSpider;
import com.gdufe.osc.service.strategy.ImgTypeStrategy;
import com.gdufe.osc.utils.StrategyHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: yizhen
 * @date: 2019/4/20 14:44
 */
@Slf4j
@RestController
@RequestMapping("/zhihu")
public class ZhuHuSpiderController {

	@Autowired
	private ZhiHuSpider zhiHuSpider;
	@Autowired
	private StrategyHelper strategyHelper;

//	@RequestMapping(value = "/spider/get", method = RequestMethod.GET)
//	public OscResult<List<String>> listSpiderImg(int offset, int limit, String type) {
//		ImgTypeStrategy strategy = strategyHelper.getImgTypeStrategy(type);
//		List<String> imgList = strategy.getImg(offset, limit);
//		if (CollectionUtils.isEmpty(imgList)) {
//			return new OscResult<List<String>>().fail(OscResultEnum.MISSING_RES_EXCEPTION);
//		}
//		return new OscResult<List<String>>().success(imgList);
//	}

	@RequestMapping(value = "/spider/get", method = RequestMethod.GET)
	public Mono<List<String>> listSpiderImgFlux(int offset, int limit, String type) {
		ImgTypeStrategy strategy = strategyHelper.getImgTypeStrategy(type);
		Mono<List<String>> mono = strategy.getImg(offset, limit);
		if (mono == null) {
			return mono.justOrEmpty(Optional.empty());
		}
		mono.doOnNext((imgList) -> {
			imgList.forEach((img) -> {
				List<String> res = new ArrayList<>();
				if (img.indexOf("http") != -1) {
					// 业务逻辑
					res.add(img);
					log.info("x = {}", img);
				}
			});
		}).subscribe();
		return mono;
	}

	@RequestMapping(value = "/download/img", method = RequestMethod.GET)
	public OscResult<List<DownloadImg>> listDownloadImg() {
		List<DownloadImg> downloadImgs = zhiHuSpider.listDownloadImg();
		if (CollectionUtils.isEmpty(downloadImgs)) {
			return new OscResult<List<DownloadImg>>().fail(OscResultEnum.MISSING_RES_EXCEPTION);
		}
		return new OscResult<List<DownloadImg>>().success(downloadImgs);
	}

	@TimeWatch
	@RequestMapping(value = "/download/spider", method = RequestMethod.GET)
	public OscResult<String> executeSpider(String id) {
		if (StringUtils.isEmpty(id)) {
			return new OscResult<String>().fail(OscResultEnum.MISSING_PARAM_EXCEPTION);
		}
		log.info("id = {}", id);
		zhiHuSpider.spider(id);
		return new OscResult<String>().success(OscResultEnum.RUNNING_SPIDER.getMsg());
	}
}












