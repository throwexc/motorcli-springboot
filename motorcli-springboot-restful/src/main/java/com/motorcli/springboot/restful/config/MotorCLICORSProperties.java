package com.motorcli.springboot.restful.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "motorcli.cors")
public class MotorCLICORSProperties {

    /**
     * 准许的域
     */
    private List<String> allowedOrigins = new ArrayList<>();

    /**
     * 准许的方法
     */
    private List<String> allowedMethods = new ArrayList<>();

    /**
     * 准许的头信息
     */
    private List<String> allowedHeaders = new ArrayList<>();
}
