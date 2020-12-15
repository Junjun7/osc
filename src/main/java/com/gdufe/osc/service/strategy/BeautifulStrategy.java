package com.gdufe.osc.service.strategy;

import com.gdufe.osc.entity.Img;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
	public List<String> getImg(int offset, int limit) {
		offset = convertOffset(limit);
		List<Img> imgs = imgDao.listImgLink(offset, limit);
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
}
