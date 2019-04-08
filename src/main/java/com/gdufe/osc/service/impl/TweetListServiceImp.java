package com.gdufe.osc.service.impl;

import com.alibaba.fastjson.JSON;
import com.gdufe.osc.entity.TweetList;
import com.gdufe.osc.entity.TweetListDetails;
import com.gdufe.osc.entity.TweetListMore;
import com.gdufe.osc.service.CacheHelper;
import com.gdufe.osc.service.TweetListService;
import com.gdufe.osc.utils.HttpMethod;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 14:45
 */
@Service
@Slf4j
public class TweetListServiceImp implements TweetListService {

	@Autowired
	private CacheHelper<TweetListDetails> cacheHelper;

	@Override
	public List<TweetListDetails> listTweetList(int page, int pageSize, String user) {
		List<Integer> ids = getTweetIds(page, pageSize, user);
		if (CollectionUtils.isEmpty(ids)) {
			return null;
		}
		List<TweetListDetails> res = getTweetListDetails(ids);
		return res;
	}

	@Override
	public TweetListDetails getTweetList(String tweetId) {
		Integer id = NumberUtils.toInt(tweetId, 0);
		List<Integer> ids = Lists.newArrayList(id);
		List<TweetListDetails> res = getTweetListDetails(ids);
		return CollectionUtils.isEmpty(res) ? null : res.get(0);
	}

	private List<TweetListDetails> getTweetListDetails(List<Integer> ids) {
		String tweetUrl = getDetailsUrl();
		List<TweetListDetails> res = Lists.newArrayList();
		for (int id : ids) {
			String url = tweetUrl + "&id=" + id;
			TweetListDetails tweetDetails = getTweetDetails(url);
			if (Objects.isNull(tweetDetails)) {
				continue;
			}
			res.add(tweetDetails);
		}
		return res;
	}

	private TweetListDetails getTweetDetails(String url) {
		TweetListDetails cacheDetails = cacheHelper.get(url);
		if (Objects.nonNull(cacheDetails)) {
			return cacheDetails;
		}
		String data = HttpMethod.get(url);
		if (StringUtils.isEmpty(data)) {
			return null;
		}
		TweetListDetails details = JSON.parseObject(data, TweetListDetails.class);
		filterFormat(details);
		if (!CollectionUtils.isEmpty(details.getImgSmall()) && !CollectionUtils.isEmpty(details.getImgBig())) {
			details = filterImg(details);
		}
		cacheHelper.put(url, details);
		return details;
	}

	/**
	 * 过滤一些不符合前端的字段
	 * 如时间格式不符合
	 * body里面的一些url，表情等
	 *
	 * @param details
	 */
	private void filterFormat(TweetListDetails details) {
		filterDate(details);
		filterBody(details);
	}

	private void filterBody(TweetListDetails details) {
		String body = details.getBody();
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
		details.setBody(res);
	}

	private void filterDate(TweetListDetails details) {
		String pubDate = details.getPubDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(pubDate);
		} catch (ParseException e) {
			date = new Date();
		} finally {
			details.setPubDate(date.getTime() + "");
		}
	}

	private TweetListDetails filterImg(TweetListDetails details) {
		List<String> imgList = Lists.newArrayList();
		String[] smallImg = details.getImgSmall().get(0).split("https");
		for (int i = 2; i < smallImg.length; i++) {
			smallImg[i] = smallImg[i].replaceAll(",", "");
			imgList.add("https" + smallImg[i]);
		}
		details.setImgSmall(imgList);

		imgList = Lists.newArrayList();
		String[] bigImg = details.getImgBig().get(0).split("https");
		for (int i = 2; i < bigImg.length; i++) {
			bigImg[i] = bigImg[i].replaceAll(",", "");
			imgList.add("https" + bigImg[i]);
		}
		details.setImgBig(imgList);
		return details;
	}

	private List<Integer> getTweetIds(int page, int pageSize, String user) {
		String tweetUrl = getTweetUrl(page, pageSize, user);
		String data = HttpMethod.get(tweetUrl);
		if (StringUtils.isEmpty(data)) {
			return null;
		}
		TweetListMore tweetListMore = JSON.parseObject(data, TweetListMore.class);
		List<TweetList> lists = tweetListMore.getTweetlist();
		List<Integer> ids = Lists.newArrayList();
		lists.forEach(x -> {
			ids.add(x.getId());
		});
		return ids;
	}

	private String getTweetUrl(int page, int pageSize, String user) {
		String tweetUrl = getIdsUrl();
		tweetUrl += "&user=" + user + "&pageSize=" + pageSize + "&page=" + page;
		return tweetUrl;
	}
}









