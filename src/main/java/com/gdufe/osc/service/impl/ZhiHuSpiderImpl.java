package com.gdufe.osc.service.impl;

import com.gdufe.osc.dao.DownloadImgDao;
import com.gdufe.osc.entity.DownloadImg;
import com.gdufe.osc.enums.ImgTypeEnum;
import com.gdufe.osc.scheduled.CronTaskBySpider;
import com.gdufe.osc.service.ZhiHuSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: yizhen
 * @date: 2019/4/20 13:05
 */
@Service
public class ZhiHuSpiderImpl implements ZhiHuSpider {

	@Autowired
	private DownloadImgDao downloadImgDao;
	@Autowired
	private CronTaskBySpider cronTaskBySpider;

	private static final ExecutorService es = Executors.newCachedThreadPool();

	@Override
	public List<DownloadImg> listDownloadImg() {
		return downloadImgDao.listAllDownloadImg();
	}

	@Override
	public void spider(String id) {
		es.submit(() -> cronTaskBySpider.spider(id, "20", ImgTypeEnum.DOWNLOAD_IMG));
	}
}








