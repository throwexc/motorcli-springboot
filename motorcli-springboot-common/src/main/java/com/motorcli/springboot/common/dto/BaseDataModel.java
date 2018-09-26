package com.motorcli.springboot.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.motorcli.springboot.common.utils.ClassUtils;

/**
 *  DTO 基类
 *
 *  可 JSON 序列化，去除 JSON 中 值为空的 KEY
 */
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseDataModel {

    protected String dateTimeFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }

    protected String dateFormat() {
        return "yyyy-MM-dd";
    }

    /**
     * 通过一个对象， 把对象的值映射到类中的属性上
     * @param object 值对象
     * @param <T> 对象类型
     */
    public <T> void mapByObject(T object) {
        ClassUtils.mappingObject(object, this, this.dateFormat(), this.dateTimeFormat());
    }

    /**
     * 把类中的属性值，映射到对象中
     * @param object 值对象
     * @param <T> 对象类型
     */
    public <T> void mapToObject(T object) {
        ClassUtils.mappingObject(this, object, this.dateFormat(), this.dateTimeFormat());
    }
}
