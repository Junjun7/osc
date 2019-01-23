package com.gdufe.osc.common;

import com.gdufe.osc.enums.OscResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: yizhen
 * @Date: 2018/12/7 18:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OscResult<T> {

	/**
	 * 请求是否成功
	 */
	private boolean success;

	/**
	 * 请求返回的详细信息
	 */
	private String message;

	/**
	 * 返回的状态
	 */
	private String code;

	/**
	 * 具体返回对象
	 */
	private T data;

	public OscResult<T> success(T data) {
		OscResult<T> res = new OscResult<>();
		res.setCode(OscResultEnum.SUCCESS.getCode());
		res.setSuccess(true);
		res.setMessage(OscResultEnum.SUCCESS.getMsg());
		res.setData(data);
		return res;
	}

	public OscResult<T> fail() {
		OscResult<T> res = getFail(OscResultEnum.OTHER_EXCEPTION);
		return res;
	}

	public OscResult<T> fail(OscResultEnum oscResultEnum) {
		OscResult<T> res = getFail(oscResultEnum);
		res.setData(data);
		return res;
	}

	private OscResult<T> getFail(OscResultEnum enums) {
		OscResult<T> res = new OscResult<>();
		res.setCode(enums.getCode());
		res.setMessage(enums.getMsg());
		res.setSuccess(false);
		return res;
	}
}
