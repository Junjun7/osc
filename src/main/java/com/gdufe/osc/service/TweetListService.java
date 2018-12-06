package com.gdufe.osc.service;

import com.gdufe.osc.entity.TweetList;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 14:45
 */
public interface TweetListService {

	List<TweetList> getTweetList(int page, int pageSize, int user);

	default String getUrl() {
		return "https://www.oschina.net/action/openapi/tweet_list?dataType=json";
	}
}
