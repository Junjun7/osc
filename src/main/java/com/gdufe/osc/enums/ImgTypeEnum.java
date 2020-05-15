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
}
