package com.motorcli.springboot.restfull.dto.converter;

import com.motorcli.springboot.common.exceptions.DataConverterException;
import com.motorcli.springboot.common.utils.ClassUtils;
import com.motorcli.springboot.restfull.dto.EntityDataModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListConverter<T> extends AbstractDtoConverter<T> {

    public ListConverter(Collection<T> data) {
        super(data);
    }

    @Override
    public <X extends EntityDataModel> List<X> toList(Class<X> clazz) throws DataConverterException {
        List<X> list = new ArrayList<>();
        try {
            for (T model : this.data) {
                X toModel = ClassUtils.createInstance(clazz);
                toModel.mapByObject(model);
                toModel.setValues(model);
                list.add(toModel);
            }
            return list;
        } catch (ReflectiveOperationException e) {
            throw new DataConverterException("数据转换时发生异常[" + clazz.getName() + "], 请检查要转换的 DTO 中是否有空构造参数");
        }
    }
}
