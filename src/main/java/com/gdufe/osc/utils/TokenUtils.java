package com.gdufe.osc.utils;

import org.springframework.stereotype.Component;

/**
 * @Author: yizhen
 * @Date: 2018/12/6 11:19
 */
@Component
public class TokenUtils {

	public static String getToken() {

		return CacheToken.getToken();
	}

	public static String getFreshToken() {

		return CacheToken.getFreshToken();
	}
}
