package com.gdufe.osc.exception;

import com.gdufe.osc.enums.OscResultEnum;
import com.gdufe.osc.utils.NumberUtils;

/**
 * @author changwenbo
 * @date 2020/7/31 16:19
 */
public class NetworkException extends Exception {

	private int code;
	private String msg;

	public NetworkException(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public NetworkException(OscResultEnum ore) {
		super(ore.getMsg());
		this.code = NumberUtils.toInt(ore.getCode());
		this.msg = ore.getMsg();
	}
}
