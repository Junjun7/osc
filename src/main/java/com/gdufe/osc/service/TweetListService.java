package com.gdufe.osc.service;

import com.gdufe.osc.entity.TweetListDetails;
import com.gdufe.osc.utils.CacheToken;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 14:45
 */
public interface TweetListService {

	/** 获取所有tweet的信息 */
	List<TweetListDetails> listTweetList(int page, int pageSize, String user);

	/** 根据tweetId获取tweet的信息 */
	TweetListDetails getTweetList(String tweetId);

	default String getIdsUrl(RedisService redisService) {
		String token = CacheToken.getToken();
		if (StringUtils.isEmpty(token)) {
			token = redisService.getToken();
		}
		return "https://www.oschina.net/action/openapi/tweet_list?dataType=json&access_token=" + token;
	}

	default String getDetailsUrl(RedisService redisService) {
		String token = CacheToken.getToken();
		if (StringUtils.isEmpty(token)) {
			token = redisService.getToken();
		}
		return "https://www.oschina.net/action/openapi/tweet_detail?dataType=json&access_token=" + token;
	}
}
