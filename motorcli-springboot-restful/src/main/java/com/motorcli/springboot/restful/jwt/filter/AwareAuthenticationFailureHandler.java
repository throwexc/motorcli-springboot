package com.motorcli.springboot.restful.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motorcli.springboot.restful.result.Result;
import com.motorcli.springboot.restful.token.exceptions.AuthMethodNotSupportedException;
import com.motorcli.springboot.restful.token.exceptions.ExpiredTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 认证失败处理程序
 */
public class AwareAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper mapper;

    @Autowired
    public AwareAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Result result = new Result(-401);

        if (e instanceof BadCredentialsException) {
            result.setMsg("Invalid username or password");
            mapper.writeValue(response.getWriter(), result);
        } else if (e instanceof ExpiredTokenException) {
            result.setMsg("Token has expired");
            mapper.writeValue(response.getWriter(), request);
        } else if (e instanceof AuthMethodNotSupportedException) {
            result.setMsg(e.getMessage());
            mapper.writeValue(response.getWriter(), request);
        }
        result.setMsg("Authentication failed");
        mapper.writeValue(response.getWriter(), result);
    }
}
