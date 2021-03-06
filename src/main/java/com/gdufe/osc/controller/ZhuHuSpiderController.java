package com.gdufe.osc.controller;

import com.gdufe.osc.annotation.TimeWatch;
import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.entity.DownloadImg;
import com.gdufe.osc.entity.request.ListSpiderImgRequest;
import com.gdufe.osc.enums.ImgTypeEnum;
import com.gdufe.osc.enums.OscResultEnum;
import com.gdufe.osc.factory.ImgFactory;
import com.gdufe.osc.service.ZhiHuSpider;
import com.gdufe.osc.service.processor.IntraProcessor;
import com.gdufe.osc.service.strategy.ImgTypeStrategy;
import com.gdufe.osc.utils.NumberUtils;
import com.gdufe.osc.utils.StrategyHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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
	@Autowired
	private ImgFactory imgFactory;

	@TimeWatch
	@RequestMapping(value = "/process/spider/get", method = RequestMethod.GET)
	public OscResult<List<String>> listSpiderImg(int offset, int limit, String type) {
		ImgTypeStrategy strategy = strategyHelper.getImgTypeStrategy(type);
		log.info("strategy = {}", strategy);
		List<String> imgList = strategy.getImg(limit);
		if (CollectionUtils.isEmpty(imgList)) {
			log.info("list is empty");
			return new OscResult<List<String>>().fail(OscResultEnum.MISSING_RES_EXCEPTION);
		}
		log.info("list success");
		return new OscResult<List<String>>().success(imgList);
	}

	/**
	 * 此方法是上面的V2, 可以学习下下面的技术
	 * 1: 校验参数  @Valid
	 * 2: TimeWatch 打印如参出参 + 执行时间
	 * 3: process是一个通用的方法
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@TimeWatch
	@RequestMapping(value = "/spider/get", method = RequestMethod.GET)
	public OscResult<List<String>> listSpiderImgV2(@Valid ListSpiderImgRequest request) throws Exception {
		ImgTypeEnum imgTypeEnum = ImgTypeEnum.findByValue(NumberUtils.toInt(request.getType()));
		log.info("imgTypeEnum = {}", imgTypeEnum);

		ImgTypeStrategy strategy = imgFactory.getService(imgTypeEnum.name());
		int limit = request.getLimit();

		return IntraProcessor.process(() -> strategy.getImg(limit));
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
	public OscResult<String> executeSpider(String id, int limit) {
		if (StringUtils.isEmpty(id)) {
			return new OscResult<String>().fail(OscResultEnum.MISSING_PARAM_EXCEPTION);
		}
		log.info("id = {}, limit = {}", id, limit);
		zhiHuSpider.spider(id, limit);
		return new OscResult<String>().success(OscResultEnum.RUNNING_SPIDER.getMsg());
	}
}












