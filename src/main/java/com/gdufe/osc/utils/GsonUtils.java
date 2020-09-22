package com.gdufe.osc.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Gson工具类
 *
 * @author yangsong
 * @since 2020/6/11 11:15 上午
 */
@Slf4j
@Component
public class GsonUtils {

    private static Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .disableHtmlEscaping()
            .setVersion(1.0)
            .create();

    public static String toJson(Object src) {
        try {
            if (src instanceof String){
                return (String)src;
            }
            return gson.toJson(src);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        try {
            return gson.fromJson(jsonStr, clazz);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T fromJson(String json, TypeToken<T> type) {
        try {
            return gson.fromJson(json, type.getType());
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static JsonElement toJsonTree(Object src) {
        try {
            return gson.toJsonTree(src);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static JsonObject toJsonObject(Object src) {
        try {
            return gson.toJsonTree(src).getAsJsonObject();
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static JsonObject parse(String json) {
        if (StringUtils.isBlank(json)) {
           return null;
        }
        try {
            return (JsonObject) JsonParser.parseString(json);
        }catch (Exception e){
            log.error("parse json fail e ", e);
        }
        return null;
    }

    public static JsonObject parseWithNullable(String json) {
        if (StringUtils.isBlank(json)) {
            return new JsonObject();
        }
        try {
            return (JsonObject) JsonParser.parseString(json);
        }catch (Exception e){
            log.warn("parse json fail e ", e);
        }
        return new JsonObject();
    }
}
