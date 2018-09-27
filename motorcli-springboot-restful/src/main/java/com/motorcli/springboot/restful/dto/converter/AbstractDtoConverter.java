package com.motorcli.springboot.restful.dto.converter;

import com.motorcli.springboot.common.exceptions.DataConverterException;
import com.motorcli.springboot.restful.dto.EntityDataModel;

import java.util.Collection;
import java.util.List;

public abstract class AbstractDtoConverter<T> {

    protected Collection<T> data;

    public AbstractDtoConverter(Collection<T> data) {
        this.data = data;
    }

    public abstract <X extends EntityDataModel> List<X> toList(final Class<X> clazz) throws DataConverterException;
}
