package com.gdufe.osc.service.strategy;

import com.gdufe.osc.dao.ImgBiZhiDao;
import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.entity.Img;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author changwenbo
 * @date 2020/5/15 16:02
 */
@Service
@Slf4j
public abstract class ImgTypeStrategy {

	@Autowired
	protected ImgDao imgDao;

	@Autowired
	protected ImgBiZhiDao imgBiZhiDao;

	protected abstract String getServiceName();

	protected abstract List<Img> queryImg(int offset, int limit);

	protected abstract int getCount();

	public final List<String> getImg(int limit) {

		List<Img> imgEntity = queryImg(convertOffset(limit), limit);
		return imgEntity.stream().map(img -> img.getLink())
				.collect(Collectors.toList());

	}

	/** 随机选择图片 */
	private int convertOffset(int limit) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int cnt = getCount();
		int rd = random.nextInt(cnt);
		if (rd > limit) {
			rd -= limit;
		}
		log.info("图片随机位置为：{}", rd);
		return rd;
	}
}
