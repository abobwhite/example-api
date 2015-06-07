package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.security.core.authority.AuthorityUtils;

import com.daugherty.e2c.security.Role;

public class E2CUserTest {

    @Test
    public void accountIsNotLockedWhenUserConstructedWithLessThanFiveFailures() throws Exception {
        E2CUser user = new E2CUser("username", "password", true, 4, AuthorityUtils.createAuthorityList(Role.ADMIN),
                new Party(1L), true);
        assertThat(user.isAccountNonLocked(), is(true));
    }

    @Test
    public void accountIsLockedWhenUserConstructedWithFiveFailures() throws Exception {
        E2CUser user = new E2CUser("username", "password", true, 5, AuthorityUtils.createAuthorityList(Role.ADMIN),
                new Party(1L), true);
        assertThat(user.isAccountNonLocked(), is(false));
    }

}
