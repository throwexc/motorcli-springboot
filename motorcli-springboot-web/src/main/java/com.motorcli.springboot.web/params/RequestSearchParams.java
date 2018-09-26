package com.motorcli.springboot.web.params;

import com.motorcli.springboot.common.exceptions.DataConverterException;
import com.motorcli.springboot.common.utils.ClassUtils;
import com.motorcli.springboot.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询参数封装类
 * 用与封装组合查询参数对象
 */
public abstract class RequestSearchParams {

    /**
     * 日期类型
     */
    protected enum DateType {

        /**
         * 日期
         * yyyy-MM-dd
         */
        DATE,

        /**
         * 日期时间
         * yyyy-MM-dd hh:mm;ss
         */
        DATETIME
    }

    /**
     * Servlet Request 对象
     */
    protected HttpServletRequest request;

    /**
     * 查询参数 Map 对象
     */
    protected Map<String, String> paramMap = new HashMap<>();

    public RequestSearchParams(HttpServletRequest request) {
        this.request = request;
        this.readParams();
        this.mappingParams();
        this.setValues();
    }

    /**
     * 读取参数
     */
    private void readParams() {
        Enumeration<String> paramNames = this.request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String value = this.request.getParameter(name);
            if(!StringUtils.isEmpty(value)) {
                this.paramMap.put(name, value);
            }
        }
    }

    protected String dateTimeFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }

    protected String dateFormat() {
        return "yyyy-MM-dd";
    }

    private void mappingParams() {
        ClassUtils.mappingObjectByMap(this.paramMap, this, this.dateFormat(), this.dateTimeFormat());
    }

    /**
     * 值设置方法
     */
    protected void setValues() {}

    /**
     * 获取参数值
     * @param name 参数名
     * @return 参数值
     */
    protected String getParam(String name) {
        return this.paramMap.get(name);
    }

    /**
     * 通过参数值转换日期
     * 该方法将判断字符串长度，根据字符串长度进行日期转换
     * @param value 日期字符串
     * @return 转换的日期
     */
    protected Date valueToDate(String value) throws DataConverterException {
        if(this.dateTimeFormat().length() == value.length() ) {
            return valueToDate(value, DateType.DATETIME);
        } else {
            return valueToDate(value, DateType.DATE);
        }
    }

    /**
     * 通过参数值转换日期
     * @param value 值
     * @param dateType 日期类型
     * @return 日期对象
     * @throws DataConverterException 在非正常日期格式字符串情况下转换会发生异常
     */
    protected Date valueToDate(String value, DateType dateType) throws DataConverterException {
        try {
            if (dateType == DateType.DATE ) {
                if(this.dateFormat().length() == value.length()) {
                    return DateUtils.parseDate(value, this.dateFormat());
                } else {
                    throw new DataConverterException("日期格式错误, 需要转换的日期为 [" + this.dateFormat() + "]");
                }
            }
            if (dateType == DateType.DATETIME) {
                if(this.dateTimeFormat().length() == value.length()) {
                    return DateUtils.parseDate(value, this.dateTimeFormat());
                } else {
                    throw new DataConverterException("日期格式错误, 需要转换的日期为 [" + this.dateTimeFormat() + "]");
                }
            }
            return null;
        } catch (ParseException e) {
            throw new DataConverterException("日期格式错误", e);
        }
    }
}
