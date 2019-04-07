package com.gdufe.osc.controller;

import com.gdufe.osc.annotation.TimeWatch;
import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.entity.TweetListDetails;
import com.gdufe.osc.enums.OscResultEnum;
import com.gdufe.osc.enums.TweetCodeEnum;
import com.gdufe.osc.service.TweetListService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 17:58
 */
@RestController
@RequestMapping("/prefix/api/tweet")
public class TweetListController {

	@Autowired
	private TweetListService tweetListService;

	@RequestMapping(value = "/newest", method = RequestMethod.GET)
	public OscResult<List<TweetListDetails>> listNewestTweetList(Integer page, Integer pageSize) {
		List<TweetListDetails> details =
				tweetListService.listTweetList(page, pageSize, TweetCodeEnum.NEWEST_TWEET_CODE.getCode());
		return toRes(details);
	}

	@RequestMapping(value = "/hotest", method = RequestMethod.GET)
	public OscResult<List<TweetListDetails>> listHotestTweetList(Integer page, Integer pageSize) {
		List<TweetListDetails> details =
				tweetListService.listTweetList(page, pageSize, TweetCodeEnum.HOTEST_TWEET_CODE.getCode());
		return toRes(details);
	}

	@TimeWatch
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public OscResult<List<TweetListDetails>> listUsersTweetList(
			Integer page, Integer pageSize,
			@PathVariable(value = "userId", required = false) String userId) {
		if (StringUtils.isEmpty(userId)) {
			return new OscResult<List<TweetListDetails>>().fail(OscResultEnum.MISSING_PARAM_EXCEPTION);
		}
		List<TweetListDetails> details =
				tweetListService.listTweetList(page, pageSize, userId);
		return toRes(details);
	}

	@TimeWatch
	@RequestMapping(value = "/single/{tweetId}", method = RequestMethod.GET)
	public OscResult<TweetListDetails> getSingleTweetList(
			@PathVariable(value = "tweetId", required = false) String tweetId) {
		if (StringUtils.isEmpty(tweetId)) {
			return new OscResult<TweetListDetails>().fail(OscResultEnum.MISSING_PARAM_EXCEPTION);
		}
		TweetListDetails tweetList = tweetListService.getTweetList(tweetId);
		return toRes(tweetList);
	}

	private <T> OscResult<T> toRes(T t) {
		if (Objects.isNull(t)) {
			return new OscResult<T>().fail(OscResultEnum.MISSING_RES_EXCEPTION);
		}
		return new OscResult<T>().success(t);
	}
}
