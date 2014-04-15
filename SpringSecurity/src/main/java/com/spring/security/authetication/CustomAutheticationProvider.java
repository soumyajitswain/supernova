package com.spring.security.authetication;

import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.spring.security.authority.CustomAuthorityUtils;
@Component
public class CustomAutheticationProvider  implements AuthenticationProvider {
	
    public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
    	UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
    	String userName = (String) token.getPrincipal();
    	String password = (String) token.getCredentials();
    	if(userName == null) {
    		throw new BadCredentialsException("Invalid username/password");
    	}
    	if(password == null) {
    		throw new BadCredentialsException("Invalid username/password");
    	}
    	Collection<? extends GrantedAuthority> authorities = CustomAuthorityUtils.createAuthorities(userName);
		return new UsernamePasswordAuthenticationToken(userName, password,authorities);
	}

	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
