package com.motorcli.springboot.restful.dto;

import com.motorcli.springboot.common.dto.DataModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class TreeModel<T> extends DataModel<T> {

    @ApiModelProperty("id")
    protected String id;

    @ApiModelProperty("父节点ID")
    protected String parentId;

    @ApiModelProperty("子节点")
    protected List<?> children;

    public TreeModel() {}

    public TreeModel(T entity) {
        super(entity);
    }
}
