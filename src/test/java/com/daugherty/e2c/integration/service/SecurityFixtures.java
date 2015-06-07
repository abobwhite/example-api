package com.daugherty.e2c.integration.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Party;

class SecurityFixtures {

    public static void setUpContext(AuthenticationProvider authenticationProvider, Authentication authentication) {
        authenticationProvider.authenticate(authentication);

        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    public static Authentication buildAuthentication(List<GrantedAuthority> authorities) {
        E2CUser e2cUser = new E2CUser("fordp", "hoopy", true, 0, authorities, new Party(1L, "vXP9l0Or"), true);
        Authentication authentication = new RememberMeAuthenticationToken("e2cApiKey", e2cUser,
                e2cUser.getAuthorities());
        authentication.setAuthenticated(true);
        return authentication;
    }

    public static Authentication buildAuthentication(Long partyId, String publicId,List<GrantedAuthority> authorities) {
        E2CUser e2cUser = new E2CUser("fordp", "hoopy", true, 0, authorities, new Party(partyId, publicId), true);

        Authentication authentication = new RememberMeAuthenticationToken("e2cApiKey", e2cUser,
                e2cUser.getAuthorities());
        authentication.setAuthenticated(true);
        return authentication;
    }

}
