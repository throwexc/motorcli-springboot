package com.motorcli.springboot.restful.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.motorcli.springboot.restful.authentication.RestAuthenticationEntryPoint;
import com.motorcli.springboot.restful.authentication.SkipPathRequestMatcher;
import com.motorcli.springboot.restful.jwt.filter.AwareAuthenticationFailureHandler;
import com.motorcli.springboot.restful.jwt.filter.AwareAuthenticationSuccessHandler;
import com.motorcli.springboot.restful.jwt.filter.TokenAuthenticationProcessingFilter;
import com.motorcli.springboot.restful.jwt.provider.LoginAuthenticationProvider;
import com.motorcli.springboot.restful.jwt.provider.TokenAuthenticationProvider;
import com.motorcli.springboot.restful.jwt.token.JWTHeaderTokenExtractor;
import com.motorcli.springboot.restful.jwt.token.JWTTokenFactory;
import com.motorcli.springboot.restful.token.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;


public abstract class AbstractJWTConfiguration extends WebSecurityConfigurerAdapter {

    public static final String TOKEN_HEADER_PARAM = "Authorization";
    private static final String FORM_BASED_LOGIN_ENTRY_POINT = "/authentication/token";
    private static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";
    private static final String TOKEN_REFRESH_ENTRY_POINT = "/authentication/refresh_token";

    private final JWTTokenFactory tokenFactory;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final LoginAuthenticationProvider loginAuthenticationProvider;
    private final TokenAuthenticationProvider tokenAuthenticationProvider;
    private final TokenExtractor tokenExtractor;

    @Autowired
    public AbstractJWTConfiguration(
            MotorCLIJWTProperties properties,
            ObjectMapper objectMapper,
            JWTTokenFactory jwtTokenFactory,
            JWTHeaderTokenExtractor jwtHeaderTokenExtractor,
            UserDetailsService userDetailsService) {
        this.tokenFactory = jwtTokenFactory;
        this.authenticationEntryPoint = new RestAuthenticationEntryPoint();
        this.successHandler = new AwareAuthenticationSuccessHandler(objectMapper, this.tokenFactory);
        this.failureHandler = new AwareAuthenticationFailureHandler(objectMapper);
        this.loginAuthenticationProvider = new LoginAuthenticationProvider(objectMapper, userDetailsService);
        this.tokenAuthenticationProvider = new TokenAuthenticationProvider(properties);
        this.tokenExtractor = jwtHeaderTokenExtractor;
    }

    private TokenAuthenticationProcessingFilter buildTokenAuthenticationProcessingFilter() throws Exception {
        List<String> list = Lists.newArrayList(TOKEN_BASED_AUTH_ENTRY_POINT);
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
        auth.authenticationProvider(this.tokenAuthenticationProvider);
        auth.authenticationProvider(this.loginAuthenticationProvider);
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
                .addFilterBefore(buildTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public Docket authDoc() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Auth 接口")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .paths(regex("/authentication/.*"))//过滤的接口
                .build()
                .apiInfo(apiInfo());

//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("Auth 接口")
//                .genericModelSubstitutes(DeferredResult.class)
//                .useDefaultResponseMessages(false)
//                .forCodeGeneration(true)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.motorcli.springboot.restful.auth.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {

        Contact contact = new Contact("Li", "https://github.com/throwexc", "306760249@qq.com");

        ApiInfo apiInfo = new ApiInfo("MotorCli Web 脚手架工程",//大标题
                "服务端接口",//小标题
                "版本0.1",//版本
                "",
                contact,//作者
                "",//链接显示文字
                "",//网站链接,
                new ArrayList()
        );
        return apiInfo;
    }
}
