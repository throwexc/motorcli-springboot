package com.motorcli.springboot.common.utils.http;

import lombok.Getter;
import lombok.Setter;

/**
 * HTTP 口令信息
 * 用户 Auth 验证
 */
@Getter
@Setter
public class TokenInfo {

    private String token;

    private String tokenPrefix = "Bearer ";

    public TokenInfo() {}

    public TokenInfo(String token) {
        this.token = token;
    }

    public TokenInfo(String token, String tokenPrefix) {
        this.token = token;
        this.tokenPrefix = tokenPrefix;
    }
}
