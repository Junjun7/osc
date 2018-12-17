package com.gdufe.osc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/17 15:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentListMore {

	private Notice notice;

	private List<CommentList> commentList;
}
