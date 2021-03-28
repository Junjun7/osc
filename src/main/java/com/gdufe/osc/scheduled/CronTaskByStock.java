package com.gdufe.osc.scheduled;

import com.gdufe.osc.utils.gson.GsonUtils;
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

	private static final String stockUrl = "http://ipo.sseinfo.com/info/commonQuery.do?jsonCallBack=jsonpCallback79425&isPagination=true&sqlId=COMMON_SSE_IPO_ISSUE_L&stockType=0&pageHelp.pageSize=15&_=1616911664823";
	private static final String bondUrl = "http://dcfm.eastmoney.com/em_mutisvcexpandinterface/api/js/get?type=KZZ_LB2.0&token=70f12f2f4f091e459a279469fe49eca5&st=STARTDATE&sr=-1&p=1&ps=10";
	private static final String format = "yyyy-MM-dd";

	@Autowired
	private WeChatNoticeUtils weChatNoticeUtils;

	/** 每天9点执行 */
	@Scheduled(cron = "0 0 9 * * ?")
	public void notifyStockTime() {
		executeStock();
		executeBond();
	}

	private void executeBond() {
		try {
			String content = HttpHelper.get(bondUrl);
			if (StringUtils.isEmpty(content)) {
				log.error("e = {}", content);
				weChatNoticeUtils.setMessage("债券爬取失败，请您及时排查问题..", content);
				return;
			}
			JsonArray jsonArray = GsonUtils.toJsonArrayWithNullable(content);
			log.info("jsonStr = {}", jsonArray.size());
			StringBuilder name = new StringBuilder();
			DateTime dateTime = DateTime.now();
			String dt = dateTime.toString(format) + "T00:00:00";
			boolean send = false;
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject json = jsonArray.get(i).getAsJsonObject();
				String date = json.get("STARTDATE").getAsString();
				String stockName = json.get("CORRESNAME").getAsString();
				if (dt.equals(date)) {
					send = true;
					name.append(dt + "  " + stockName);
					name.append("\n");
					log.info("date = {}, stockName = {}", date, stockName);
				}
			}
			if (send) {
				weChatNoticeUtils.setMessage("今天有如下债券申购，请及时申购：", name.toString());
			}
		} catch (Exception e) {
			log.error("e = {}", e);
			weChatNoticeUtils.setMessage("债券爬取失败，请您及时排查问题..", e.toString());
			return;
		}
	}

	private void executeStock() {
		try {
			String content = HttpHelper.get(stockUrl);
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
			log.info("jsonStr = {}", jsonArray.size());
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























