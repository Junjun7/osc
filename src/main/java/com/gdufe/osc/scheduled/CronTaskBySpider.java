package com.gdufe.osc.scheduled;

import com.gdufe.osc.dao.DownloadImgDao;
import com.gdufe.osc.dao.ImgBiZhiDao;
import com.gdufe.osc.dao.ImgDao;
import com.gdufe.osc.entity.DownloadImg;
import com.gdufe.osc.enums.ImgTypeEnum;
import com.gdufe.osc.exception.NetworkException;
import com.gdufe.osc.utils.CacheToken;
import com.gdufe.osc.utils.HttpHelper;
import com.gdufe.osc.utils.WeChatNoticeUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
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

	private static final String CK = "_zap=a4f17e87-d96f-4e1a-89c0-5e9ffe38459d; _xsrf=bjuVsr3iGkzSXuFN42C9xVk67GzL52Ex; d_c0=\"ALCb3JMkJxGPTtNRFcCtXRpT19-oRBSY8TA=|1587464610\"; z_c0=Mi4xdlUzZUFRQUFBQUFBc0p2Y2t5UW5FUmNBQUFCaEFsVk55eW1NWHdBWDE4MmxWX1F4REZ3VFM0UzhGWVdjeXF5Q1Rn|1587469259|c5b6c27b6bf0b41a3ba407102479d568567020df; _ga=GA1.2.1491470843.1591010738; _gid=GA1.2.184526573.1591010738; tst=r; q_c1=8c35dedb6bd3453c8d3c454e3737d89b|1591010746000|1587469281000; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1591016861,1591071263,1591073013,1591073077; SESSIONID=d7v6FKvef5csNtnf60L9JhFOWcrtIaKNoRwGbncbfgD; JOID=W1oVB0pI1gmkbdqjKEUV3ZsB-_0wJ-N7lyOa10km6GjTJqbGTDPN3vdu06UpVwFAso3N8yAGkI7PzLvM1r8rTJg=; osd=WlsWCkpJ1wqpbduiK0gV3JoC9v0xJuB2lyKb1EQm6WnQK6bHTTDA3vZv0KgpVgBDv43M8iMLkI_Oz7bM174oQZg=; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1591090592; _gat_gtag_UA_149949619_1=1; KLBRSID=d1f07ca9b929274b65d830a00cbd719a|1591090641|1591090543";
	private static final String PREFIX = "https://www.zhihu.com/api/v4/questions/";
	private static final String SUFFIX = "/answers?include=data%5B%2A%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cis_labeled%2Cis_recognized%2Cpaid_info%3Bdata%5B%2A%5D.mark_infos%5B%2A%5D.url%3Bdata%5B%2A%5D.author.follower_count%2Cbadge%5B%2A%5D.topics&platform=desktop&sort_by=default&limit=5&offset=";
	private static final int LIMIT = 10;

	@Autowired
	private ImgDao imgDao;

	@Autowired
	private ImgBiZhiDao imgBiZhiDao;

	@Autowired
	private DownloadImgDao downloadImgDao;

	@Autowired
	private WeChatNoticeUtils weChatNoticeUtils;

	@Autowired
	private Environment environment;

	/** 每天凌晨3点执行爬虫 */
	@Scheduled(cron = "0 30 3 * * ?")
	@CacheEvict(value = {"zhiHuImg", "zhiHuImgCount"}, allEntries = true)
	public void imgSpider() throws InterruptedException, NetworkException {
		ImmutablePair<List<String>, List<String>> pair = initIds();
		if (pair == null) {
			return;
		}
		List<String> imgIds = pair.getLeft();
		List<String> imgBiZhiIds = pair.getRight();
		for (String id : imgIds) {
			spider(id, LIMIT, ImgTypeEnum.BEAUTIFUL_IMG);
			// 睡一分钟
			TimeUnit.MINUTES.sleep(1);
		}
		for (String id : imgBiZhiIds) {
			spider(id, LIMIT, ImgTypeEnum.PIC_IMG);
			// 睡一分钟
			TimeUnit.MINUTES.sleep(1);
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
	 * 默认循环10次
	 * @param id
	 * @param limit
	 * @param imgType
	 */
	private static int index = 1;
	public void spider(String id, int limit, ImgTypeEnum imgType) throws NetworkException {
		this.index = 1;
		for (int i = 1; i <= limit; i++) {
			int x = 5 * i;
			spider(id, x + "", imgType);
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {}
		}
	}

	/**
	 * type == 1  等于知乎美女图片
	 * type == 2  等于知乎壁纸
	 * @param id
	 * @param limit
	 * @param imgType
	 */
	public void spider(String id, String limit, ImgTypeEnum imgType) throws NetworkException {
		log.info("id = {}, limit = {}, imgType = {}", id, limit, imgType);
		// 统计更新了多少
		int cnt = 0;
		String url = getRealUrl(id, limit);
		String data = HttpHelper.get(url, CacheToken.getCK(), null);
		if (StringUtils.isEmpty(data) || data.contains("AuthenticationInvalidRequest")) {
			log.error("返回数据为空，或者cookie失效。返回数据data：{}", data);
			weChatNoticeUtils.setMessage("爬虫失败", "返回数据为空，或者cookie失效。返回数据data：" + data);
			return;
		}
		Set<String> imgSet = new HashSet<>();
		fillImg(data, imgSet);
		List<String> imgList = Lists.newArrayList(imgSet);
		for (String img : imgList) {
			if (ImgTypeEnum.BEAUTIFUL_IMG == imgType) {
				cnt += imgDao.insertImgLink(img);
			}
			if (ImgTypeEnum.PIC_IMG == imgType) {
				cnt += imgBiZhiDao.insertImgLink(img);
			}
			if (ImgTypeEnum.DOWNLOAD_IMG == imgType) {
				saveToDisk(id, img);
				cnt = imgList.size();
			}
		}
		log.info("type = {}, id = {}, 总共更新{}条数据", imgType, id, cnt);
	}

	private void saveToDisk(String id, String img) {
		log.info("id = {}, img = {}, index = {}", id, img, index);
		String osName = environment.getProperty("os.name");
		log.info("osName = {}", osName);
		String fileName = "F:/img/" + id;
		if (StringUtils.isEmpty(osName)) {
		} else {
			if (osName.toLowerCase().contains("linux")) {
				fileName = "/home/tomcat/apache-tomcat-8.5.23/workspace/osc/img/" + id;
			}
		}
		log.info("fileName = {}", fileName);
		try {
			FileUtils.forceMkdir(new File(fileName));
		} catch (IOException e) {
			log.error("创建文件失败,不再继续爬虫 e = {}", e);
			return;
		}
		fileName += "/" + index + ".jpg";
		index++;
		log.info("fileName = {}", fileName);
		try {
			FileUtils.copyURLToFile(new URL(img), new File(fileName));
		} catch (IOException e) {
			log.error("读取图片出错,该图片可能不存在 e = {}", e);
			return;
		}
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

	public static void main(String[] args) throws NetworkException {
		String url = PREFIX + "328457531" + SUFFIX + "10";
		System.out.println(url);
		String data = HttpHelper.get(url, "_zap=0f1d5301-390e-40fd-9487-274602a54fef; d_c0=\"AIDYPxWLrhGPTirG1Y3oNaDSsuIeIvDmVlo=|1596551178\"; _ga=GA1.2.1469994816.1596551179; _xsrf=76d4v4yyGpyewAw3TWcikayUgDg4yl1X; z_c0=Mi4xdlUzZUFRQUFBQUFBZ05nX0ZZdXVFUmNBQUFCaEFsVk5JdXNjWUFDQUhhWHJZMVZKWmx3X2xBM0pXUlg4RDkzZGZB|1596955938|605a43af6e542c75e25ca5f1fcb4a39537c8518e; tst=r; q_c1=e0445df378724864a0000c6f232685bd|1605268376000|1596721277000; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1606744611,1607609986,1607842154,1607861483; SESSIONID=A71wJaRTqRAipsF4FJNGue03DjsEqD2qkMIvsTsEPUU; JOID=Wl0XAkKkckl3hPNxdK2e2N_tQNJv6EJ7CM-fJCPULQ8d_IE6R6vBnyGI-XV_vJtZtOMKFrtXueawzpf65JD_Sjs=; osd=V18XBEqpcElxjP5zdKuW1d3tRtpi6kJ9AMKdJCXcIA0d-ok3RavHlyyK-XN3sZlZsusHFLtRseuyzpHy6ZL_TDM=; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1608365860; KLBRSID=9d75f80756f65c61b0a50d80b4ca9b13|1608365860|1608365826", null);
		System.out.println(data);
	}
}
