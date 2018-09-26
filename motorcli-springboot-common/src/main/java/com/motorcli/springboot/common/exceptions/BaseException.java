package com.motorcli.springboot.common.exceptions;

/**
 * 异常基类
 * 系统中自定义异常都集成该类
 * 自定义异常都必须定义一个唯一的 <code>errorCode</code>
 */
public abstract class BaseException extends RuntimeException {

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int errorCode();
}
