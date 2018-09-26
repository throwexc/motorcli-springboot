package com.motorcli.springboot.mybatis.config;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@Configuration
@ConditionalOnClass({SqlSessionFactoryBean.class})
@ConditionalOnProperty(name = "mybatis.enable", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
@EnableTransactionManagement
@EnableConfigurationProperties({MotorCLIMybatisProperties.class})
public class MotorCLIMybatisAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public static MapperScannerConfigurer mapperScannerConfigurer(MotorCLIMybatisProperties properties) {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        if(StringUtils.isEmpty(properties.getBaseMapperPackage())) {
            mapperScannerConfigurer.setBasePackage("com.motorcli.*.dao");
        } else {
            mapperScannerConfigurer.setBasePackage("com.motorcli.*.dao," + properties.getBaseMapperPackage());
        }

        return mapperScannerConfigurer;
    }
}
