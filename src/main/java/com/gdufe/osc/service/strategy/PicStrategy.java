package com.gdufe.osc.service.strategy;

import com.gdufe.osc.entity.Img;
import com.gdufe.osc.entity.ImgBiZhi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author changwenbo
 * @date 2020/5/15 16:04
 */
@Service(PicStrategy.SERVICE_NAME)
@Slf4j
public class PicStrategy extends ImgTypeStrategy {

	public static final String SERVICE_NAME = "PIC_IMG";

	@Override
	protected String getServiceName() {
		return SERVICE_NAME;
	}

	@Override
	protected List<Img> queryImg(int offset, int limit) {
		List<Img> imgs = new ArrayList<>();
		List<ImgBiZhi> imgBiZhis = imgBiZhiDao.listImgLink(offset, limit);
		for (ImgBiZhi imgBiZhi : imgBiZhis) {
			Img img = new Img();
			BeanUtils.copyProperties(imgBiZhi, img);
			imgs.add(img);
		}
		if (CollectionUtils.isEmpty(imgs)) {
			return imgs;
		}
		return imgs;
	}

	@Override
	protected int getCount() {
		return NumberUtils.toInt(imgBiZhiDao.countImg() + "");
	}
}
