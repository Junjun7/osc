package com.gdufe.osc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yizhen
 * @date: 2019/1/23 22:21
 */
@Getter
@AllArgsConstructor
public enum OscResultEnum {

	SUCCESS("200", "请求成功"),
	MISSING_PARAM_EXCEPTION("400", "缺少参数"),
	MISSING_RES_EXCEPTION("401", "缺少返回结果"),
	NETWORK_EXCEPTION("500", "网络出错"),
	LIMIT_EXCEPTION("501", "请不要刷接口"),
	OTHER_EXCEPTION("502", "其他问题");

	private String code;
	private String msg;
}
