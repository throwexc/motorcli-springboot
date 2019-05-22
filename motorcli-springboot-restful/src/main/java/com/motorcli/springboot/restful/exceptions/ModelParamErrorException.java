package com.motorcli.springboot.restful.exceptions;

import com.motorcli.springboot.common.exceptions.BaseException;
import com.motorcli.springboot.common.exceptions.ExceptionCode;

public class ModelParamErrorException extends BaseException implements ExceptionCode {

    public ModelParamErrorException(String message) {
        super(message);
    }

    public ModelParamErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int errorCode() {
        return PARAM_ERROR_EXCEPTION_CODE;
    }
}
