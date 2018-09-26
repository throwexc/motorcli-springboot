package com.motorcli.springboot.restfull.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * 通用返回结果
 */
@ApiModel("多条数据返回结果")
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

    private Map<String, Object> other;

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

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
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

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Map<String, Object> getOther() {
        return other;
    }

    public void setOther(Map<String, Object> other) {
        this.other = other;
    }
}
