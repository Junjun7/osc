package com.gdufe.osc.service.strategy;

import com.gdufe.osc.dao.ImgBiZhiDao;
import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.enums.ImgTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public abstract List<String> getImg(int offset, int limit);

	public abstract String getServiceName();

	/** 随机选择图片 */
	protected int convertOffset(int limit) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int cnt = getCount();
		int rd = random.nextInt(cnt);
		if (rd > limit) {
			rd -= limit;
		}
		log.info("图片随机位置为：{}", rd);
		return rd;
	}

	private int getCount() {
		String serviceName = getServiceName();
		if (ImgTypeEnum.BEAUTIFUL_IMG.name().equalsIgnoreCase(serviceName)) {
			return NumberUtils.toInt(imgDao.countImg() + "");
		}
		if (ImgTypeEnum.PIC_IMG.name().equalsIgnoreCase(serviceName)) {
			return NumberUtils.toInt(imgBiZhiDao.countImg() + "");
		}
		return NumberUtils.toInt(imgDao.countImg() + "");
	}
}
