package com.motorcli.springboot.restfull.dto;

import com.motorcli.springboot.common.dto.DataModel;

public class EntityDataModel<T> extends DataModel<T> {

    public EntityDataModel() {}

    public EntityDataModel(T entity) {
        super(entity);
    }
}
