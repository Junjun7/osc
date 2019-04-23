package com.gdufe.osc.osc;

import com.gdufe.osc.entity.TweetListDetails;
import com.gdufe.osc.service.TweetListService;
import com.gdufe.osc.service.ZhiHuSpider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: yizhen
 * @date: 2019/2/25 20:25
 */
public class ZhiHuSpiderTest extends OscApplicationTests {

	@Autowired
	private ZhiHuSpider zhiHuSpider;

	@Test
	public void insertImgTest() {
		zhiHuSpider.imgSpider();
	}
}
