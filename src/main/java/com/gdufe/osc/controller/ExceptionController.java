package com.gdufe.osc.controller;

import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.enums.OscResultEnum;
import com.gdufe.osc.scheduled.CronTaskByFreshToken;
import com.gdufe.osc.utils.WeChatNoticeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
	private CronTaskByFreshToken cronTaskByFreshToken;
	@Autowired
	private WeChatNoticeUtils weChatNoticeUtils;

	@ResponseBody
	@ExceptionHandler
	public OscResult<String> exceptionError(Throwable throwable) {

		// 将整个错误栈打印出来
		String msg = throwable.toString();
		if (StringUtils.isEmpty(msg)) {
			msg = throwable.toString();
		}
		// token失效
		if (throwable instanceof NullPointerException || throwable instanceof IOException) {
			try {
				cronTaskByFreshToken.refreshCache();
			} catch (Exception e) {
				log.error("osc接口出现问题 ==> " + e);
			}
			weChatNoticeUtils.setMessage(msg);
			log.error(throwable + "");
			return new OscResult<String>().fail(OscResultEnum.NETWORK_EXCEPTION);
		}
		// 缺少字段
		if (throwable instanceof IllegalStateException) {
			weChatNoticeUtils.setMessage(msg);
			log.error(throwable + "");
			return new OscResult<String>().fail(OscResultEnum.MISSING_PARAM_EXCEPTION);
		}
		// 其他异常 未知
		weChatNoticeUtils.setMessage(msg);
		log.error(throwable + "");
		return new OscResult<String>().fail();
	}
}
