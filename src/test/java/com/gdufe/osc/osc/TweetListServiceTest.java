package com.gdufe.osc.osc;

import com.gdufe.osc.OscApplicationTests;
import com.gdufe.osc.entity.TweetListDetails;
import com.gdufe.osc.service.TweetListService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/2/25 20:25
 */
public class TweetListServiceTest extends OscApplicationTests {

	@Autowired
	private TweetListService tweetListService;

	@Test
	public void getTweetListTest() {
		List<TweetListDetails> tweetList = tweetListService.listTweetList(0, 10, "-1");
		for (TweetListDetails tweetListDetails : tweetList) {
			System.out.println(tweetListDetails.getBody());
		}
	}
}
