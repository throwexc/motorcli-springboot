package com.motorcli.springboot.mybatis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

@Getter
@Setter
@ConfigurationProperties(prefix = "mybatis")
public class MotorCLIMybatisProperties implements InitializingBean, EnvironmentAware {

    /** Mapper 包路径 **/
    private String baseMapperPackage;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.baseMapperPackage = environment.getProperty("mybatis.baseMapperPackage");
    }
}
