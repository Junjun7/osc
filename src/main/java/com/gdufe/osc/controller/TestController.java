package com.gdufe.osc.controller;

import com.gdufe.osc.entity.AccessToken;
import com.gdufe.osc.service.RedisHelper;
import com.gdufe.osc.service.TweetListService;
import com.gdufe.osc.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:49
 */
@RestController
public class TestController {

	@Autowired
	private TweetListService tweetListService;
	@Autowired
	private RedisHelper<AccessToken> redisHelper;

	@GetMapping("/")
	public String index() {
		return "Hello Gdufe";
	}

	@GetMapping("/token")
	public String getToken() {
		return TokenUtils.getToken();
	}

	@GetMapping("/freshtoken")
	public String getFreshToken() {

		return TokenUtils.getFreshToken();
	}

	@GetMapping("/getKey")
	public AccessToken getAccess(String key) {

		return redisHelper.get(key, AccessToken.class);
	}

	@GetMapping("/setKey")
	public Boolean setAccess(String key) {

		AccessToken accessToken = new AccessToken();
		return redisHelper.setEx(key, 30L, accessToken);
	}
}








