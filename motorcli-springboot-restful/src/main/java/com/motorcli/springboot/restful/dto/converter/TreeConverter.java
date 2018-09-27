package com.motorcli.springboot.restful.dto.converter;

import com.motorcli.springboot.common.exceptions.DataConverterException;
import com.motorcli.springboot.restful.dto.TreeEntityDataModel;

import java.util.Collection;
import java.util.List;

public class TreeConverter<T> extends ListConverter<T> {

    private String topId;

    public TreeConverter(Collection<T> data) {
        super(data);
    }

    public TreeConverter(Collection<T> data, String topId) {
        super(data);
        this.topId = topId;
    }

    public <X extends TreeEntityDataModel> List<X> toTree(Class<X> clazz) throws DataConverterException {
        List<X> changeModelList = super.toList(clazz);
        TreeDataChanger<X> jsonTreeDtoOption = new TreeDataChanger(changeModelList, this.topId);
        return jsonTreeDtoOption.toTree();
    }
}
