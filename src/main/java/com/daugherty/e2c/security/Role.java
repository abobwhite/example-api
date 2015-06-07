package com.daugherty.e2c.security;

import org.springframework.security.access.vote.AuthenticatedVoter;

/**
 * Defines constants that can be used by @Secured annotations on service-tier methods.
 */
public class Role {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String BUYER = "ROLE_BUYER";
    public static final String BUYER_MODERATOR = "ROLE_BUYER_MODERATOR";
    public static final String SUPPLIER = "ROLE_SUPPLIER";
    public static final String SUPPLIER_MODERATOR = "ROLE_SUPPLIER_MODERATOR";
    public static final String TRANSLATOR = "ROLE_TRANSLATOR";
    public static final String SALES = "ROLE_SALES";

    public class Spring {
        public static final String IS_AUTHENTICATED_ANONYMOUSLY = AuthenticatedVoter.IS_AUTHENTICATED_ANONYMOUSLY;
        public static final String IS_AUTHENTICATED_FULLY = AuthenticatedVoter.IS_AUTHENTICATED_FULLY;
    }

}
