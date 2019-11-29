package com.gdufe.osc.utils;

import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Chang
 * @Date: 2018/10/3
 */
@Component
@Slf4j
public class HttpMethod {

	private HttpMethod() {}

	public static String get(String url) {
		HttpConfig config = getConfig();
		String content = null;
		try {
			content = HttpClientUtil.get(config.url(url));
		} catch (HttpProcessException e) {
			log.error("get请求失败，message: {}", e.getMessage());
		}
		return content;
	}

	@Deprecated
	public static String get(String url, Header[] headers) {
		String content = null;
		try {
			content = HttpClientUtil.get(null, url, headers, null, null);
		} catch (HttpProcessException e) {
			log.error("get请求失败，message: {}", e.getMessage());
		}
		return content;
	}

	@Deprecated
	public static String post(String url, Header[] headers, Map<String, Object> map) {
		String content = null;
		try {
			content = HttpClientUtil.post(null, url, headers, map, null, null);
		} catch (HttpProcessException e) {
			log.error("post请求失败，message: {}", e.getMessage());
		}
		return content;
	}

	@Deprecated
	public static String post(String url, Map<String, Object> map) {
		String content = null;
		try {
			content = HttpClientUtil.post(null, url, getHeader(), map, null, null);
		} catch (HttpProcessException e) {
			e.printStackTrace();
		}
		return content;
	}

	@Deprecated
	public static String post(HttpConfig config) {
		String content = null;
		try {
			content = HttpClientUtil.post(config);
		} catch (HttpProcessException e) {
			log.error("post请求失败，message: {}", e.getMessage());
		}
		return content;
	}

	public static String getCode() throws IOException, NullPointerException {
		Map<String, Object> map = getMaps();
		List<NameValuePair> list = Lists.newArrayList();
		map.forEach((x, y) -> list.add(new BasicNameValuePair(x, y.toString())));
		Header[] headers = getHeader();
		CloseableHttpClient client = null;
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		try {
			String url = "https://www.oschina.net/action/oauth2/authorize";
			client = HttpClients.createDefault();
			httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(list));
			httpPost.setHeaders(headers);
			response = client.execute(httpPost);
			headers = response.getAllHeaders();
		} finally {
			response.close();
			client.close();
			httpPost.releaseConnection();
		}
		String code = getInstanceCode(headers);
		if (StringUtils.isEmpty(code)) {
			throw new NullPointerException();
		}
		return code;
	}

	private static String getInstanceCode(Header[] headers) {
		String value = "";
		for (Header header : headers) {
			if ("Location".equals(header.getName())) {
				value = header.getValue();
			}
		}
		value = StringUtils.replaceOnce(value, "code=", "&");
		String str = StringUtils.substringBetween(value, "&");
		return str;
	}

	private static Map<String, Object> getMaps() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("client_id", "sW9a1Tf8AP8IIbUydQrr");
		map.put("response_type", "code");
		map.put("redirect_uri", "https://www.wenber.com");
		map.put("scope", "comment_api,tweet_api,user_api,");
		map.put("state", "cwb");
		map.put("user_oauth_approval", "true");
		return map;
	}

	private static HttpConfig getConfig() {
		Header[] headers = getHeader();
		HttpConfig config = HttpConfig.custom().headers(headers);
		return config;
	}

	private static Header[] getHeader() {
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
		String cookies = "_user_behavior_=e630430d-2736-4008-869d-e186330c487c; oscid=nM1CUeLEvOAfEgaKtB2C%2F6f7l2MdmwbMS4YhOZ%2FLJE3fjc5xFZi7nn4%2FDoeh9ZcJE6UMC%2FWo8tQPQgK1CTjg8uV2nVpeTtuzMlgYUQw6a3bZ2te1xz4t8Pmhw%2FYc1mC89nSv%2BTeXDjxQhhFRn2KDpochuiHXbnO80ZZEWRnEvpU%3D; aliyungf_tc=AQAAACstpXnRCQkAcvQ+t/+3DnL1HcUl; gitee-session-n=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJTNlZDEwYjNlNzA3OTk2NjVkYWYwNTIzODg3OTM4YjUzBjsAVEkiF21vYnlsZXR0ZV9vdmVycmlkZQY7AEY6CG5pbEkiEF9jc3JmX3Rva2VuBjsARkkiMS9scU5JUjJubjlSRDJPN1NzbElwZWQxa2YyWTd0bUwyZzJIclYwWkx5YXM9BjsARg%3D%3D--857244952ff89e85de5932e0cb1b82430729b2c0";
		Header[] headers = HttpHeader.custom()
				.userAgent(userAgent)
				.cookie(cookies)
				.build();
		return headers;
	}

	public static void main(String[] args) {
	}
}




