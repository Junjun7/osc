package com.gdufe.osc.service.impl;

import com.gdufe.osc.dao.DownloadImgDao;
import com.gdufe.osc.entity.DownloadImg;
import com.gdufe.osc.service.ZhiHuSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/4/20 13:05
 */
@Service
public class ZhiHuSpiderImpl implements ZhiHuSpider {

	@Autowired
	private DownloadImgDao downloadImgDao;

	@Override
	public List<DownloadImg> listDownloadImg() {
		return downloadImgDao.listAllDownloadImg();
	}
}








