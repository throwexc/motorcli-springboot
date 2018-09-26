package com.motorcli.springboot.common.exceptions;

/**
 * 文件操作异常
 */
public class FileHandlerException extends BaseException implements ExceptionCode {

    public FileHandlerException(String message) {
        super(message);
    }

    public FileHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int errorCode() {
        return FILE_EXCEPTION_CODE;
    }
}
