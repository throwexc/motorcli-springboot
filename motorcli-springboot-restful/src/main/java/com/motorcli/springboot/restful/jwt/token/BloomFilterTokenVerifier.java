package com.motorcli.springboot.restful.jwt.token;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Token 验证过滤器
 */
@ConditionalOnProperty(name = "motorcli.jwt.enable", havingValue = "true", matchIfMissing = false)
@Component
public class BloomFilterTokenVerifier implements TokenVerifier {
    @Override
    public boolean verify(String jti) {
        return true;
    }
}
