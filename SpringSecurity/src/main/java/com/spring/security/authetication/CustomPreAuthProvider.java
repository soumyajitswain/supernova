package com.spring.security.authetication;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.spring.security.authority.CustomAuthorityUtils;

public class CustomPreAuthProvider extends PreAuthenticatedAuthenticationProvider {
	private static final Log logger = LogFactory.getLog(CustomPreAuthProvider.class);
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("PreAuthenticated authentication request: " + authentication);
		}

		if (authentication.getPrincipal() == null) {
			logger.debug("No pre-authenticated principal found in request.");
			throw new BadCredentialsException("No pre-authenticated principal found in request.");

		}

		if (authentication.getCredentials() == null) {
			logger.debug("No pre-authenticated credentials found in request.");
			throw new BadCredentialsException("No pre-authenticated credentials found in request.");
		}

    	Collection<? extends GrantedAuthority> authorities = CustomAuthorityUtils.createAuthorities(
    			authentication.getPrincipal().toString());

		PreAuthenticatedAuthenticationToken result =
				new PreAuthenticatedAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
		result.setDetails(authentication.getDetails());

		return result;
	}


}
