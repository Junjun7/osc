package com.gdufe.osc.controller;

import com.gdufe.osc.entity.AccessToken;
import com.gdufe.osc.entity.Person;
import com.gdufe.osc.exception.NetworkException;
import com.gdufe.osc.scheduled.CronTaskBySpider;
import com.gdufe.osc.service.RedisHelper;
import com.gdufe.osc.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:49
 */
@Slf4j
@RestController
@RequestMapping("/prefix")
public class TestController {

	@Autowired
	private RedisHelper<AccessToken> redisHelper;
	@Autowired
	private CronTaskBySpider cronTaskBySpider;

	@RequestMapping(value = "/")
	public String index() {
		log.info("Test");
		return "别扫我接口，谢谢！！！";
	}

	@RequestMapping("/img/url")
	public String getImgUrl() {
//		return "https://pic1.zhimg.com/80/v2-09716650530b30759143589279cccd44_hd.jpg";
		// 二维码和大打赏
		return "https://pic4.zhimg.com/50/v2-469b1192443fa2aa7359e92b625bad42_r.jpg";
	}

	@RequestMapping("/zhihu")
	public String zhiHu() throws InterruptedException, NetworkException {
		cronTaskBySpider.imgSpider();
		return "ok";
	}

	@RequestMapping(value = "/timestamp")
	public Long getTimeStamp() {
		return System.currentTimeMillis();
	}

	@RequestMapping("/token")
	public String getToken() {
		return TokenUtils.getToken();
	}

	@RequestMapping("/freshtoken")
	public String getFreshToken() {

		return TokenUtils.getFreshToken();
	}

	@RequestMapping("/getKey")
	public AccessToken getAccess(String key) {

		return redisHelper.get(key, AccessToken.class);
	}

	@RequestMapping("/setKey")
	public Boolean setAccess(String key) {

		AccessToken accessToken = new AccessToken();
		return redisHelper.setEx(key, 30L, accessToken);
	}

	@RequestMapping("/person")
	public void testValidate(@Valid Person person) {
		log.info("person = " + person);
	}
}








