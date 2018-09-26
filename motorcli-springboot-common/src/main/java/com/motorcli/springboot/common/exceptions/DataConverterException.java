package com.motorcli.springboot.common.exceptions;

/**
 * 数据转换异常
 */
public class DataConverterException extends BaseException implements ExceptionCode {

    public DataConverterException(String message) {
        super(message);
    }

    public DataConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int errorCode() {
        return DATA_CONVERTER_EXCEPTION_CODE;
    }
}
