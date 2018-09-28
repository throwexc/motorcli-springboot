package com.motorcli.springboot.restful.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 通用返回结果
 * 多数据返回结果
 */
@Getter
@Setter
public class ResultItems<T> extends Result {

    @ApiModelProperty("数据集合")
    private List<T> items;

    @ApiModelProperty("记录总数")
    private Long total;

    @ApiModelProperty("记录ID字段")
    private String identifier;

    @ApiModelProperty("记录描述字段")
    private String label;

    @ApiModelProperty("总页数")
    private Integer totalPage;

    private Integer page;

    public ResultItems(int code) {
        super(code);
    }

    public ResultItems(int code, String info) {
        super(code, info);
    }

    public ResultItems(int code, List<T> items) {
        super(code);
        this.items = items;
    }

    public ResultItems(int code, List<T> items, long total) {
        super(code);
        this.items = items;
        this.total = total;
    }

    public ResultItems(int code, String info, List<T> items) {
        super(code, info);
        this.items = items;
    }

    public ResultItems(int code, String info, List<T> items, long total) {
        super(code, info);
        this.items = items;
        this.total = total;
    }

    public ResultItems(int code, String info, List<T> items, int page, long total, int totalPage) {
        this(code, info, items,total);
        this.totalPage = totalPage;
        this.page = page;
    }
}
