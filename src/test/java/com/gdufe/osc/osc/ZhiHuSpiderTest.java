package com.gdufe.osc.osc;

import com.gdufe.osc.scheduled.CronTaskBySpider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: yizhen
 * @date: 2019/2/25 20:25
 */
public class ZhiHuSpiderTest extends OscApplicationTests {

	@Autowired
	private CronTaskBySpider cronTaskBySpider;

	@Test
	public void insertImgTest() {
		cronTaskBySpider.imgSpider();
	}
}
