package com.gdufe.osc.service.impl;

import com.alibaba.fastjson.JSON;
import com.gdufe.osc.entity.TweetList;
import com.gdufe.osc.entity.TweetListMore;
import com.gdufe.osc.service.TweetListService;
import com.gdufe.osc.utils.CacheToken;
import com.gdufe.osc.utils.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 14:45
 */
@Service
@Slf4j
public class TweetListServiceImp implements TweetListService {

	private static String url = "https://www.oschina.net/action/openapi/tweet_list?dataType=json";

	@Override
	public List<TweetList> getTweetList(int page, int pageSize, int user) {
		String tweetUrl = getTweetUrl(page, pageSize, user);
		String data = HttpMethod.get(tweetUrl);
		TweetListMore tweetListMore = JSON.parseObject(data, TweetListMore.class);
		List<TweetList> lists = tweetListMore.getTweetlist();
		return lists;
	}

	private String getTweetUrl(int page, int pageSize, int user) {

		String token = CacheToken.getToken();
		String tweetUrl = url + "&access_token=" + token;
		tweetUrl += "&user=" + user + "&pageSize=" + pageSize + "&page=" + page;
		return tweetUrl;
	}
}









