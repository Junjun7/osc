package com.gdufe.osc.service.impl;

import com.alibaba.fastjson.JSON;
import com.gdufe.osc.entity.CommentList;
import com.gdufe.osc.entity.CommentListMore;
import com.gdufe.osc.service.CommentListService;
import com.gdufe.osc.utils.HttpMethod;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		filterFormat(commentLists);
		return commentLists;
	}

	private void filterFormat(List<CommentList> commentLists) {
		filterDate(commentLists);
		filterBody(commentLists);
	}

	private void filterDate(List<CommentList> commentLists) {
		for (CommentList commentList : commentLists) {
			String pubDate = commentList.getPubDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			Date date = null;
			try {
				date = sdf.parse(pubDate);
			} catch (ParseException e) {
				date = new Date();
			} finally {
				commentList.setPubDate(date.getTime() + "");
			}
		}
	}

	private void filterBody(List<CommentList> commentLists) {
		for (CommentList commentList : commentLists) {
			String body = commentList.getContent();
			// osc本地表情 基本格式如下 <emoji align=\"absmiddle\" data-emoji=\"emoji flushed\" data-name=\"flushed\"></emoji>
			String regex = "<emo.+?ji>";
			String res = body.replaceAll(regex, "");
			// 网络表情 借本格式如下：<img src=\"http://www.oschina.net/js/ke/plugins/emoticons/35.gif\" alt=\"35\">
			regex = "<img.+?>";
			res = res.replaceAll(regex, "");
			// 链接，@红薯，#AEAI HR# 如：
			// <a href=\"https://gitee.com/johncoffey/gvt2paceko1hzlw7d09fb97.code\" target=\"_blank\" rel=\"nofollow\">https://gitee.com/johncoffey/gvt2paceko1hzlw7d09fb97.code</a>
			// <a href='https://my.oschina.net/u/12' target=\"_blank\" rel=\"nofollow\">@红薯</a>
			// <a href=\"https://www.oschina.net/p/aeaihr\" target=\"_blank\" rel=\"nofollow\">#AEAI HR#</a>
			regex = "<a.+?>";
			res = res.replaceAll(regex, "");
			res = res.replaceAll("</a>", "");
			// 箭头  \n还不清楚，先看看
			regex = "&.t;";
			res = res.replaceAll(regex, ">");
			commentList.setContent(res);
		}
	}
}
