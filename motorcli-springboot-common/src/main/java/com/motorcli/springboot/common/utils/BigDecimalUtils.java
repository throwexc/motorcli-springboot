package com.motorcli.springboot.common.utils;

import java.math.BigDecimal;

/**
 * 高精度数字工具类
 * 从非字符串对象构建高精度对象时，会产生精度丢失
 * 所以都应从字符进行构建
 */
public class BigDecimalUtils {

    /**
     * 创建高精度对象
     * @param val 数值型的值
     * @return 高精度对象
     */
    public static BigDecimal create(Double val) {
        if(val == null) {
            val = 0.0;
        }
        return  create(val + "");
    }

    /**
     * 创建高精度对象
     * @param val 数值型的值
     * @return 高精度对象
     */
    public static BigDecimal create(Float val) {
        if(val == null) {
            val = 0.0f;
        }
        return  create(val + "");
    }

    /**
     * 创建高精度对象
     * @param val 数值型的值
     * @return 高精度对象
     */
    public static BigDecimal create(Integer val) {
        if(val == null) {
            val = 0;
        }
        return  create(val + "");
    }

    /**
     * 创建高精度对象
     * @param val 数值型的值
     * @return 高精度对象
     */
    public static BigDecimal create(Long val) {
        if(val == null) {
            val = 0L;
        }
        return create(val + "");
    }

    /**
     * 创建高精度对象
     * @param val 数值型的值
     * @return 高精度对象
     */
    public static BigDecimal create(String val) {
        return new BigDecimal(val);
    }
}
