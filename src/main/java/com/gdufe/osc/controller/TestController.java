package com.gdufe.osc.controller;

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
	TweetListService tweetListService;

	@GetMapping("/token")
	public String getToken() {
		return TokenUtils.getToken();
	}

	@GetMapping("/freshtoken")
	public String getFreshToken() {

		return TokenUtils.getFreshToken();
	}
}
