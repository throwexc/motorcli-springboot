package com.motorcli.springboot.restful.jwt.token;

import com.motorcli.springboot.restful.token.Token;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "TOKEN 信息")
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
