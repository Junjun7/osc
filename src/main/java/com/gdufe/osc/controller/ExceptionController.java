package com.gdufe.osc.controller;

import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.scheduled.CronTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @Author: yizhen
 * @Date: 2018/12/7 16:27
 */
@ControllerAdvice
@Slf4j
public class ExceptionController {

	@Autowired
	CronTask cronTask;

	@ResponseBody
	@ExceptionHandler
	public OscResult<String> exceptionError(Throwable throwable) {

		if (throwable instanceof NullPointerException || throwable instanceof IOException) {
			cronTask.refreshCache();
			return new OscResult<String>().fail("网络出错，请刷新");
		}
		String msg = throwable.getMessage();
		log.error(msg);
		OscResult<String> res = new OscResult<String>().fail(msg);
		return res;
	}
}
