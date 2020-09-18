package com.gdufe.osc;

import org.joda.time.DateTime;

/**
 * @author changwenbo
 * @date 2020/9/14 10:24
 */
public class Main {
	public static void main(String[] args) {

		String format = "yyyy-MM-dd";

		DateTime dateTime = DateTime.now();
		String s = dateTime.toString(format);
		System.out.println(s);
	}
}
