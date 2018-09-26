package com.motorcli.springboot.restfull.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TreeEntityDataModel<T> extends EntityDataModel<T> {

    @ApiModelProperty("id")
    protected String id;

    @ApiModelProperty("父节点ID")
    protected String parentId;

    @ApiModelProperty("子节点")
    protected List<TreeEntityDataModel> children;

    public TreeEntityDataModel() {}

    public TreeEntityDataModel(T entity) {
        super(entity);
    }
}
