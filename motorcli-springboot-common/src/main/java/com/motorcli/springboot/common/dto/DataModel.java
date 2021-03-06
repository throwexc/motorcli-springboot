package com.motorcli.springboot.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 实体数据类型 DTO
 */
public abstract class DataModel<T> extends BaseDataModel {

    public DataModel() {
    }

    public DataModel(T entity) {
        this.mapByObject(entity);
        this.setValues(entity);
    }

    @JsonIgnore
    public void setValues(T entity) {}
}
