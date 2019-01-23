package com.gdufe.osc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yizhen
 * @date: 2019/1/23 15:48
 */
@Getter
@AllArgsConstructor
public enum TweetCodeEnum {

	NEWEST_TWEET_CODE("0", "最新动弹"),
	HOTEST_TWEET_CODE("-1", "热门动弹"),
	MY_TWEET_CODE("1", "我的动弹");

	private String code;
	private String msg;
}
