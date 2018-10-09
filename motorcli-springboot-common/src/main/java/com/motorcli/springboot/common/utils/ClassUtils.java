package com.motorcli.springboot.common.utils;

import com.motorcli.springboot.common.exceptions.ClassReflectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ClassUtils {

    /**
     * 通过一个类， 创建一个类的实例
     * @param clz 类对象
     * @param <T> 类类型
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T createInstance(final Class<T> clz) throws IllegalAccessException, InstantiationException {
        T obj = clz.newInstance();
        return obj;
    }

    /**
     * 映射值到一个对象的属性上
     * @param value 值
     * @param targetObject 目标对象
     * @param targetField 目标字段
     * @param dateFormat 日期格式
     * @param dateTimeFormat 日期时间格式
     * @param <X> 目标对象类型
     */
    public static <X> void mappingValue(final Object value, final X targetObject, final Field targetField, final String dateFormat, final String dateTimeFormat) {
        String targetType = targetField.getType().getName();

        ReflectionUtils.makeAccessible(targetField);

        // 为空则为非基础类型属性赋值， 基础属性忽略
        if(value == null) {
            if(targetType.indexOf(".") != -1) {
                ReflectionUtils.setField(targetField, targetObject, value);
            }
            return;
        }

        String valueType = value.getClass().getName();

        // 类型相等， 直接转换
        if (targetType.equals(valueType)) {
            ReflectionUtils.setField(targetField, targetObject, value);
        } else {
            if (targetType.equals("java.lang.String")) { // 目标类型为字符串
                if (valueType.equals("java.util.Date")) {
                    ReflectionUtils.setField(targetField, targetObject, DateUtils.formatDate((Date) value, dateTimeFormat));
                } else if(value instanceof List || value instanceof Set || value instanceof Map) { // 不做集合到字符串的映射
                    return;
                } else {
                    ReflectionUtils.setField(targetField, targetObject, value.toString());
                }
            } else if (targetType.equals("java.math.BigDecimal")) { // 目标类型为高精度
                if (valueType.equals("int")
                        || valueType.equals("long")
                        || valueType.equals("float")
                        || valueType.equals("double")
                        || valueType.equals("java.lang.Integer")
                        || valueType.equals("java.lang.Long")
                        || valueType.equals("java.lang.Float")
                        || valueType.equals("java.lang.Double")
                        || valueType.equals("java.lang.String")) {
                    ReflectionUtils.setField(targetField, targetObject, BigDecimalUtils.create(value.toString()));
                }
            } else if (targetType.equals("java.util.Date")) { // 目标类型为日期，并且源类型为字符串
                if (valueType.equals("java.lang.String")) {
                    try {
                        if (dateTimeFormat.length() > value.toString().length()) {
                            ReflectionUtils.setField(targetField, targetObject, DateUtils.parseDate(value.toString(), dateFormat));
                        } else {
                            ReflectionUtils.setField(targetField, targetObject, DateUtils.parseDate(value.toString(), dateTimeFormat));
                        }
                    } catch (ParseException e) {
                        throw new ClassReflectionException("日期字符串转换成日期错误 [" + value + "]", e);
                    }
                } else if (valueType.equals("long") || valueType.equals("java.lang.Long")) {
                    ReflectionUtils.setField(targetField, targetObject, new Date((Long) value));
                }

            } else if (targetType.equals("boolean") || targetType.equals("java.lang.Boolean")) { // 逻辑型转换
                if(valueType.equals("boolean")|| valueType.equals("java.lang.Boolean")) {
                    ReflectionUtils.setField(targetField, targetObject, value);
                }
            } else if (targetType.equals("int") || targetType.equals("java.lang.Integer")) {
                if (valueType.equals("java.lang.Integer") ) {
                    ReflectionUtils.setField(targetField, targetObject, value);
                } else if (valueType.equals("java.lang.String")) {
                    ReflectionUtils.setField(targetField, targetObject, Integer.parseInt(value.toString()));
                }
            } else if (targetType.equals("long") || targetType.equals("java.lang.Long")) {
                if (valueType.equals("java.lang.Long")) {
                    ReflectionUtils.setField(targetField, targetObject, value);
                } else if (valueType.equals("java.lang.String")) {
                    ReflectionUtils.setField(targetField, targetObject, Long.parseLong(value.toString()));
                }
            } else if (targetType.equals("float") || targetType.equals("java.lang.Float")) {
                if (valueType.equals("java.lang.Float")) {
                    ReflectionUtils.setField(targetField, targetObject, value);
                } else if (valueType.equals("java.lang.String")) {
                    ReflectionUtils.setField(targetField, targetObject, Float.parseFloat(value.toString()));
                }
            }  else if (targetType.equals("double") || targetObject.equals("java.lang.Double")) {
                if (valueType.equals("java.lang.Double")) {
                    ReflectionUtils.setField(targetField, targetObject, value);
                } else if (valueType.equals("java.lang.String")) {
                    ReflectionUtils.setField(targetField, targetObject, Double.parseDouble(value.toString()));
                }
            }
        }
    }

    /**
     * 映射字段值
     * @param srcObject 源对象
     * @param srcField 源字段
     * @param targetObject 目标对象
     * @param targetField 目标字段
     * @param dateFormat 日期格式
     * @param dateTimeFormat 日期时间格式
     * @param <T> 源对象类型
     * @param <X> 目标对象类型
     */
    public static <T, X> void mappingField(final T srcObject, final Field srcField, final X targetObject, final Field targetField, final String dateFormat, final String dateTimeFormat) {

        ReflectionUtils.makeAccessible(srcField);

        Object value = ReflectionUtils.getField(srcField, srcObject);

        mappingValue(value, targetObject, targetField, dateFormat, dateTimeFormat);
    }

    /**
     * 对象值映射
     * @param srcObject 源对象
     * @param targetObject 目标对象
     * @param dateFormat 日期格式
     * @param dateTimeFormat 日期时间格式
     * @param <T> 源对象类型
     * @param <X> 目标对象类型
     */
    public static <T, X> void mappingObject(final T srcObject, final X targetObject, final String dateFormat, final String dateTimeFormat) {
        ReflectionUtils.doWithFields(targetObject.getClass(), field -> {
            Field srcField = ReflectionUtils.findField(srcObject.getClass(), field.getName());

            if(srcField != null) {
                mappingField(srcObject, srcField, targetObject, field, dateFormat, dateTimeFormat);
            }
        }, ReflectionUtils.COPYABLE_FIELDS);
    }

    /**
     * 映射对象值
     * @param srcMap 源 Map 对象
     * @param targetObject 目标对象
     * @param dateFormat 日期格式
     * @param dateTimeFormat 日期时间格式
     * @param <X> 目标对象类型
     */
    public static <X, V extends Object> void mappingObjectByMap(final Map<String, V> srcMap, final X targetObject, final String dateFormat, final String dateTimeFormat) {

        ReflectionUtils.doWithFields(targetObject.getClass(), field -> {
            if(srcMap.containsKey(field.getName())) {
                Object value = srcMap.get(field.getName());
                mappingValue(value, targetObject, field, dateFormat, dateTimeFormat);
            }
        }, ReflectionUtils.COPYABLE_FIELDS);
    }
}
