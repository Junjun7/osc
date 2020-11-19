package com.gdufe.osc.scheduled;

import com.gdufe.osc.utils.GsonUtils;
import com.gdufe.osc.utils.HttpHelper;
import com.gdufe.osc.utils.WeChatNoticeUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author changwenbo
 * @date 2020/9/14 10:11
 */
@Component
@Slf4j
public class CronTaskByStock {

	private static final String url = "http://ipo.sseinfo.com/info/commonQuery.do?jsonCallBack=jsonpCallback79425&isPagination=true&sqlId=COMMON_SSE_IPO_ISSUE_L&stockType=0&pageHelp.pageSize=15&_=1600048639851";
	private static final String format = "yyyy-MM-dd";

	@Autowired
	private WeChatNoticeUtils weChatNoticeUtils;

	/** 每天早上九点 */
	@Scheduled(cron = "0 0 9 * * ?")
	public void notifyStockTime() {
		try {
			String content = HttpHelper.get(url);
			if (StringUtils.isEmpty(content) || !content.startsWith("jsonpCallback")) {
				log.error("e = {}", content);
				weChatNoticeUtils.setMessage("股票爬取失败，请您及时排查问题..", content);
				return;
			}
			int start = content.indexOf("{");
			int end = content.lastIndexOf("}");
			String jsonStr = StringUtils.substring(content.substring(start, end + 1), 0, -1) + "}";
			JsonObject jsonObject = GsonUtils.toJsonObjectWithNullable(jsonStr);
			JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
			log.info("jsonStr = {}", jsonArray);
			StringBuilder name = new StringBuilder();
			DateTime dateTime = DateTime.now();
			String dt = dateTime.toString(format);
			boolean send = false;
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject json = jsonArray.get(i).getAsJsonObject();
				String date = json.get("ONLINE_ISSUANCE_DATE").getAsString();
				String stockName = json.get("SECURITY_NAME").getAsString();
				if (dt.equals(date)) {
					send = true;
					name.append(dt + "  " + stockName);
					name.append("\n");
					log.info("date = {}, stockName = {}", date, stockName);
				}
			}
			if (send) {
				weChatNoticeUtils.setMessage("今天有如下股票申购，请及时申购：", name.toString());
			}
		} catch (Exception e) {
			log.error("e = {}", e);
			weChatNoticeUtils.setMessage("股票爬取失败，请您及时排查问题..", e.toString());
			return;
		}
	}
}























