package com.gdufe.osc.service.strategy;

import com.gdufe.osc.entity.Img;
import com.gdufe.osc.entity.ImgBiZhi;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
}
