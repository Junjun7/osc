package com.gdufe.osc.utils;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author changwenbo
 * @date 2019/11/27 17:31
 */
@Component
@Slf4j
public class HttpHelper {

	public static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";

	private HttpHelper() {}

	public static String get(String url) {
		HttpRequest request = HttpRequest.get(url)
				.header(Header.USER_AGENT, UA);
		String body = null;
		try {
			body = request.execute().body();
		} catch (Exception e) {
			log.error("get请求失败，message: {}", e.getMessage());
		}
		return body;
	}

	public static String get(String url, String cookie, Map<String, Object> map) {
		HttpRequest request = HttpRequest.get(url)
				.header(Header.USER_AGENT, UA);
		if (!CollectionUtils.isEmpty(map)) {
			request.form(map);
		}
		if (StringUtils.isNotEmpty(cookie)) {
			request.cookie(cookie);
		}
		String body = null;
		try {
			body = request.execute().body();
		} catch (Exception e) {
			log.error("get请求失败，message: {}", e.getMessage());
		}
		return body;
	}

	public static String post(String url, String json) {
		HttpRequest request = HttpRequest.post(url)
				.header(Header.USER_AGENT, UA);
		request.body(json);
		String body = "";
		try {
			HttpResponse execute = request.execute();
			body = execute.body();
		} catch (Exception e) {
			log.error("post请求失败，message: {}", e.getMessage());
		}
		return body;
	}

	public static String getCode() {
		String cookies = "_user_behavior_=e630430d-2736-4008-869d-e186330c487c; oscid=nM1CUeLEvOAfEgaKtB2C%2F6f7l2MdmwbMS4YhOZ%2FLJE3fjc5xFZi7nn4%2FDoeh9ZcJE6UMC%2FWo8tQPQgK1CTjg8uV2nVpeTtuzMlgYUQw6a3bZ2te1xz4t8Pmhw%2FYc1mC89nSv%2BTeXDjxQhhFRn2KDpochuiHXbnO80ZZEWRnEvpU%3D; aliyungf_tc=AQAAACstpXnRCQkAcvQ+t/+3DnL1HcUl; gitee-session-n=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJTNlZDEwYjNlNzA3OTk2NjVkYWYwNTIzODg3OTM4YjUzBjsAVEkiF21vYnlsZXR0ZV9vdmVycmlkZQY7AEY6CG5pbEkiEF9jc3JmX3Rva2VuBjsARkkiMS9scU5JUjJubjlSRDJPN1NzbElwZWQxa2YyWTd0bUwyZzJIclYwWkx5YXM9BjsARg%3D%3D--857244952ff89e85de5932e0cb1b82430729b2c0";
		String url = "https://www.oschina.net/action/oauth2/authorize";
		Map<String, Object> map = Maps.newHashMap();
		map.put("client_id", "sW9a1Tf8AP8IIbUydQrr");
		map.put("response_type", "code");
		map.put("redirect_uri", "https://www.wenber.com");
		map.put("scope", "comment_api,tweet_api,user_api,");
		map.put("state", "cwb");
		map.put("user_oauth_approval", "true");

		HttpRequest httpRequest = HttpRequest.post(url);
		httpRequest.header(Header.USER_AGENT, UA).cookie(cookies).form(map);
		String code = null;
		try {
			HttpResponse execute = httpRequest.execute();
			String location = execute.header("Location");
			code = StringUtils.substringBetween(location, "=", "&");
		} catch (Exception e) {
			log.error("获取code失败...", e.getMessage());
		}
		return code;
	}

	public static void main(String[] args) {
	}
}
