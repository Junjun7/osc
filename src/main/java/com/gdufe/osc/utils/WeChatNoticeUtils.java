package com.gdufe.osc.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
		String post = HttpHelper.post(URL, new Gson().toJson(res));
		if (StringUtils.isNotEmpty(post) && post.contains("200")) {
			return true;
		}
		return false;
	}

	private void fillDataMap(Map<String, Object> data, String title, String content) {
		// 标题
		Map<String, String> titleMap = Maps.newHashMap();
		titleMap.put("value", title);
		titleMap.put("color", "#333333");
		data.put("first", titleMap);
		Map<String, String> contentMap = Maps.newHashMap();
		contentMap.put("value", content);
		contentMap.put("color", "#333333");
		data.put("remark", contentMap);
	}
}
