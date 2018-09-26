package com.motorcli.springboot.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {

    /**
     * 字符串转日期
     * @param dateStr 日期字符串
     * @param formatStr 日期格式化字符串
     * @return 日期对象
     * @throws ParseException 日期字符串非法时，抛出该异常
     * @see java.text.SimpleDateFormat
     */
    public static Date parseDate(String dateStr, String formatStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.parse(dateStr);
    }

    /**
     * 字符串转日期
     * @param dateStr {@code yyyy-MM-dd} 日期字符串
     * @return 日期对象
     * @throws ParseException 日期字符串非法时，抛出该异常
     */
    public static Date parseDate(String dateStr) throws ParseException {
        return parseDate("yyyy-MM-dd", dateStr);
    }

    /**
     * 字符串转日期
     * @param dateStr {@code yyyy-MM-dd HH:mm:ss} 日期字符串
     * @return 日期对象
     * @throws ParseException 日期字符串非法时，抛出该异常
     */
    public static Date parseDateTime(String dateStr) throws ParseException {
        return parseDate("yyyy-MM-dd HH:mm:ss", dateStr);
    }

    /**
     * 日期转字符串
     * @param date 日期对象
     * @param formatStr 格式化字符串
     * @return 日期字符串
     * @see java.text.SimpleDateFormat
     */
    public static String formatDate(Date date, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    /**
     * 日期转字符串
     * @param date {@code yyyy-MM-dd} 日期对象
     * @return 日期字符串
     */
    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 日期转字符串
     * @param date {@code yyyy-MM-dd HH:mm:ss} 日期对象
     * @return 日期字符串
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 计算两个时间的差，返回分钟数
     * 取值方式为差的绝对值
     * @param date1 第一个日期对象
     * @param date2 第二个日期对象
     * @return 分钟数
     */
    public static int subDateOfMinute(Date date1, Date date2) {
        int minute = subDateOfSecond(date1,date2)/60;
        return minute;
    }

    /**
     * 计算两个时间的差，返回秒数
     * 取值方式为差的绝对值
     * @param date1 第一个日期对象
     * @param date2 第二个日期对象
     * @return 秒数
     */
    public static int subDateOfSecond(Date date1, Date date2) {
        int second = Math.abs((int)(date1.getTime() - date2.getTime())) / 1000;
        return second;
    }
}
