package com.motorcli.springboot.restful.auth.token;

import io.jsonwebtoken.Claims;

public class JWTAccessToken implements Token {

    private final String rawToken;
    private Claims claims;

    public JWTAccessToken(final String token, Claims claims) {
        this.rawToken = token;
        this.claims = claims;
    }

    @Override
    public String getToken() {
        return this.rawToken;
    }

    public Claims getClaims() {
        return this.claims;
    }
}
