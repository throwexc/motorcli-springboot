package com.motorcli.springboot.restful.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "motorcli.jwt")
public class MotorCLIJWTProperties {

    /**
     * Token 失效时间
     * 单位 秒
     */
    private long expirationTime;

    /**
     * Refresh Token 失效时间
     * 单位 秒
     */
    private long refreshExpirationTime;

    /**
     * 发行者
     */
    private String issuer;

    /**
     * 签名 KEY
     */
    private String signingKey;
}
