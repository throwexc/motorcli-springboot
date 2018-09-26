package com.motorcli.springboot.common.exceptions;

/**
 * 类反射异常
 */
public class ClassReflectionException extends BaseException implements ExceptionCode {

    public ClassReflectionException(String message) {
        super(message);
    }

    public ClassReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int errorCode() {
        return CLASS_REFLECTION_EXCEPTION;
    }
}
