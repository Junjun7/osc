package com.gdufe.osc.common;

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
	private int code;

	/**
	 * 具体返回对象
	 */
	private T data;

	public OscResult<T> success(T data) {

		OscResult<T> res = new OscResult<>();
		res.setCode(200);
		res.setSuccess(true);
		res.setMessage("请求成功");
		res.setData(data);
		return res;
	}

	public OscResult<T> fail() {

		OscResult<T> res = getFail();
		return res;
	}

	public OscResult<T> fail(T data) {
		OscResult<T> res = getFail();
		res.setData(data);
		return res;
	}

	private OscResult<T> getFail() {
		OscResult<T> res = new OscResult<>();
		res.setCode(500);
		res.setSuccess(false);
		res.setMessage("请求失败");
		return res;
	}
}
