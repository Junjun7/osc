package com.gdufe.osc.service.strategy;

import com.gdufe.osc.dao.ImgBiZhiDao;
import com.gdufe.osc.entity.Img;
import com.gdufe.osc.entity.ImgBiZhi;
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
 * @author changwenbo
 * @date 2020/5/15 16:04
 */
@Service
@Slf4j
public class PicStrategy extends ImgTypeStrategy {

	private static final String SERVICE_NAME = "PIC_IMG";

	@Autowired
	private ImgBiZhiDao imgBiZhiDao;

	@Override
	public List<String> getImg(int offset, int limit) {
		offset = convertOffset(limit);
		List<Img> imgs = Lists.newArrayList();
		List<ImgBiZhi> imgBiZhis = imgBiZhiDao.listImgLink(offset, limit);
		for (ImgBiZhi imgBiZhi : imgBiZhis) {
			Img img = new Img();
			BeanUtils.copyProperties(imgBiZhi, img);
			imgs.add(img);
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

	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}

	/** 随机选择图片 */
	private Integer convertOffset(int limit) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int cnt = NumberUtils.toInt(imgBiZhiDao.countImg() + "");
		int rd = random.nextInt(cnt);
		if (rd > limit) {
			rd -= limit;
		}
		log.info("图片随机位置为：{}", rd);
		return rd;
	}
}
