package com.gdufe.osc.service.strategy;

import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.entity.Img;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author changwenbo
 * @date 2020/5/15 16:04
 */
@Service
@Slf4j
public class BeautifulStrategy extends ImgTypeStrategy {

	@Autowired
	private ImgDao imgDao;

	@Override
	public Mono<List<String>> getImg(int offset, int limit) {
		offset = convertOffset(limit);
		List<Img> imgs = imgDao.listImgLink(offset, limit);
		if (CollectionUtils.isEmpty(imgs)) {
			return null;
		}
		List<String> res = Lists.newArrayList();
		for (Img img : imgs) {
			res.add(img.getLink());
		}
		return Mono.justOrEmpty(res);
	}

	/** 随机选择图片 */
	private Integer convertOffset(int limit) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int cnt = NumberUtils.toInt(imgDao.countImg() + "");
		int rd = random.nextInt(cnt);
		if (rd > limit) {
			rd -= limit;
		}
		log.info("图片随机位置为：{}", rd);
		return rd;
	}
}
