package com.motorcli.springboot.restful.jwt.token;

import com.motorcli.springboot.restful.token.TokenExtractor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

/**
 * 一个实现 {@link TokenExtractor} 接口 获取Token
 * 授权：Bearer scheme
 */
@ConditionalOnProperty(name = "motorcli.jwt.enable", havingValue = "true", matchIfMissing = false)
@Component
public class JWTHeaderTokenExtractor implements TokenExtractor {

    public static String HEADER_PREFIX = "Bearer ";

    @Override
    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }
        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }
        return header.substring(HEADER_PREFIX.length(), header.length());
    }
}
