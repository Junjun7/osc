package com.gdufe.osc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: yizhen
 * @Date: 2018/12/17 15:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentList {

	// 头像
	private String commentPortrait;

	// 评论人ID
	private Integer commentAuthorId;

	// 昵称
	private String commentAuthor;

	// 评论内容ID
	private Integer id;

	// 1-WEB、2-WAP、3-Android、4-IOS、5-WP
	private int client_type;

	// 时间
	private String pubDate;

	// 内容
	private String content;
}









