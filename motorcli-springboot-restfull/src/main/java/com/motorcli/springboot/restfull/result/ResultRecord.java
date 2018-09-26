package com.motorcli.springboot.restfull.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 通用返回结果
 */
@ApiModel("单条数据返回结果")
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

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
