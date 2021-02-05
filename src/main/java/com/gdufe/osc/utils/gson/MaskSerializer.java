package com.gdufe.osc.utils.gson;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * @author changwenbo
 * @date 2021/2/5 10:44
 */
@Slf4j
public class MaskSerializer implements JsonSerializer<Object> {

	Gson gson = new Gson();

	/** 正则匹配，只要key出现在realKey中，则掩码 */
	private static final Set<String> SENSITIVE_INFO_SET = Sets.newHashSet("name", "token");

	@Override
	public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
		if (null == o) {
			return null;
		}
		JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
		for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			String realKey = entry.getKey();
			if (needMask(realKey)) {
				jsonObject.addProperty(realKey, MaskUtils.getSecretDisplayStr(jsonObject.get(realKey).getAsString()));
			}
		}

		return jsonObject;
	}

	private boolean needMask(String realKey) {
		if (StringUtils.isEmpty(realKey)) {
			return false;
		}
		for (String key : SENSITIVE_INFO_SET) {
			if (realKey.contains(key)) {
				return true;
			}
		}
		return false;
	}
}
