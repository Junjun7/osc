package com.gdufe.osc.service;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/4/20 13:07
 */
public interface ZhiHuSpider {

	List<String> getImg(Integer offset, Integer limit);

	void imgSpider();

	List<String> ids = Lists.newArrayList(
			"297715922",
			"291678281",
			"275359100",
			"315236887",
			"26297181",
			"273647787");
}
