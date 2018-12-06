package com.gdufe.osc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 15:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetListMore {


	private Notice notice;
	private List<TweetList> tweetlist;

	private class Notice {
		private Integer replyCount;
		private Integer msgCount;
		private Integer fansCount;
		private Integer referCount;
	}
}
