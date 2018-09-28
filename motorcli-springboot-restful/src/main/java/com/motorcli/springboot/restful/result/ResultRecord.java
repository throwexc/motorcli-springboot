package com.motorcli.springboot.restful.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用返回结果
 * 单数据返回结果
 */
@Getter
@Setter
public class ResultRecord<T> extends Result {

    @ApiModelProperty("返回结果记录")
    private T record;

    @ApiModelProperty("记录ID字段")
    private String identifier;

    @ApiModelProperty("记录描述字段")
    private String label;

    public ResultRecord(int code) {
        super(code);
    }

    public ResultRecord(int code, String info) {
        super(code, info);
    }

    public ResultRecord(int code, T record) {
        super(code);
        this.record = record;
    }

    public ResultRecord(int code, String info, T record) {
        super(code, info);
        this.record = record;
    }
}
