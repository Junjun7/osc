package com.gdufe.osc.controller;

import com.gdufe.osc.annotation.TimeWatch;
import com.gdufe.osc.entity.CommentList;
import com.gdufe.osc.service.CommentListService;
import com.gdufe.osc.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/17 16:04
 */
@RestController
@RequestMapping("/prefix/api/comment")
public class CommentListController {
	@Autowired
	private CommentListService commentListService;

	@TimeWatch
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<CommentList> getCommentList(String id, Integer page, Integer pageSize) {
		if (!NumberUtils.isNumber(id)) {
			return Collections.emptyList();
		}
		return commentListService.getCommentList(NumberUtils.toInt(id), page, pageSize);
	}
}










