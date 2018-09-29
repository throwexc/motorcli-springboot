package com.motorcli.springboot.restful.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.motorcli.springboot.restful.auth.RestAuthenticationEntryPoint;
import com.motorcli.springboot.restful.auth.SkipPathRequestMatcher;
import com.motorcli.springboot.restful.auth.extractor.JWTHeaderTokenExtractor;
import com.motorcli.springboot.restful.auth.extractor.TokenExtractor;
import com.motorcli.springboot.restful.auth.filter.*;
import com.motorcli.springboot.restful.auth.service.RoleAuthService;
import com.motorcli.springboot.restful.auth.service.UserAuthService;
import com.motorcli.springboot.restful.auth.token.JWTTokenFactory;
import com.motorcli.springboot.restful.auth.token.TokenAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


public abstract class AbstractAuthJWTConfiguration extends WebSecurityConfigurerAdapter {

    public static final String TOKEN_HEADER_PARAM = "X-Authorization";
    private static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/authenticate";
    private static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";
    private static final String MANAGE_TOKEN_BASED_AUTH_ENTRY_POINT = "/manage/**";
    private static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/refresh_token";

    private final JWTTokenFactory tokenFactory;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final LoginAuthenticationProvider loginAuthenticationProvider;
    private final TokenAuthenticationProvider tokenAuthenticationProvider;
    private final TokenExtractor tokenExtractor;

    @Autowired
    public AbstractAuthJWTConfiguration(
            MotorCLIAuthJWTProperties properties,
            ObjectMapper objectMapper,
            UserAuthService userAuthService,
            RoleAuthService roleAuthService) {
        this.tokenFactory = new JWTTokenFactory(properties);
        this.authenticationEntryPoint = new RestAuthenticationEntryPoint();
        this.successHandler = new AwareAuthenticationSuccessHandler(objectMapper, this.tokenFactory);
        this.failureHandler = new AwareAuthenticationFailureHandler(objectMapper);
        this.loginAuthenticationProvider = new LoginAuthenticationProvider(objectMapper, userAuthService, roleAuthService);
        this.tokenAuthenticationProvider = new TokenAuthenticationProvider(properties);
        this.tokenExtractor = new JWTHeaderTokenExtractor();
    }

    @Bean
    public JWTTokenFactory tokenFactory(MotorCLIAuthJWTProperties properties) {
        return new JWTTokenFactory(properties);
    }

    private LoginProcessingFilter buildLoginProcessingFilter() throws Exception {
        LoginProcessingFilter filter = new LoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler, failureHandler);
        filter.setAuthenticationManager(super.authenticationManager());
        return filter;
    }

    private TokenAuthenticationProcessingFilter buildTokenAuthenticationProcessingFilter() throws Exception {
        List<String> list = Lists.newArrayList(TOKEN_BASED_AUTH_ENTRY_POINT, MANAGE_TOKEN_BASED_AUTH_ENTRY_POINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(list);
        TokenAuthenticationProcessingFilter filter = new TokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(super.authenticationManager());
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(loginAuthenticationProvider);
        auth.authenticationProvider(tokenAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 因为使用的是JWT，因此这里可以关闭csrf了
                .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll() // Login end-point
                .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll() // Token refresh end-point
                .and()
                .authorizeRequests()
                .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected API End-points
                //.antMatchers(MANAGE_TOKEN_BASED_AUTH_ENTRY_POINT).hasAnyRole(RoleEnum.ADMIN.desc())
                .and()
                .addFilterBefore(buildLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
