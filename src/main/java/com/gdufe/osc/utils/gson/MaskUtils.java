package com.gdufe.osc.utils.gson;

/**
 * @author changwenbo
 * @date 2021/2/5 10:36
 */
public class MaskUtils {

	private static String STARS = "********************";
	private static String HALF_STARS = "**********";

	public static String getSecretDisplayStr(String str) {
		if (str == null) {
			return null;
		} else if (str.isEmpty()) {
			return "";
		} else {
			int len = str.length();
			if (len <= 2) {
				return str.charAt(0) + getStars(len - 1);
			} else {
				int left = len / 3;
				left = left > 4 ? 4 : left;
				return str.substring(0, left) + getStars(len - left - left) + str.substring(len - left);
			}
		}
	}

	private static String getStars(int num) {
		if (num == 0) {
			return "";
		} else {
			return num <= 20 ? STARS.substring(0, num) : HALF_STARS + (num - 20) + HALF_STARS;
		}
	}
}
