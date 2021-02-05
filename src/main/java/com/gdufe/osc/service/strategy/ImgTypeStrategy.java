package com.gdufe.osc.service.strategy;

import com.gdufe.osc.dao.ImgBiZhiDao;
import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.entity.Img;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

	public final List<String> getImg(int offset, int limit) {

		offset = convertOffset(limit);
		List<Img> imgs = queryImg(offset, limit);
		List<String> res = new ArrayList<>();
		for (Img img : imgs) {
			res.add(img.getLink());
		}
		return res;
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
