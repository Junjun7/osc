package com.gdufe.osc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yizhen
 * @date: 2019/1/23 15:48
 */
@Getter
@AllArgsConstructor
public enum ImgTypeEnum {

	BEAUTIFUL_IMG("1", "美女图片"),
	PIC_IMG("2", "壁纸图片");

	private String type;
	private String msg;

	public static ImgTypeEnum findByValue(int type) {
		switch (type) {
			case 1:
				return BEAUTIFUL_IMG;
			case 2:
				return PIC_IMG;
			default:
				return BEAUTIFUL_IMG;
		}
	}
}
