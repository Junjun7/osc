package com.gdufe.osc.controller;

import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.enums.OscResultEnum;
import com.gdufe.osc.service.ZhiHuSpider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/4/20 14:44
 */
@RestController
@RequestMapping("/zhihu")
public class ZhuHuSpiderController {

	@Autowired
	private ZhiHuSpider zhiHuSpider;

	@RequestMapping(value = "/spider/get", method = RequestMethod.GET)
	public OscResult<List<String>> listSpiderImg(Integer offset, Integer limit, String type) {
		if (StringUtils.isEmpty(type)) {
			type = "1";
		}
		List<String> imgList = zhiHuSpider.getImg(offset, limit, type);
		if (CollectionUtils.isEmpty(imgList)) {
			new OscResult<List<String>>().fail(OscResultEnum.MISSING_RES_EXCEPTION);
		}
		return new OscResult<List<String>>().success(imgList);
	}
}












