package com.gdufe.osc.service.impl;

import com.gdufe.osc.dao.DownloadImgDao;
import com.gdufe.osc.entity.DownloadImg;
import com.gdufe.osc.enums.ImgTypeEnum;
import com.gdufe.osc.exception.NetworkException;
import com.gdufe.osc.scheduled.CronTaskBySpider;
import com.gdufe.osc.service.ZhiHuSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: yizhen
 * @date: 2019/4/20 13:05
 */
@Slf4j
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
	public void spider(String id, int limit) {
		es.submit(() -> {
			try {
				cronTaskBySpider.spider(id, limit == 0 ? 10 : limit, ImgTypeEnum.DOWNLOAD_IMG);
			} catch (NetworkException e) {
				log.error("下载图片失败 e = {}", e);
			}
		});
	}
}








