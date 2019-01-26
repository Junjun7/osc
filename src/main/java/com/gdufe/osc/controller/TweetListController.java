package com.gdufe.osc.controller;

import com.gdufe.osc.annotation.TimeWatch;
import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.entity.TweetListDetails;
import com.gdufe.osc.enums.OscResultEnum;
import com.gdufe.osc.enums.TweetCodeEnum;
import com.gdufe.osc.service.TweetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(value = "/newest", method = RequestMethod.GET)
	public OscResult<List<TweetListDetails>> getNewestTweetList(int page, int pageSize) {
		List<TweetListDetails> details =
				tweetListService.getTweetList(page, pageSize, TweetCodeEnum.NEWEST_TWEET_CODE.getCode());
		return new OscResult<List<TweetListDetails>>().success(details);
	}

	@RequestMapping(value = "/hotest", method = RequestMethod.GET)
	public OscResult<List<TweetListDetails>> getHotestTweetList(int page, int pageSize) {
		List<TweetListDetails> details =
				tweetListService.getTweetList(page, pageSize, TweetCodeEnum.HOTEST_TWEET_CODE.getCode());
		return new OscResult<List<TweetListDetails>>().success(details);
	}

	@TimeWatch
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public OscResult<List<TweetListDetails>> getUsersTweetList(
			int page, int pageSize, @PathVariable(value = "userId", required = false) String userId) {
		if (userId == null) {
			return new OscResult<List<TweetListDetails>>().fail(OscResultEnum.MISSING_PARAM_EXCEPTION);
		}
		List<TweetListDetails> details =
				tweetListService.getTweetList(page, pageSize, userId);
		return new OscResult<List<TweetListDetails>>().success(details);
	}
}
