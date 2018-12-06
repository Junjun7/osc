package com.gdufe.osc.utils;

import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import org.apache.http.Header;
import org.springframework.stereotype.Component;

/**
 * @Auther: Chang
 * @Date: 2018/10/3
 */
@Component
public class HttpMethod {

	public static String get(String url) {
		HttpConfig config = getConfig();
		String content = null;
		try {
			content = HttpClientUtil.get(config.url(url));
		} catch (HttpProcessException e) {
			e.printStackTrace();
		}
		return content;
	}

	private static HttpConfig getConfig() {
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
		Header[] headers= HttpHeader.custom()
				.userAgent(userAgent)
				.build();
		HttpConfig config = HttpConfig.custom().headers(headers);
		return config;
	}
}




