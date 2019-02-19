package com.gdufe.osc.utils;

import com.alibaba.fastjson.JSON;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author: yizhen
 * @date: 2019/2/18 14:09
 */
@Component
public class WeChatNoticeUtils {

	@Value("${userIds}")
	private String userIds;
	@Value("${template_id}")
	private String templateId;

	private static final String URL = "http://wxmsg.dingliqc.com/send";

	public Boolean setMessage(String content) {
		return setMessage("", content);
	}

	public Boolean setMessage(String title, String content) {
		Map<String, Object> res = Maps.newHashMap();
		res.put("userIds", Lists.newArrayList(userIds).toArray());
		res.put("template_id", templateId);
		Map<String, Object> data = Maps.newHashMap();
		fillDataMap(data, title, content);
		res.put("data", data);
		HttpConfig config = getHttpConfig(URL, JSON.toJSONString(res));
		String post = HttpMethod.post(config);
		if (StringUtils.isNotEmpty(post) && post.contains("200")) {
			return true;
		}
		return false;
	}

	private HttpConfig getHttpConfig(String url, String data) {
		Header[] headers = HttpHeader.custom()
				.contentType("application/json")
				.build();
		HttpConfig config = HttpConfig.custom()
				.json(data)
				.url(url)
				.headers(headers);
		return config;
	}

	private void fillDataMap(Map<String, Object> data, String title, String content) {
		// 标题
		Map<String, String> titleMap = Maps.newHashMap();
		titleMap.put("value", title);
		titleMap.put("color", "#333333");
		data.put("first", titleMap);
		// 紧急程度
		Map<String, String> levelMap = Maps.newHashMap();
		levelMap.put("value", "紧急");
		levelMap.put("color", "#ff0000");
		data.put("keyword1", levelMap);
		// 无意义
		Map<String, String> mapNoMeaning = Maps.newHashMap();
		mapNoMeaning.put("value", "");
		mapNoMeaning.put("color", "#ff0000");
		data.put("keyword2", mapNoMeaning);
		// 时间
		Map<String, String> dateMap = Maps.newHashMap();
		dateMap.put("value", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		dateMap.put("color", "#333333");
		data.put("keyword3", dateMap);
		// 具体内容
		Map<String, String> contentMap = Maps.newHashMap();
		contentMap.put("value", content);
		contentMap.put("color", "#333333");
		data.put("remark", contentMap);
	}
}
