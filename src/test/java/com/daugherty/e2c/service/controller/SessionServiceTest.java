package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.security.Role;

@RunWith(MockitoJUnitRunner.class)
public class SessionServiceTest {

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private Principal authenticatedUser;

    @InjectMocks
    private final SessionService sessionService = new SessionService();

    @Test
    public void retrieveAuthenticatedUserDelegatesToUserDetailsServiceAndDoesNotReturnPassword() {
        when(authenticatedUser.getName()).thenReturn("apdent");
        E2CUser user = new E2CUser("username", "password", true, 3, AuthorityUtils.createAuthorityList(Role.BUYER),
                new Party(42L), true);

        when(userDetailsService.loadUserByUsername("apdent")).thenReturn(user);

        UserDetails returnedUser = sessionService.retrieveAuthenticatedUser(authenticatedUser);

        assertThat(returnedUser.getUsername(), is(user.getUsername()));
        assertThat(returnedUser.getPassword(), is(SessionService.RESTRICTED_VALUE));
        assertThat(returnedUser.getAuthorities().size(), is(user.getAuthorities().size()));
        assertThat(returnedUser.isEnabled(), is(user.isEnabled()));
        assertThat(returnedUser.isAccountNonLocked(), is(user.isAccountNonLocked()));
        assertThat(((E2CUser) returnedUser).getParty(), is(user.getParty()));
    }

}
