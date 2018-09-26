package com.motorcli.springboot.web.result;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统错误返回结果
 */
@Getter
@Setter
public class ErrorResult {

    /**
     * 错误码
     */
    protected int code;

    /**
     * 错误信息
     */
    protected String error;

    /**
     * 错误消息
     */
    protected String msg;
}
