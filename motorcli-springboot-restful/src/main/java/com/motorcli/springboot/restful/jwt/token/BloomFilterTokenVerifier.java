package com.motorcli.springboot.restful.jwt.token;

import org.springframework.stereotype.Component;

/**
 * Token 验证过滤器
 */
@Component
public class BloomFilterTokenVerifier implements TokenVerifier {
    @Override
    public boolean verify(String jti) {
        return true;
    }
}
