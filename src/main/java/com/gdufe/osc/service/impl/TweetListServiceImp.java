package com.gdufe.osc.service.impl;

import com.gdufe.osc.entity.TweetList;
import com.gdufe.osc.entity.TweetListDetails;
import com.gdufe.osc.entity.TweetListMore;
import com.gdufe.osc.service.CacheHelper;
import com.gdufe.osc.service.RedisService;
import com.gdufe.osc.service.TweetListService;
import com.gdufe.osc.utils.gson.GsonUtils;
import com.gdufe.osc.utils.HttpHelper;
import com.gdufe.osc.utils.NumberUtils;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 14:45
 */
@Service
@Slf4j
public class TweetListServiceImp implements TweetListService {

	@Autowired
	private CacheHelper<TweetListDetails> cacheHelper;

	@Autowired
	private RedisService redisService;

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
		String tweetUrl = getDetailsUrl(redisService);
		List<TweetListDetails> res = ids.stream().map(id -> {
			String url = tweetUrl + "&id=" + id;
			return getTweetDetails(url);
		}).filter(tweet -> tweet != null).collect(Collectors.toList());
		return res;
	}

	private TweetListDetails getTweetDetails(String url) {
		TweetListDetails cacheDetails = cacheHelper.get(url);
		if (Objects.nonNull(cacheDetails)) {
			return cacheDetails;
		}
		String data = HttpHelper.get(url);
		if (StringUtils.isEmpty(data)) {
			return null;
		}
		// ?????????Gson??????????????????
		data = transferData(data);
		TweetListDetails details = GsonUtils.fromJson(data, TweetListDetails.class);
		filterFormat(details);
		if (!StringUtils.isEmpty(details.getImgSmallStr()) && !StringUtils.isEmpty(details.getImgBigStr())) {
			details = filterImg(details);
		}
		cacheHelper.put(url, details);
		return details;
	}

	private String transferData(String data) {
		JsonObject parse = GsonUtils.toJsonObjectWithNullable(data);
		if (parse.has("imgBig")) {
			String imgBig = parse.get("imgBig").getAsString();
			parse.addProperty("imgBigStr", imgBig);
			parse.remove("imgBig");
		}
		if (parse.has("imgSmall")) {
			String imgSmall = parse.get("imgSmall").getAsString();
			parse.addProperty("imgSmallStr", imgSmall);
			parse.remove("imgSmall");
		}
		return parse.toString();
	}

	/**
	 * ????????????????????????????????????
	 * ????????????????????????
	 * body???????????????url????????????
	 *
	 * @param details
	 */
	private void filterFormat(TweetListDetails details) {
		filterDate(details);
		filterBody(details);
	}

	private void filterBody(TweetListDetails details) {
		String body = details.getBody();
		// osc???????????? ?????????????????? <emoji align=\"absmiddle\" data-emoji=\"emoji flushed\" data-name=\"flushed\"></emoji>
		String regex = "<emo.+?ji>";
		String res = body.replaceAll(regex, "");
		// ???????????? ?????????????????????<img src=\"http://www.oschina.net/js/ke/plugins/emoticons/35.gif\" alt=\"35\">
		regex = "<img.+?>";
		res = res.replaceAll(regex, "");
		// ?????????@?????????#AEAI HR# ??????
		// <a href=\"https://gitee.com/johncoffey/gvt2paceko1hzlw7d09fb97.code\" target=\"_blank\" rel=\"nofollow\">https://gitee.com/johncoffey/gvt2paceko1hzlw7d09fb97.code</a>
		// <a href='https://my.oschina.net/u/12' target=\"_blank\" rel=\"nofollow\">@??????</a>
		// <a href=\"https://www.oschina.net/p/aeaihr\" target=\"_blank\" rel=\"nofollow\">#AEAI HR#</a>
		regex = "<a.+?>";
		res = res.replaceAll(regex, "");
		res = res.replaceAll("</a>", "");
		// ??????  \n????????????????????????
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
		String[] smallImg = details.getImgSmallStr().split("https");
		for (int i = 2; i < smallImg.length; i++) {
			smallImg[i] = smallImg[i].replaceAll(",", "");
			imgList.add("https" + smallImg[i]);
		}
		details.setImgSmall(imgList);

		imgList = Lists.newArrayList();
		String[] bigImg = details.getImgBigStr().split("https");
		for (int i = 2; i < bigImg.length; i++) {
			bigImg[i] = bigImg[i].replaceAll(",", "");
			imgList.add("https" + bigImg[i]);
		}
		details.setImgBig(imgList);
		return details;
	}

	private List<Integer> getTweetIds(int page, int pageSize, String user) {
		String tweetUrl = getTweetUrl(page, pageSize, user);
		String data = HttpHelper.get(tweetUrl);
		if (StringUtils.isEmpty(data)) {
			return null;
		}
		TweetListMore tweetListMore = GsonUtils.fromJson(data, TweetListMore.class);
		List<TweetList> lists = tweetListMore.getTweetlist();
		List<Integer> ids = lists.stream().map((TweetList::getId))
				.collect(Collectors.toList());
		return ids;
	}

	private String getTweetUrl(int page, int pageSize, String user) {
		String tweetUrl = getIdsUrl(redisService);
		tweetUrl += "&user=" + user + "&pageSize=" + pageSize + "&page=" + page;
		return tweetUrl;
	}
}









