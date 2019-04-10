package com.gdufe.osc.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @author: yizhen
 * @date: 2019/4/10 11:16
 */
@Component
public class SecretUtils {

	/** 30秒token值失效 */
	private static final long NONCE_DURATION = 30000L;
	/** 构造字符串的盐值 */
	private static final String SALT = "$$$zxy###cwb$$$";

	public static boolean extractSecret(String timestamp, String token) {
		if (StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(token)) {
			return false;
		}
		long ts = NumberUtils.toLong(timestamp, 0);
		long now = System.currentTimeMillis();
		if ((now - ts) > SecretUtils.NONCE_DURATION) {
			return false;
		}
		String targetToken = DigestUtils.md5DigestAsHex(buildKey(timestamp).getBytes());
		return token.equals(targetToken);
	}

	private static String buildKey(String timestamp) {
		return SALT + "_" + timestamp;
	}
}

















