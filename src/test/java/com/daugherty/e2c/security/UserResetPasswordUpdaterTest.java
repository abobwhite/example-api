package com.daugherty.e2c.security;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.mail.sender.ChangePasswordMailSender;
import com.daugherty.e2c.persistence.data.UserReadDao;
import com.daugherty.e2c.persistence.data.UserWriteDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class UserResetPasswordUpdaterTest {
    @Mock
    private UserReadDao userReadDao;
    @Mock
    private UserWriteDao userWriteDao;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private UsernameNotFoundException usernameNotFoundException;
    @Mock
    private ChangePasswordMailSender changePasswordMailSender;

    @InjectMocks
    private final UserUpdater userUpdater = new UserResetPasswordUpdater();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void resetPasswordFindsUserAndChangesPassword() {
        E2CUser user = new E2CUser("username", "oldPassword", false, 0, Lists.newArrayList(new SimpleGrantedAuthority(
                Role.BUYER)), new Party(1L), true);

        when(userReadDao.getUsernameByPassword("oldPassword")).thenReturn("username");
        when(userDetailsService.loadUserByUsername("username")).thenReturn(user);
        when(userReadDao.getEmailWithPersonalNameForPartyId(1L)).thenReturn("emailAddress");

        userUpdater.updatePassword(null, "oldPassword", "newPassword", "newPassword", Locale.ENGLISH);

        verify(userWriteDao).changePassword("username", "oldPassword", "newPassword");
        verify(changePasswordMailSender).send("emailAddress", "username", Locale.ENGLISH);
    }
}
