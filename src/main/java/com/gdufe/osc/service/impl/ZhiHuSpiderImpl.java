package com.gdufe.osc.service.impl;

import com.gdufe.osc.dao.DownloadImgDao;
import com.gdufe.osc.dao.ImgBiZhiDao;
import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.entity.DownloadImg;
import com.gdufe.osc.entity.Img;
import com.gdufe.osc.entity.ImgBiZhi;
import com.gdufe.osc.service.ZhiHuSpider;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: yizhen
 * @date: 2019/4/20 13:05
 */
@Service
@Slf4j
public class ZhiHuSpiderImpl implements ZhiHuSpider {

	@Autowired
	private ImgDao imgDao;
	@Autowired
	private ImgBiZhiDao imgBiZhiDao;
	@Autowired
	private DownloadImgDao downloadImgDao;

	@Override
	public List<String> getImg(int offset, int limit, String type) {
		offset = convertOffset(limit, type);
		List<Img> imgs = Lists.newArrayList();
		if ("1".equals(type)) {
			imgs = imgDao.listImgLink(offset, limit);
		} else if ("2".equals(type)) {
			List<ImgBiZhi> imgBiZhis = imgBiZhiDao.listImgLink(offset, limit);
			for (ImgBiZhi imgBiZhi : imgBiZhis) {
				Img img = new Img();
				BeanUtils.copyProperties(imgBiZhi, img);
				imgs.add(img);
			}
		}
		if (CollectionUtils.isEmpty(imgs)) {
			return null;
		}
		List<String> res = Lists.newArrayList();
		for (Img img : imgs) {
			res.add(img.getLink());
		}
		return res;
	}

	/** 随机选择图片 */
	private Integer convertOffset(int limit, String type) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int cnt = 1;
		if ("1".equals(type)) {
			cnt = Integer.parseInt(imgDao.countImg().toString());
		} else if ("2".equals(type)) {
			cnt = NumberUtils.toInt(imgBiZhiDao.countImg() + "");
		}
		int rd = random.nextInt(cnt);
		if (rd > limit) {
			rd -= limit;
		}
		log.info("图片随机位置为：{}", rd);
		return rd;
	}

	@Override
	public List<DownloadImg> listDownloadImg() {
		return downloadImgDao.listAllDownloadImg();
	}
}








