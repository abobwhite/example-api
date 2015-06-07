package com.daugherty.e2c.domain;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Provides utility methods for accessing security-related information.
 */
public class SecurityUtils {

    public static final String UNREGISTERED_USER = "UnregisteredUser";

    public static boolean authenticatedUserHasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                if (grantedAuthority.getAuthority().equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return UNREGISTERED_USER;
        } else if (principalIsE2CUser(authentication)) {
            return ((E2CUser) authentication.getPrincipal()).getUsername();
        } else {
            return (String) authentication.getPrincipal();
        }
    }

    private static boolean principalIsE2CUser(Authentication authentication) {
        return authentication.getPrincipal() != null && authentication.getPrincipal() instanceof E2CUser;
    }

    public static Long getAuthenticatedUserPartyId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && principalIsE2CUser(authentication)) {
            return ((E2CUser) authentication.getPrincipal()).getParty().getId();
        } else {
            return null;
        }
    }

}
