package com.gdufe.osc.service.impl;

import com.alibaba.fastjson.JSON;
import com.gdufe.osc.entity.CommentList;
import com.gdufe.osc.entity.CommentListMore;
import com.gdufe.osc.service.CommentListService;
import com.gdufe.osc.utils.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/17 15:57
 */
@Service
public class CommentListServiceImpl implements CommentListService {

	@Override
	public List<CommentList> getCommentList(int id, int page, int pageSize) {

		String url = getCommentUrl(id, page, pageSize);
		String commentList = HttpMethod.get(url);
		CommentListMore commentListMore = JSON.parseObject(commentList, CommentListMore.class);
		List<CommentList> commentLists = commentListMore.getCommentList();
		return commentLists;
	}
}
