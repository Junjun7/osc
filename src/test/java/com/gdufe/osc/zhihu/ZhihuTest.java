package com.gdufe.osc.zhihu;

import com.gdufe.osc.OscApplicationTests;
import com.gdufe.osc.common.OscResult;
import com.gdufe.osc.controller.ZhuHuSpiderController;
import com.gdufe.osc.scheduled.CronTaskBySpider;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author changwenbo
 * @date 2019/11/6 20:31
 */
@Slf4j
public class ZhihuTest extends OscApplicationTests {

	@Autowired
	private ZhuHuSpiderController zhuHuSpiderController;

	@Autowired
	private CronTaskBySpider cronTaskBySpider;

	private Gson gson = new Gson();

	@Test
	public void testZhihu() {
		String res = gson.toJson(zhuHuSpiderController.listSpiderImg(1, 10, "1"));
		log.info("res = {}", res);
		res = gson.toJson(zhuHuSpiderController.listSpiderImg(1, 10, "2"));
		log.info("res = {}", res);
		res = gson.toJson(zhuHuSpiderController.listDownloadImg());
		log.info("res = {}", res);
	}

	@Test
	public void spider() {
		cronTaskBySpider.imgSpider();
	}
}








