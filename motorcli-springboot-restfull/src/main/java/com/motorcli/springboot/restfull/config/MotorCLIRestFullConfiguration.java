package com.motorcli.springboot.restfull.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class MotorCLIRestFullConfiguration implements WebMvcConfigurer {

    public static final int RESOURCE_CACHE_PERIOD = 10;//3600 * 24 * 7; //缓存1周时间

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api.html").addResourceLocations("classpath:/META-INF/resources/webjars/motorcli/restfull/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(RESOURCE_CACHE_PERIOD);
    }
}
