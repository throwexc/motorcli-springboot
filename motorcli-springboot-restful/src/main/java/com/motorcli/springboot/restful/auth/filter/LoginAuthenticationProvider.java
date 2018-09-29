package com.motorcli.springboot.restful.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motorcli.springboot.common.utils.JsonUtils;
import com.motorcli.springboot.restful.auth.UserContext;
import com.motorcli.springboot.restful.auth.UserInfo;
import com.motorcli.springboot.restful.auth.UserRole;
import com.motorcli.springboot.restful.auth.service.RoleAuthService;
import com.motorcli.springboot.restful.auth.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class LoginAuthenticationProvider implements AuthenticationProvider {

	//final BCryptPasswordEncoder encoder;

	final UserAuthService userAuthService;
	final RoleAuthService roleAuthService;

	private final ObjectMapper objectMapper;

    public LoginAuthenticationProvider(final ObjectMapper objectMapper, final UserAuthService userService, final RoleAuthService roleService) {
	    this.objectMapper = objectMapper;
        this.userAuthService = userService;
        this.roleAuthService = roleService;
        //this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");
        log.debug("[authentication info] - [{}]", JsonUtils.toJson(this.objectMapper, authentication, false));
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserInfo user = userAuthService.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User not found: " + username);
        if(!user.getPassword().equals(DigestUtils.md5Hex(password))) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }
//        if (!StringUtils.equals(password, user.getPassword())) {
//            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
//        }
        List<UserRole> roles = roleAuthService.findRolesByUser(user);
        if (roles == null) throw new InsufficientAuthenticationException("User has no roles assigned");
        
        List<GrantedAuthority> authorities = roles.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.authority()))
                .collect(Collectors.toList());
        
        UserContext userContext = UserContext.create(user.getUsername(), authorities);
        
        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
