package com.motorcli.springboot.restful.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用返回结果
 */
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public abstract class AbstractResult {

    @ApiModelProperty("结果状态码")
    private int code;

    @ApiModelProperty("结果信息")
    private String msg;

    public AbstractResult(int code) {
        this.code = code;
    }

    public AbstractResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
