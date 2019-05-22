package com.motorcli.springboot.restful.dto;

public class DataInputModel<T> extends ValidationModel<T> {

    public DataInputModel() {
        super();
    }

    public DataInputModel(T entity) {
        super(entity);
    }
}
