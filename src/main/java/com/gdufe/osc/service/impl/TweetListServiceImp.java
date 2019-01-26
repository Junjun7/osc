package com.gdufe.osc.service.impl;

import com.alibaba.fastjson.JSON;
import com.gdufe.osc.entity.TweetList;
import com.gdufe.osc.entity.TweetListDetails;
import com.gdufe.osc.entity.TweetListMore;
import com.gdufe.osc.service.TweetListService;
import com.gdufe.osc.utils.HttpMethod;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 14:45
 */
@Service
@Slf4j
public class TweetListServiceImp implements TweetListService {

	@Override
	public List<TweetListDetails> getTweetList(int page, int pageSize, String user) {
		List<Integer> ids = getTweetIds(page, pageSize, user);
		List<TweetListDetails> res = getTweetListDetails(ids);
		return res;
	}

	private List<TweetListDetails> getTweetListDetails(List<Integer> ids) {
		String tweetUrl = getDetailsUrl();
		List<TweetListDetails> res = Lists.newArrayList();
		for (int id : ids) {
			String url = tweetUrl + "&id=" + id;
			//log.info("id = " + id);
			res.add(getTweetDetails(url));
		}
		return res;
	}

	private TweetListDetails getTweetDetails(String url) {
		String data = HttpMethod.get(url);
		TweetListDetails details = JSON.parseObject(data, TweetListDetails.class);
		if (details.getImgBig() != null && details.getImgBig() != null) {
			details = filterImg(details);
		}
		return details;
	}

	private TweetListDetails filterImg(TweetListDetails details) {
		StringBuilder sb = new StringBuilder();
		String[] smallImg = details.getImgSmall().split("https");
		for (int i = 2; i < smallImg.length; i++) {
			sb.append("https" + smallImg[i]);
		}
		details.setImgSmall(sb.toString());
		sb = new StringBuilder();
		String[] bigImg = details.getImgBig().split("https");
		for (int i = 2; i < bigImg.length; i++) {
			sb.append("https" + bigImg[i]);
		}
		details.setImgBig(sb.toString());
		return details;
	}

	private List<Integer> getTweetIds(int page, int pageSize, String user) {
		String tweetUrl = getTweetUrl(page, pageSize, user);
		String data = HttpMethod.get(tweetUrl);
		TweetListMore tweetListMore = JSON.parseObject(data, TweetListMore.class);
		List<TweetList> lists = tweetListMore.getTweetlist();
		List<Integer> ids = Lists.newArrayList();
		lists.forEach(x -> {
			ids.add(x.getId());
			//log.info("authorId = " + x.getAuthorId());
		});
		return ids;
	}

	private String getTweetUrl(int page, int pageSize, String user) {
		String tweetUrl = getIdsUrl();
		tweetUrl += "&user=" + user + "&pageSize=" + pageSize + "&page=" + page;
		return tweetUrl;
	}
}









