package com.motorcli.springboot.web.error;

import com.motorcli.springboot.common.exceptions.BaseException;
import com.motorcli.springboot.web.result.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 */
@Slf4j
@ControllerAdvice
public class ErrorRequestHandler extends ResponseEntityExceptionHandler {

    @Autowired(required = false)
    private SystemErrorListener systemErrorListener;

    @Autowired(required = false)
    private CustomExceptionListener customExceptionListener;

	@ExceptionHandler({
		BaseException.class,
		Exception.class,
	})
    @ResponseBody
    ResponseEntity<ErrorResult> handleControllerException(HttpServletRequest request, Throwable ex) {

        HttpStatus status = getStatus(request);
        ErrorResult errorResult = new ErrorResult();
		String fullStackTrace = ExceptionUtils.getStackTrace(ex);

        if(ex instanceof BaseException) {
            status = HttpStatus.OK;

            BaseException error = (BaseException)ex;
            errorResult.setCode(error.errorCode());
            errorResult.setMsg(error.getMessage());

            logger.warn("自定义异常 --> " + ex.getMessage());

            if(this.customExceptionListener != null) {
                this.customExceptionListener.onException(ex);
            }

        	return new ResponseEntity<>(errorResult, status);
        } else {
            if(this.systemErrorListener != null) {
                this.systemErrorListener.onError(ex);
            }
        }

        logger.error("服务器异常", ex);

    	errorResult.setCode(-9999);
    	errorResult.setMsg("系统异常");
    	errorResult.setError(fullStackTrace);

    	return new ResponseEntity<>(errorResult, status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.valueOf(statusCode);
    }
}
