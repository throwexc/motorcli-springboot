package com.motorcli.springboot.restful.jwt.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motorcli.springboot.common.utils.JsonUtils;
import com.motorcli.springboot.restful.auth.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.assertj.core.util.Lists;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
public class LoginAuthenticationProvider implements AuthenticationProvider {


	private final ObjectMapper objectMapper;

	private final UserDetailsService userAuthService;

    public LoginAuthenticationProvider(final ObjectMapper objectMapper, final UserDetailsService userService) {
	    this.objectMapper = objectMapper;
	    this.userAuthService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");
        log.debug("[authentication info] - [{}]", JsonUtils.toJson(this.objectMapper, authentication, false));
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserDetails user = userAuthService.loadUserByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User not found: " + username);
        if(!user.getPassword().equals(DigestUtils.md5Hex(password))) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }

        List<GrantedAuthority> authorities = Lists.newArrayList(user.getAuthorities());
        
        UserContext userContext = UserContext.create(user.getUsername(), authorities);
        
        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
