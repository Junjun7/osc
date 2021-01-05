package com.gdufe.osc.service.strategy;

import com.gdufe.osc.entity.Img;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author changwenbo
 * @date 2020/5/15 16:04
 */
@Slf4j
@Service(BeautifulStrategy.SERVICE_NAME)
public class BeautifulStrategy extends ImgTypeStrategy {

	public static final String SERVICE_NAME = "BEAUTIFUL_IMG";

	@Override
	protected String getServiceName() {
		return SERVICE_NAME;
	}

	@Override
	protected List<Img> queryImg(int offset, int limit) {
		List<Img> imgs = imgDao.listImgLink(offset, limit);
		if (CollectionUtils.isEmpty(imgs)) {
			return Collections.EMPTY_LIST;
		}
		return imgs;
	}

	@Override
	protected int getCount() {
		return NumberUtils.toInt(imgDao.countImg() + "");
	}
}
