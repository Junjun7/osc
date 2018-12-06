package com.gdufe.osc.web;

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
	TweetListService tweetListService;

	@PostMapping("/newest")
	public List<TweetListDetails> getNewestTweetList(int page, int pageSize) {

		return tweetListService.getTweetList(page, pageSize, 0);
	}

	@PostMapping("/hotest")
	public List<TweetListDetails> getHotestTweetList(int page, int pageSize) {

		return tweetListService.getTweetList(page, pageSize, -1);
	}
}
