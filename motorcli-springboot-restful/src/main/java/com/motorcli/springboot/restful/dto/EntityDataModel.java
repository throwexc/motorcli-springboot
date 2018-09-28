package com.motorcli.springboot.restful.dto;

import com.motorcli.springboot.common.dto.DataModel;

public abstract class EntityDataModel<T> extends DataModel<T> {

    public EntityDataModel() {}

    public EntityDataModel(T entity) {
        super(entity);
    }
}
