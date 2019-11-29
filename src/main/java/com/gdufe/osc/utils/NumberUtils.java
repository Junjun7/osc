package com.gdufe.osc.utils;

public class NumberUtils {

	public static boolean isNumber(String str) {
		if(str == null || str.length() == 0) {
			return false;
		}
		try {
			Integer.parseInt(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static int toInt(final String str) {
		return toInt(str, 0);
	}

	public static int toInt(final String str, final int defaultValue) {
		if(str == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(str);
		} catch (final NumberFormatException nfe) {
			return defaultValue;
		}
	}
}
