package com.gdufe.osc.controller;

import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.entity.TweetListDetails;
import com.gdufe.osc.service.TweetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 17:58
 */
@RestController
@RequestMapping("/api/tweet")
public class TweetListController {

	@Autowired
	private TweetListService tweetListService;

	@PostMapping("/newest")
	public OscResult<List<TweetListDetails>> getNewestTweetList(int page, int pageSize) {

		List<TweetListDetails> details = tweetListService.getTweetList(page, pageSize, 0);
		return new OscResult<List<TweetListDetails>>().success(details);
	}

	@PostMapping("/hotest")
	public OscResult<List<TweetListDetails>> getHotestTweetList(int page, int pageSize) {

		List<TweetListDetails> details = tweetListService.getTweetList(page, pageSize, -1);
		return new OscResult<List<TweetListDetails>>().success(details);
	}
}
