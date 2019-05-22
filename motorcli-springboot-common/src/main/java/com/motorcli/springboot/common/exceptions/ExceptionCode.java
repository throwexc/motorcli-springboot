package com.motorcli.springboot.common.exceptions;

public interface ExceptionCode {

    /**
     * 类反射异常编码
     */
    int CLASS_REFLECTION_EXCEPTION = -9;

    /**
     * 数据转换异常编码
     */
    int DATA_CONVERTER_EXCEPTION_CODE = -10;

    /**
     * 参数错误
     */
    int PARAM_ERROR_EXCEPTION_CODE = -9999;

    /**
     * 文件异常编码
     */
    int FILE_EXCEPTION_CODE = - 10001;
}
