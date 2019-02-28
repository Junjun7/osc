package com.gdufe.osc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 14:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetList {

	private Integer id;

	private String pubDate;

	private String body;

	private String author;

	private String authorId;

	private Integer commentCount;

	private String portrait;
}









