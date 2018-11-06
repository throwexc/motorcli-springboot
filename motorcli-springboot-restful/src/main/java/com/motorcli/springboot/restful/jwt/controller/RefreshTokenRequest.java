package com.motorcli.springboot.restful.jwt.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {

    /**
     * 刷新 TOKEN
     */
    private String refreshToken;
}
