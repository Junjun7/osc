package com.gdufe.osc.service;

import com.gdufe.osc.entity.DownloadImg;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/4/20 13:07
 */
public interface ZhiHuSpider {

	/**
	 * type == 1  等于知乎美女图片
	 * type == 2  等于知乎壁纸
	 * @param offset
	 * @param limit
	 * @param type
	 * @return
	 */
	List<String> getImg(Integer offset, Integer limit, String type);

	List<DownloadImg> listDownloadImg();
}
