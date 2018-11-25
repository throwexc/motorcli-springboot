package com.motorcli.springboot.restful.jwt.controller;

import com.google.common.collect.Lists;
import com.motorcli.springboot.restful.auth.UserContext;
import com.motorcli.springboot.restful.config.MotorCLIJWTProperties;
import com.motorcli.springboot.restful.jwt.token.*;
import com.motorcli.springboot.restful.token.Token;
import com.motorcli.springboot.restful.token.exceptions.InvalidTokenException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"Authentication"}, value = "JWT 授权接口")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenFactory jwtTokenFactory;

    @Autowired
    private TokenVerifier tokenVerifier;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MotorCLIJWTProperties properties;

    @PostMapping(value = "/authentication/token")
    @ApiOperation("获取 TOKEN")
    public JWTAccessToken login(@RequestBody AuthenticationRequest request) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserContext userContext = (UserContext) authentication.getPrincipal();

        JWTAccessToken token = this.jwtTokenFactory.createAccessToken(userContext);

        return token;
    }

    @GetMapping("/authentication/token/refresh")
    @ApiOperation("刷新 Token")
    public Token refreshToken(@RequestBody RefreshTokenRequest request) {
        String tokenPayload = request.getRefreshToken();
        RawAccessToken rawToken = new RawAccessToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, properties.getSigningKey()).orElseThrow(() -> new InvalidTokenException(" Token 验证失败 "));

        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidTokenException(" Token 验证失败 ");
        }

        String subject = refreshToken.getSubject();

        final UserDetails userDetails = userDetailsService.loadUserByUsername(subject);

        List<GrantedAuthority> authorities = Lists.newArrayList(userDetails.getAuthorities());

        UserContext userContext = UserContext.create(userDetails.getUsername(), authorities);

        return this.jwtTokenFactory.createAccessToken(userContext);
    }
}
