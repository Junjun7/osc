package com.gdufe.osc.controller;

import com.gdufe.osc.annotation.TimeWatch;
import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.entity.CommentList;
import com.gdufe.osc.entity.TweetListDetails;
import com.gdufe.osc.enums.OscResultEnum;
import com.gdufe.osc.enums.TweetCodeEnum;
import com.gdufe.osc.service.CommentListService;
import com.gdufe.osc.service.RedisHelper;
import com.gdufe.osc.utils.IPUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: yizhen
 * @Date: 2018/12/17 16:04
 */
@RestController
@RequestMapping("/api")
public class FakeController {

	private static final String RES = "我日，竟然刷我接口，我靠！！！";

	@Autowired
	private RedisHelper<Integer> redisHelper;

	@RequestMapping(value = "/comment/list", method = RequestMethod.GET)
	public String getCommentList(Integer id, Integer page, Integer pageSize,
	                             HttpServletRequest request) {
		addBlockIP(request);
		return RES;
	}

	@RequestMapping(value = "/tweet/newest", method = RequestMethod.GET)
	public String listNewestTweetList(Integer page, Integer pageSize) {
		return RES;
	}

	@RequestMapping(value = "/tweet/hotest", method = RequestMethod.GET)
	public String listHotestTweetList(Integer page, Integer pageSize) {
		return RES;
	}

	@RequestMapping(value = "/tweet/{userId}", method = RequestMethod.GET)
	public String listUsersTweetList(
			Integer page, Integer pageSize, HttpServletRequest request,
			@PathVariable(value = "userId", required = false) String userId) {
		addBlockIP(request);
		return RES;
	}

	@RequestMapping(value = "/tweet/single/{tweetId}", method = RequestMethod.GET)
	public String getSingleTweetList(
			@PathVariable(value = "tweetId", required = false) String tweetId,
			HttpServletRequest request) {
		addBlockIP(request);
		return RES;
	}

	private void addBlockIP(HttpServletRequest request) {
		// 加入黑名单
		String ip = IPUtils.getClientIp(request);
		redisHelper.set(ip, 50);
	}
}










