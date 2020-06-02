package com.gdufe.osc.service;

import com.gdufe.osc.entity.DownloadImg;

import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/4/20 13:07
 */
public interface ZhiHuSpider {

	List<DownloadImg> listDownloadImg();

	void spider(String id);
}
