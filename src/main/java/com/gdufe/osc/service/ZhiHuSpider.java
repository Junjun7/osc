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
			// 身材好是一种怎样的体验？
			"26037846",
			// 你见过的有些人能漂亮到什么程度？
			"266808424");
}
