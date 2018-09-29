package com.motorcli.springboot.restful.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motorcli.springboot.restful.auth.UserContext;
import com.motorcli.springboot.restful.auth.token.JWTAccessToken;
import com.motorcli.springboot.restful.auth.token.JWTTokenFactory;
import com.motorcli.springboot.restful.auth.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.ui.ModelMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * 认证成功处理程序
 */
public class AwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper mapper;
    private final JWTTokenFactory tokenFactory;

    @Autowired
    public AwareAuthenticationSuccessHandler(final ObjectMapper mapper, final JWTTokenFactory tokenFactory) {
        this.mapper = mapper;
        this.tokenFactory = tokenFactory;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserContext userContext = (UserContext) authentication.getPrincipal();

        JWTAccessToken accessToken = tokenFactory.createAccessToken(userContext);
        Token refreshToken = tokenFactory.createRefreshToken(userContext);

        ModelMap tokenMap = new ModelMap();
        tokenMap.put("claims", accessToken.getClaims());
        tokenMap.put("token", accessToken.getToken());
        tokenMap.put("refreshToken", refreshToken.getToken());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), tokenMap);

        clearAuthenticationAttributes(request);
    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
