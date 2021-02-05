package com.gdufe.osc;

import com.gdufe.osc.entity.AccessToken;
import com.gdufe.osc.entity.Person;
import com.gdufe.osc.utils.gson.GsonUtils;

/**
 * @author changwenbo
 * @date 2020/9/14 10:24
 */
public class Main {
	public static void main(String[] args) {
		Person person = new Person();
		person.setAge(18);
		person.setName("zhangsan");

		System.out.println(GsonUtils.toJson(person));

		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken("11111111122222333333333");
		accessToken.setRefreshToken("222222333333444444444");
		System.out.println(GsonUtils.toJson(accessToken));
	}
}
