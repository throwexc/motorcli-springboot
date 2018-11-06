package com.motorcli.springboot.restful.jwt.token;

/**
 * Token 验证
 */
public interface TokenVerifier {
    boolean verify(String jti);
}
