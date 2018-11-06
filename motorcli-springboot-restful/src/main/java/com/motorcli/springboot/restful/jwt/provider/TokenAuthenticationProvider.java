package com.motorcli.springboot.restful.jwt.provider;

import com.motorcli.springboot.restful.auth.UserContext;
import com.motorcli.springboot.restful.config.MotorCLIJWTProperties;
import com.motorcli.springboot.restful.jwt.token.AuthenticationToken;
import com.motorcli.springboot.restful.jwt.token.RawAccessToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 使用 {@link AuthenticationProvider} 的接口提供实现 {@link com.motorcli.springboot.restful.token.Token} 身份验证的实例
 *
 */
@Slf4j
public class TokenAuthenticationProvider implements AuthenticationProvider {


	private final MotorCLIJWTProperties tokenProperties;

	public TokenAuthenticationProvider(MotorCLIJWTProperties tokenProperties) {
		this.tokenProperties = tokenProperties;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		RawAccessToken rawAccessToken = (RawAccessToken) authentication.getCredentials();
		long startTime = System.currentTimeMillis();
		Jws<Claims> jwsClaims = rawAccessToken.parseClaims(tokenProperties.getSigningKey());
		log.debug("[验证Token消耗时间] - [{}]", (System.currentTimeMillis() - startTime));
		String subject = jwsClaims.getBody().getSubject();

		List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
		List<GrantedAuthority> authorities = scopes.stream().map(SimpleGrantedAuthority::new).collect(toList());
		UserContext context = UserContext.create(subject, authorities);
		return new AuthenticationToken(context, context.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (AuthenticationToken.class.isAssignableFrom(authentication));
	}
}
