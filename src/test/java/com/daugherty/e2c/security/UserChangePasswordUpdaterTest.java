package com.daugherty.e2c.security;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
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

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.mail.sender.ChangePasswordMailSender;
import com.daugherty.e2c.persistence.data.UserReadDao;
import com.daugherty.e2c.persistence.data.UserWriteDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class UserChangePasswordUpdaterTest {
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
    private final UserChangePasswordUpdater userUpdater = new UserChangePasswordUpdater();

    @Mock
    private PHPassHasher hasher;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        userUpdater.setPhpassHasherForTest(hasher);
    }

    @Test
    public void changePasswordThrowValidationErrorWhenUserNotFound() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("username");

        doThrow(usernameNotFoundException).when(userDetailsService).loadUserByUsername("username");

        userUpdater.updatePassword("username", "oldPassword", "newPassword", "newPassword", Locale.ENGLISH);

        verifyZeroInteractions(userWriteDao);
    }

    @Test
    public void changePasswordThrowValidationErrorWhenNewPasswordNotMatched() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("password");

        userUpdater.updatePassword("username", "oldPassword", "newPassword", "newPasswordNotMatch", Locale.ENGLISH);

        verifyZeroInteractions(userWriteDao);
    }

    @Test
    public void changePasswordThrowValidationErrorWhenOldPasswordNotMatched() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("oldPassword");

        E2CUser user = new E2CUser("username", "oldPasswordNotEqual", false, 0,
                Lists.newArrayList(new SimpleGrantedAuthority(Role.BUYER)), new Party(1L), true);

        when(userReadDao.getUsernameByPassword("oldPassword")).thenReturn("username");
        when(userDetailsService.loadUserByUsername("username")).thenReturn(user);
        when(hasher.isMatch("oldPassword", "oldPasswordNotEqual")).thenReturn(false);

        userUpdater.updatePassword("username", "oldPassword", "newPassword", "newPasswordNotMatch", Locale.ENGLISH);

        verifyZeroInteractions(userWriteDao);
    }

    @Test
    public void changePasswordFindsUserAndResetsPassword() {
        E2CUser user = new E2CUser("username", "oldPassword", false, 0, Lists.newArrayList(new SimpleGrantedAuthority(
                Role.BUYER)), new Party(1L), true);

        when(userReadDao.getEmailWithPersonalNameForPartyId(1L)).thenReturn("differentEmailAddress");
        when(userDetailsService.loadUserByUsername("username")).thenReturn(user);
        when(hasher.isMatch("oldPassword", "oldPassword")).thenReturn(true);

        userUpdater.updatePassword("username", "oldPassword", "newPassword", "newPassword", Locale.ENGLISH);

        verify(userWriteDao).changePassword("username", "oldPassword", "newPassword");
        verify(changePasswordMailSender).send("differentEmailAddress", "username", Locale.ENGLISH);
    }
}
