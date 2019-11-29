package com.gdufe.osc.tweet;

import com.gdufe.osc.OscApplicationTests;
import com.gdufe.osc.entity.CommentList;
import com.gdufe.osc.entity.TweetListDetails;
import com.gdufe.osc.service.CommentListService;
import com.gdufe.osc.service.TweetListService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author changwenbo
 * @date 2019/11/9 12:46
 */
@Slf4j
public class TweetTest extends OscApplicationTests {

	@Autowired
	private TweetListService tweetListService;

	@Autowired
	private CommentListService commentListService;

	@Test
	public void testTweet() {
		tweetListService.listTweetList(1, 10, "0");
		List<TweetListDetails> tweetListDetails = tweetListService.listTweetList(1, 10, "-1");
		log.info("tweetListDetails = {}", tweetListDetails);
		int id = tweetListDetails.get(0).getId();
		TweetListDetails tweetList = tweetListService.getTweetList(id + "");
		log.info("tweetList = {}", tweetList);
	}

	@Test
	public void testComment() {
		List<CommentList> commentList = commentListService.getCommentList(20631949, 1, 10);
		System.out.println(commentList);
	}
}
