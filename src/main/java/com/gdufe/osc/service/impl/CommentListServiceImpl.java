package com.gdufe.osc.service.impl;

import com.gdufe.osc.entity.CommentList;
import com.gdufe.osc.entity.CommentListMore;
import com.gdufe.osc.service.CacheHelper;
import com.gdufe.osc.service.CommentListService;
import com.gdufe.osc.utils.gson.GsonUtils;
import com.gdufe.osc.utils.HttpHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

	@Autowired
	private CacheHelper<List<CommentList>> cacheHelper;

	@Override
	public List<CommentList> getCommentList(int id, int page, int pageSize) {
		List<CommentList> cl = cacheHelper.get(buildCacheKey(id, page, pageSize));
		if (!CollectionUtils.isEmpty(cl)) {
			return cl;
		}
		String url = getCommentUrl(id, page, pageSize);
		String commentList = HttpHelper.get(url);
		if (StringUtils.isEmpty(commentList)) {
			return null;
		}
		CommentListMore commentListMore = GsonUtils.fromJson(commentList, CommentListMore.class);
		List<CommentList> commentLists = commentListMore.getCommentList();
		if (CollectionUtils.isEmpty(commentLists)) {
			return null;
		}
		filterFormat(commentLists);
		cacheHelper.put(buildCacheKey(id, page, pageSize), commentLists);
		return commentLists;
	}

	private String buildCacheKey(Integer... keys) {
		String key = StringUtils.join(keys, "+");
		return key;
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
