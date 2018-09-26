package com.motorcli.springboot.common.utils;

import java.util.*;

/**
 * 集合对象辅助类
 */
public class CollectionUtils {

    /**
     * 判断数组是否为空数组
     * @param array 数组
     * @return <tt>true</tt> 空数组
     *         <br>
     *         <tt>false</tt> 非空数组
     */
    public static boolean isEmpty(Object[] array) {
        if(array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断集合是否为空集合
     * @param collection 集合对象
     * @return <tt>true</tt> 空集合
     *         <br>
     *         <tt>false</tt> 非空集合
     */
    public static boolean isEmpty(Collection<?> collection) {
        if(collection == null || collection.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断 {@code Map} 是否为空
     * @param map {@code Map} 对象
     * @return <tt>true</tt> 空 {@code Map}
     *         <br>
     *         <tt>false</tt> 非空 {@code Map}
     */
    public static boolean isEmpty(Map<?, ?> map) {
        if(map == null || map.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断 {@code Map<String, Object>} 集合中指定 {@code Key} 的值是否为空
     * @param map {@code Map} 对象
     * @param key {@code Map} 对象中的键值
     * @return <tt>true</tt> 空值
     *         <br>
     *         <tt>false</tt> 非空值
     */
    public static boolean isEmpty(Map<String, Object> map, String key) {
        if(map.get(key) != null && !"".equals(map.get(key).toString())) {
            return false;
        }
        return true;
    }

    /**
     * 把 {@code Map<String, Object>} 中的 {@code Key} 转换为大写，构建一个新的 {@code Map<String, Object>}
     * @param map {@code Map} 对象
     * @return 一个键值全为大写字母的新 {@code Map<String, Object>} 对象
     */
    public static Map<String, Object> mapKeysToUpperCase(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<>();
        for(String key : map.keySet()) {
            resultMap.put(key.toUpperCase(), map.get(key));
        }
        return resultMap;
    }

    /**
     * 把 {@code Map<String, Object>} 中的 {@code Key} 转换为小写， 构建一个新的 {@code Map<String, Object>}
     * @param map {@code Map} 对象
     * @return 一个键值全为小写字母的新 {@code Map} 对象
     */
    public static Map<String, Object> mapKeysToLowerCase(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<>();
        for(String key : map.keySet()) {
            resultMap.put(key.toLowerCase(), map.get(key));
        }
        return resultMap;
    }

    /**
     * 把 {@code Collection<String>} 中的字符串对象以 {@code split} 拼接
     * @param collection 字符串集合
     * @param split 分隔符
     * @return 以 {@code split} 分割连接成的字符串
     */
    public static String toString(Collection<String> collection, String split) {
        if (collection == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag=false;
        for (String string : collection) {
            if (flag) {
                result.append(split);
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }


    /**
     * 把 {@code Collection<String>} 中的字符串对象以 {@code ,} 拼接
     * @param collection 字符串集合
     * @return 以 {@code ","} 分割连接成的字符串
     */
    public static String toString(Collection<String> collection){
        return toString(collection, ",");
    }

    /**
     * 把字符串对象以 {@code split} 分隔，构建一个 {@code List<String>} 集合
     * @param string 字符串
     * @param split 分隔符
     * @return {@code List<String>} 集合
     */
    public static List<String> toList(String string, String split) {
        String array[] = string.split(split);
        List<String> result = new ArrayList<>();
        for(String s : array) {
            result.add(s);
        }
        return result;
    }

    /**
     * 把字符串对象以 {@code split} 分隔，构建一个 {@code Set<String>} 集合
     * @param string 字符串
     * @param split 分隔符
     * @return {@code Set<String>} 集合
     */
    public static Set<String> toSet(String string, String split) {
        String array[] = string.split(split);
        Set<String> result = new HashSet<>();
        for(String s : array) {
            result.add(s);
        }
        return result;
    }

    /**
     * 把字符串对象以 {@code ","} 分隔，构建一个 {@code List<String>} 集合
     * @param string 字符串
     * @return {@code List<String>} 集合
     */
    public static List<String> toList(String string) {
        return toList(string, ",");
    }

    /**
     * 把字符串对象以 {@code ","} 分隔，构建一个 {@code Set<String>} 集合
     * @param string 字符串
     * @return {@code Set<String>} 集合
     */
    public static Set<String> toSet(String string) {
        return toSet(string, ",");
    }

    /**
     * 把集合对象转为 {@code List<T>} 集合
     * @param collection 集合对象
     * @param <T> 泛型类型
     * @return {@code List<T>} 集合
     */
    public static <T> List<T> toList(Collection<T> collection) {
        List<T> list = new ArrayList<>();
        for(T t : collection) {
            list.add(t);
        }
        return list;
    }

    /**
     * 把集合对象转为 {@code Set} 集合
     * @param collection 集合对象
     * @param <T> 泛型类型
     * @return {@code Set<T>} 集合
     */
    public static <T> Set<T> toSet(Collection<T> collection) {
        Set<T> set = new HashSet<T>();
        for(T t : collection) {
            set.add(t);
        }
        return set;
    }
}
