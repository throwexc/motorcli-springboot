package com.motorcli.springboot.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具
 */
@Slf4j
public class JsonUtils {

    /**
     * 对象转 json 字符串
     * @param object 对象
     * @param isPretty 是否带换行格式
     * @return json 字符串
     */
    public static String toJson(ObjectMapper objectMapper, Object object, boolean isPretty) {
        String jsonString = "";
        try {
            if (isPretty) {
                jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } else {
                jsonString = objectMapper.writeValueAsString(object);
            }
        } catch (JsonProcessingException e) {
            log.error("从对象转换为 JSON 发生错误", e);
        }
        return jsonString;
    }

    /**
     * json 字符串转对象
     *
     * @param jsonString json 字符串
     * @param clz 要转换的目标类型
     * @return 转换后的对象
     */
    public static <X> X jsonToObject(ObjectMapper objectMapper, String jsonString, Class<X> clz) {
        if (jsonString == null || "".equals(jsonString)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(jsonString, clz);
            } catch (Exception e) {
                log.error("从一个 JSON 字符串转化为对象发生错误", e);
            }

        }
        return null;
    }

    /**
     * json 字符串转 {@code Map<String, Object>} 对象
     * @param jsonString json 字符串
     * @return {@code Map<String, Object>} 对象
     */
    public static Map<String, Object> jsonStrToMap(ObjectMapper objectMapper, String jsonString) throws IOException {
        Map result = objectMapper.readValue(jsonString, HashMap.class);
        return result;
    }

    /**
     * json 字节数组转 {@code Map<String, Object>} 对象
     * @param bytes json 字节数组
     * @param charset 编码格式
     * @return {@code Map<String, Object>} 对象
     */
    public static Map<String, Object> jsonByteToMap(ObjectMapper objectMapper, byte[] bytes, String charset) throws IOException {
        if(bytes != null) {
            return jsonStrToMap(objectMapper, new String(bytes, charset));
        }
        return null;
    }

    /**
     * json 字节数组转 {@code List<Map<String, Object>>} 对象
     * @param jsonString json 字符串
     * @return {@code List<Map<String, Object>>} 对象
     */
    public static List<Map<String, Object>> jsonStrToListMap(ObjectMapper objectMapper, String jsonString) throws IOException {
        List result = objectMapper.readValue(jsonString, ArrayList.class);
        return result;
    }

    /**
     * json 字节数组转 {@code List<X>} 对象
     * @param <X> List 集合的泛型类型
     * @param jsonString json 字符串
     * @return {@code List<X>} 对象
     */
    public static <X> List<X> jsonStrToList(ObjectMapper objectMapper, String jsonString) throws IOException {
        List<X> result = objectMapper.readValue(jsonString, ArrayList.class);
        return result;
    }
}
