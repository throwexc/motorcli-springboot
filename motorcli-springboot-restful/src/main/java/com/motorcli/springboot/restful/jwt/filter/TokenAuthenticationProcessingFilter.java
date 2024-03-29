package com.motorcli.springboot.restful.jwt.filter;

import com.motorcli.springboot.restful.config.AbstractJWTConfiguration;
import com.motorcli.springboot.restful.jwt.token.AuthenticationToken;
import com.motorcli.springboot.restful.jwt.token.RawAccessToken;
import com.motorcli.springboot.restful.token.TokenExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * Token 验证
 */
public class TokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationFailureHandler failureHandler;
    private final TokenExtractor tokenExtractor;
    
    public TokenAuthenticationProcessingFilter(AuthenticationFailureHandler failureHandler,
            TokenExtractor tokenExtractor, RequestMatcher matcher) {
        super(matcher);
        this.failureHandler = failureHandler;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String tokenPayload = request.getHeader(AbstractJWTConfiguration.TOKEN_HEADER_PARAM);
        RawAccessToken token = new RawAccessToken(tokenExtractor.extract(tokenPayload));
        return getAuthenticationManager().authenticate(new AuthenticationToken(token));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
