package com.motorcli.springboot.restful.token.exceptions;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * 不支持的方法验证 / GET != POST
 */
public class AuthMethodNotSupportedException extends AuthenticationServiceException {
    private static final long serialVersionUID = 3705043083010304496L;

    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
