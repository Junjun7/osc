package com.gdufe.osc.utils;

import com.gdufe.osc.enums.ImgTypeEnum;
import com.gdufe.osc.service.strategy.BeautifulStrategy;
import com.gdufe.osc.service.strategy.ImgTypeStrategy;
import com.gdufe.osc.service.strategy.PicStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author changwenbo
 * @date 2020/5/15 16:31
 */
@Component
public class StrategyHelper {

	@Autowired
	private BeautifulStrategy beautifulStrategy;
	@Autowired
	private PicStrategy picStrategy;

	public ImgTypeStrategy getImgTypeStrategy(String type) {
		ImgTypeEnum imgTypeEnum = ImgTypeEnum.findByValue(NumberUtils.toInt(type));
		switch (imgTypeEnum) {
			case BEAUTIFUL_IMG:
				return beautifulStrategy;
			case PIC_IMG:
				return picStrategy;
			default:
				return beautifulStrategy;
		}
	}
}
