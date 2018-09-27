package com.motorcli.springboot.restful.result;

import io.swagger.annotations.ApiModel;

/**
 * 通用返回结果
 */
@ApiModel("返回结果")
public class Result extends AbstractResult {

    public Result(int code) {
        super(code);
    }

    public Result(int code, String msg) {
        super(code, msg);
    }
}
