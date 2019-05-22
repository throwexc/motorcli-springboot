package com.motorcli.springboot.restful.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.motorcli.springboot.common.dto.DataModel;
import com.motorcli.springboot.restful.exceptions.ModelParamErrorException;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.ReflectionUtils;

public abstract class ValidationModel<T> extends DataModel<T> {

    public ValidationModel() {
        super();
    }

    public ValidationModel(T entity) {
        super(entity);
    }

    /**
     * 模型验证
     * 验证带有 @ApiModelProperty 属性是否必填项, required == true 时验证
     */
    @JsonIgnore
    public void validate() {
        ReflectionUtils.doWithFields(this.getClass(), field -> {
            ApiModelProperty property = field.getAnnotation(ApiModelProperty.class);
            if(property != null && property.required() == true) {
                ReflectionUtils.makeAccessible(field);
                Object value = ReflectionUtils.getField(field, this);

                if(value == null) {
                    throw new ModelParamErrorException(property.value() + "不能为空");
                }

                String valueType = value.getClass().getName();

                if(valueType.equals("java.lang.String")) {
                    if(value.toString().trim().equals("")) {
                        throw new ModelParamErrorException(property.value() + "不能为空");
                    }
                }
            }
        }, ReflectionUtils.COPYABLE_FIELDS);
    }
}
