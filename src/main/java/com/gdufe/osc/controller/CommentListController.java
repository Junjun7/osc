package com.gdufe.osc.controller;

import com.gdufe.osc.annotation.TimeWatch;
import com.gdufe.osc.entity.CommentList;
import com.gdufe.osc.service.CommentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/17 16:04
 */
@RestController
@RequestMapping("/api/comment")
public class CommentListController {

	@Autowired
	private CommentListService commentListService;

	@TimeWatch
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<CommentList> getCommentList(Integer id, Integer page, Integer pageSize) {
		return commentListService.getCommentList(id, page, pageSize);
	}
}










