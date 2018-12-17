package com.gdufe.osc.service;

import com.gdufe.osc.entity.CommentList;
import com.gdufe.osc.utils.CacheToken;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/17 15:19
 */
@Service
public interface CommentListService {

	List<CommentList> getCommentList(int id, int page, int pageSize);

	default String getCommentUrl(int id, int page, int pageSize) {

		return "https://www.oschina.net/action/openapi/comment_list?catalog=3&page=" + page + "&pageSize=" + pageSize + "&dataType=json&id=" + id +"&access_token=" + CacheToken.getToken();
	}

}