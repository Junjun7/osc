package com.gdufe.osc.controller;

import com.gdufe.osc.entity.AccessToken;
import com.gdufe.osc.service.RedisHelper;
import com.gdufe.osc.service.ZhiHuSpider;
import com.gdufe.osc.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:49
 */
@RestController
@RequestMapping("/prefix")
public class TestController {

	@Autowired
	private RedisHelper<AccessToken> redisHelper;
	@Autowired
	private ZhiHuSpider zhiHuSpider;

	@RequestMapping(value = "/")
	public String index() {
		return "我日，竟然刷我接口，我靠！！！";
	}

	@RequestMapping("/img/url")
	public String getImgUrl() {
		return "https://pic1.zhimg.com/50/v2-f1e76ff4de42e7af05b2ff9d79781ab5_r.jpg";
	}

	@RequestMapping("/zhihu")
	public String zhiHu() {
		zhiHuSpider.imgSpider();
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
}








