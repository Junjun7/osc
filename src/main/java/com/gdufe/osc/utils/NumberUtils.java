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
}
