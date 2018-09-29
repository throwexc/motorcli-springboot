package com.motorcli.springboot.restful.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motorcli.springboot.restful.auth.service.RoleAuthService;
import com.motorcli.springboot.restful.auth.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class MotorCLIAuthJWPAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(name = "motorcli.security.enable", havingValue = "true", matchIfMissing = true)
    @EnableConfigurationProperties({MotorCLIAuthJWTProperties.class})
    public static class MotorDataSourceConfig extends AbstractAuthJWTConfiguration {
        @Autowired
        public MotorDataSourceConfig(MotorCLIAuthJWTProperties properties, ObjectMapper objectMapper, UserAuthService userAuthService, RoleAuthService roleAuthService) {
            super(properties, objectMapper, userAuthService, roleAuthService);
        }
    }
}
