package com.gdufe.osc.controller;

import com.gdufe.osc.common.OscResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: yizhen
 * @Date: 2018/12/7 16:27
 */
@ControllerAdvice
public class ExceptionController {

	@ResponseBody
	@ExceptionHandler
	public OscResult<String> exceptionError(Throwable throwable) {

		String msg = throwable.toString();
		OscResult<String> res = new OscResult<String>().fail(msg);
		return res;
	}
}
