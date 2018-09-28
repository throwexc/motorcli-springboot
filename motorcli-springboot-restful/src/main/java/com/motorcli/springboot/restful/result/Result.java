package com.motorcli.springboot.restful.result;

/**
 * 通用返回结果
 */
public class Result extends AbstractResult {

    public Result(int code) {
        super(code);
    }

    public Result(int code, String msg) {
        super(code, msg);
    }
}
