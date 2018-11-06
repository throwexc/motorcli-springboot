package com.motorcli.springboot.restful.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motorcli.springboot.restful.jwt.token.JWTHeaderTokenExtractor;
import com.motorcli.springboot.restful.jwt.token.JWTTokenFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class MotorCLIWebSecurityAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(name = "motorcli.jwt.enable", havingValue = "true", matchIfMissing = true)
    @EnableConfigurationProperties({MotorCLIJWTProperties.class})
    @Order(99)
    public static class MotorCLIJWTConfig extends AbstractJWTConfiguration {
        @Autowired
        public MotorCLIJWTConfig(MotorCLIJWTProperties properties, ObjectMapper objectMapper, JWTTokenFactory jwtTokenFactory, JWTHeaderTokenExtractor jwtHeaderTokenExtractor, UserDetailsService userDetailsService) {
            super(properties, objectMapper, jwtTokenFactory, jwtHeaderTokenExtractor, userDetailsService);
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "motorcli.cors.enable", havingValue = "true", matchIfMissing = true)
    @EnableConfigurationProperties({MotorCLICORSProperties.class})
    @Order(100)
    public static class MotorCLICORSConfig extends AbstractCORSConfiguration {
        @Autowired
        public MotorCLICORSConfig(MotorCLICORSProperties properties) {
            super(properties);
        }
    }
}
