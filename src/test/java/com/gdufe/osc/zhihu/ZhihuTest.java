package com.gdufe.osc.zhihu;

import com.gdufe.osc.OscApplicationTests;
import com.gdufe.osc.controller.ZhuHuSpiderController;
import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.entity.Img;
import com.gdufe.osc.scheduled.CronTaskBySpider;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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

	@Autowired
	private ImgDao imgDao;

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
	public void spider() throws InterruptedException {
		cronTaskBySpider.imgSpider();
	}

	@Test
	public void insertImg() {
		String prefix = "http://www.img.gdufe888.top/task/img/url/";
		List<Img> imgs = imgDao.listImgLink(1, 10000);
		List<Long> list = new ArrayList<>();
		for (Img img : imgs) {
			list.add(img.getId());
		}
		list.sort(Long::compareTo);
		List<Long> res = new ArrayList<>();
		for (long i = 1; i < 61523; i++) {
			if (!list.contains(i)) {
				res.add(i);
			}
		}
		int cnt = 0;
		for (int i = 2; i <= 500; i++) {
			String url = prefix + i;
//			cnt += imgDao.insertImgLink(res.get(i), url);
			System.out.println(res.get(i));
		}
		log.info("总共更新cnt = {}", cnt);
	}
}








