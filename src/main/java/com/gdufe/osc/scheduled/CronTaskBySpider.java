package com.gdufe.osc.scheduled;

import com.gdufe.osc.dao.DownloadImgDao;
import com.gdufe.osc.dao.ImgBiZhiDao;
import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.entity.DownloadImg;
import com.gdufe.osc.utils.HttpHelper;
import com.gdufe.osc.utils.WeChatNoticeUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author changwenbo
 * @date 2019/10/19 13:26
 */
@Component
@Slf4j
public class CronTaskBySpider {

	private static final String CK = "tgw_l7_route=7bacb9af7224ed68945ce419f4dea76d; _zap=ec860ed6-c726-404a-b137-0448fe654614; _xsrf=E0aPOcqmW3ggTDhccHzy8BfvD6wQSQEf; d_c0=\"AEDtM5xiPBCPTjqd6pwQhvgC0X5A1Rm_uQM=|1571710288\"; capsion_ticket=\"2|1:0|10:1571710288|14:capsion_ticket|44:MTcyNjdiMjY4YTQwNDc3ZWI2NWJhMTcyYjM2ZTY5MDM=|a1eea0e44b1fe86b908f1b5d6b995fa5e33be4dd70d1ac2e35477aa69c9ce3dc\"; l_n_c=1; r_cap_id=\"MDI5MWIyYmZjZWUwNDYzM2FhNjIwZGE4ZWQ3MDlmNGE=|1571710289|20aa72ae35910ba1a90033d8c9a73ec4f62c5710\"; cap_id=\"Nzk1NTBmM2JjYmJmNDUwOWE3YTc4Y2JkYjNhMzFhMmY=|1571710289|3de2d13909e08baeec9134f2f9b659472b41259d\"; l_cap_id=\"N2RmNzA3Y2Y5ZDM0NGRjYjgyZDUzNzY0ZmUxZWU4MjQ=|1571710289|0773d44889ba974e807e598f6eb8587bc391d260\"; n_c=1; z_c0=Mi4xdlUzZUFRQUFBQUFBUU8wem5HSThFQmNBQUFCaEFsVk5ZN09iWGdBMDl1OXR0YTRRWlpvYmd2QlZ2LUNPelQ1a01n|1571710307|9195cd6155797864b6586b4f1752fb903e174a1a; tst=f";
	private static final String PREFIX = "https://www.zhihu.com/api/v4/questions/";
	private static final String SUFFIX = "/answers?include=data%5B%2A%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cis_labeled%2Cis_recognized%2Cpaid_info%3Bdata%5B%2A%5D.mark_infos%5B%2A%5D.url%3Bdata%5B%2A%5D.author.follower_count%2Cbadge%5B%2A%5D.topics&platform=desktop&sort_by=default&offset=0&limit=";
	private static final String LIMIT = "20";

	@Autowired
	private ImgDao imgDao;
	@Autowired
	private ImgBiZhiDao imgBiZhiDao;
	@Autowired
	private DownloadImgDao downloadImgDao;

	/** 每天凌晨3点执行爬虫 */
	@Scheduled(cron = "0 30 3 * * ?")
	@CacheEvict(value = {"zhiHuImg", "zhiHuImgCount"}, allEntries = true)
	public void imgSpider() {
		ImmutablePair<List<String>, List<String>> pair = initIds();
		if (pair == null) {
			return;
		}
		List<String> imgIds = pair.getLeft();
		List<String> imgBiZhiIds = pair.getRight();
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
	 * 获取需要爬取的知乎id
	 * @return
	 */
	private ImmutablePair<List<String>, List<String>> initIds() {
		DownloadImg imageIds = downloadImgDao.getImageIds();
		if (imageIds == null) {
			return null;
		}
		List<String> imgIds = Lists.newArrayList(StringUtils.split(imageIds.getLink(), ","));
		List<String> imgBiZhiIds = Lists.newArrayList(StringUtils.split(imageIds.getLinkname(), ","));
		log.info("imgIds = {}", imgIds);
		log.info("imgBiZhiIds = {}", imgBiZhiIds);
		return ImmutablePair.of(imgIds, imgBiZhiIds);
	}

	/**
	 * type == 1  等于知乎美女图片
	 * type == 2  等于知乎壁纸
	 * @param id
	 * @param limit
	 * @param type
	 */
	private void spider(String id, String limit, String type) {
		// 统计更新了多少
		int cnt = 0;
		String url = getRealUrl(id, limit);
		String data = HttpHelper.get(url, CK, null);
		if (StringUtils.isEmpty(data) || data.contains("AuthenticationInvalidRequest")) {
			log.error("返回数据为空，或者cookie失效。返回数据data：{}", data);
			WeChatNoticeUtils.setMessage("爬虫失败", "返回数据为空，或者cookie失效。返回数据data：" + data);
			return;
		}
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

	public static void main(String[] args) {
		String url = PREFIX + "328457531" + SUFFIX + "10";
		System.out.println(url);
		String data = HttpHelper.get(url, CK, null);
		System.out.println(data);
	}
}
