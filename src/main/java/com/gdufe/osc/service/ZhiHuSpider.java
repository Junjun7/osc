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
			// 女生皮肤白是一种什么样的体验？
			"268207152",
			// 你认识的美女有多美？
			"37608302",
			// 作为女大学生，你的日常穿搭是什么？
			"317964300",
			// 平常人可以漂亮到什么程度？
			"50426133");
}
