package com.gdufe.osc.service.impl;

import com.arronlong.httpclientutil.common.HttpHeader;
import com.gdufe.osc.dao.ImgBiZhiDao;
import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.entity.Img;
import com.gdufe.osc.entity.ImgBiZhi;
import com.gdufe.osc.service.ZhiHuSpider;
import com.gdufe.osc.utils.HttpMethod;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: yizhen
 * @date: 2019/4/20 13:05
 */
@Service
@Slf4j
public class ZhiHuSpiderImpl implements ZhiHuSpider {

	private static final String PREFIX = "https://www.zhihu.com/api/v4/questions/";
	private static final String SUFFIX = "/answers?include=data%5B%2A%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cis_labeled%2Cis_recognized%2Cpaid_info%3Bdata%5B%2A%5D.mark_infos%5B%2A%5D.url%3Bdata%5B%2A%5D.author.follower_count%2Cbadge%5B%2A%5D.topics&platform=desktop&sort_by=default&offset=0&limit=";
	private static final String LIMIT = "20";

	@Autowired
	private ImgDao imgDao;
	@Autowired
	private ImgBiZhiDao imgBiZhiDao;

	@Override
	public List<String> getImg(Integer offset, Integer limit, String type) {
		offset = convertOffset(limit, type);
		List<Img> imgs = Lists.newArrayList();
		if ("1".equals(type)) {
			imgs = imgDao.listImgLink(offset, limit);
		} else if ("2".equals(type)) {
			List<ImgBiZhi> imgBiZhis = imgBiZhiDao.listImgLink(offset, limit);
			for (ImgBiZhi imgBiZhi : imgBiZhis) {
				Img img = new Img();
				BeanUtils.copyProperties(imgBiZhi, img);
				imgs.add(img);
			}
		}
		if (CollectionUtils.isEmpty(imgs)) {
			return null;
		}
		List<String> res = Lists.newArrayList();
		for (Img img : imgs) {
			res.add(img.getLink());
		}
		return res;
	}

	/** 随机选择图片 */
	private Integer convertOffset(int limit, String type) {
		Random random = new Random();
		int cnt = 0;
		if ("1".equals(type)) {
			cnt = Integer.parseInt(imgDao.countImg().toString());
		} else if ("2".equals(type)) {
			cnt = Integer.parseInt(imgBiZhiDao.countImg().toString());
		}
		int rd = random.nextInt(cnt);
		if (rd > limit) {
			rd -= limit;
		}
		log.info("图片随机位置为：{}", rd);
		return rd;
	}

	/** 每天凌晨3点执行爬虫 */
	@Scheduled(cron = "0 0 3 * * ?")
	@CacheEvict(value = {"zhiHuImg", "zhiHuImgCount"}, allEntries = true)
	@Override
	public void imgSpider() {
		for (String id : imgIds) {
			try {
				spider(id, LIMIT, "1");
				// 睡一分钟
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (String id : imgBiZhiIds) {
			try {
				spider(id, LIMIT, "2");
				// 睡一分钟
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * type == 1  等于知乎美女图片
	 * type == 2  等于知乎壁纸
	 * @param id
	 * @param limit
	 * @param type
	 */
	public void spider(String id, String limit, String type) {
		// 统计更新了多少
		int cnt = 0;
		String url = getRealUrl(id, limit);
		String data = HttpMethod.get(url, getHeader());
		Set<String> imgSet = Sets.newHashSet();
		fillImg(data, imgSet);
		List<String> imgList = Lists.newArrayList(imgSet);
		for (String img : imgList) {
			if ("1".equals(type)) {
				cnt += imgDao.insertImgLink(img);
			} else if ("2".equals(type)) {
				cnt += imgBiZhiDao.insertImgLink(img);
			}
		}
		log.info("type = {}, id = {}, 总共更新{}条数据", type, id, cnt);
	}

	private void fillImg(String data, Set<String> imgSet) {
		String regex = "data-o.+?jpg";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(data);
		while (matcher.find()) {
			String img = matcher.group();
			int pos = img.indexOf("http");
			if (pos == -1) {
				continue;
			}
			img = img.substring(pos);
			imgSet.add(img);
		}
	}

	private String getRealUrl(String id, String offset) {
		return PREFIX + id + SUFFIX + offset;
	}

	private static Header[] getHeader() {
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
		String cookie = "_zap=e0f1dba9-efe7-4330-a1c1-c9d45fe69fb5; _xsrf=cgXpvPe5gh97OizglawHAcMySweuy4W3; d_c0=\"AACk_X453g6PThxxsQ_7notxelvG_M7f7dE=|1548211408\"; capsion_ticket=\"2|1:0|10:1553225862|14:capsion_ticket|44:MzBkOTI0NmE4MmNhNDliMGE3YzZhOGI0ZjgwMDAyZWU=|143aeb4c683a826fdf4439e7cd6ae94f567f9ff207d18b2842e33ac251b1231f\"; r_cap_id=\"ZWU2ZjI5MmQ2MTkzNGFlOThjMTU3ZjdkNjYxZDFlMGM=|1553225866|a9329a78eaaf130be83232a87697caba90f3b262\"; cap_id=\"OTAwZTRjMjIwMWRjNDQ3NDkzOTg0NDg3MmRjY2M2NDU=|1553225866|60199f89d4d37f78a8e59ce814d8a060d3471097\"; l_cap_id=\"Mjk3MmU4ZTIyOWFjNDRmMzkwODZjNTkyZWQ2MWFhMTY=|1553225866|f169cd2af8ce075ce3031cb4747f2981950cfeb1\"; z_c0=Mi4xdlUzZUFRQUFBQUFBQUtUOWZqbmVEaGNBQUFCaEFsVk5qcWFCWFFEWVFNdVVnUjRYU2hEUjM1ci1BenljcjVrQTZB|1553225870|f073c6b36eb31853c093a56f35d863c4f7f67b35; q_c1=11c689f9e21a4b0ca282984ea13da8ba|1553520013000|1548211446000; tst=r; tgw_l7_route=116a747939468d99065d12a386ab1c5f";
		Header[] headers = HttpHeader.custom()
				.userAgent(userAgent)
				.cookie(cookie)
				.build();
		return headers;
	}
}








