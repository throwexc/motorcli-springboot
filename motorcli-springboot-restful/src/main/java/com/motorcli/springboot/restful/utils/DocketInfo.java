package com.motorcli.springboot.restful.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import springfox.documentation.service.ApiInfo;

@Getter
@Setter
@Accessors
@Builder
public class DocketInfo {

    private String groupName;

    private String basePackage;

    private ApiInfo apiInfo;
}
