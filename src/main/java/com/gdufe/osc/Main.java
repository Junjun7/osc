package com.gdufe.osc;

import com.gdufe.osc.utils.GsonUtils;
import com.google.gson.JsonObject;

/**
 * @author changwenbo
 * @date 2020/9/14 10:24
 */
public class Main {
	public static void main(String[] args) {
		String str = "{\"imgBig\":\"https://static.oschina.net/uploads/space/https://oscimg.oschina.net/oscnet/up-62a2f83cd10044693313f23a0df052cf4c5.png\",\"author\":\"Andy市民\",\"id\":22119730,\"portrait\":\"https://oscimg.oschina.net/oscnet/up-c433d2b39ed5624fd813783da456026b.jpg!/both/50x50?t=1444975720000\",\"authorid\":1252840,\"body\":\"我比较懒！\",\"pubDate\":\"2020-09-22 11:16:23\",\"imgSmall\":\"https://static.oschina.net/uploads/space/https://oscimg.oschina.net/oscnet/up-62a2f83cd10044693313f23a0df052cf4c5.png!/sq/200\",\"commentCount\":3}";

		String s = GsonUtils.toJson(str);
		System.out.println(s);

		JsonObject parse = GsonUtils.parse(str);
		System.out.println(parse.get("imgBig").getAsString());

	}
}
