package com.daugherty.e2c.business;

import com.daugherty.e2c.domain.Party;

/**
 * Exception occurs when Anonymous already exists
 */
public class AnonymousPartyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Party anonymous;

    public AnonymousPartyExistsException(Party anonymous) {
        this.anonymous = anonymous;
    }

    public Party getAnonymous() {
        return anonymous;
    }

}
