package com.daugherty.e2c.security;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.sender.ForgotPasswordMailSender;
import com.daugherty.e2c.persistence.data.BuyerReadDao;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.PendingUserReadDao;
import com.daugherty.e2c.persistence.data.PendingUserWriteDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.daugherty.e2c.persistence.data.UserReadDao;
import com.daugherty.e2c.persistence.data.UserWriteDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class UserDeleterTest {

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private UserReadDao userReadDao;
    @Mock
    private UsernameNotFoundException usernameNotFoundException;
    @Mock
    private UserWriteDao userWriteDao;
    @Mock
    private ForgotPasswordMailSender forgotPasswordMailSender;
    @Mock
    private EmptyResultDataAccessException emptyResultDataAccessException;
    @Mock
    private SupplierReadDao supplierReadDao;
    @Mock
    private Supplier supplier;
    @Mock
    private BuyerReadDao buyerReadDao;
    @Mock
    private Buyer buyer;
    @Mock
    private Contact contact;
    @Mock
    private PendingUserReadDao pendingUserReadDao;
    @Mock
    private PendingUserWriteDao pendingUserWriteDao;
    @Mock
    private Hashids hashids;

    @InjectMocks
    private UserDeleter userDeleter;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void usernameNotFoundThrowsValidationException() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("username");

        when(userReadDao.getUsernameByEmailAddress("emailAddress")).thenReturn("username");

        doThrow(usernameNotFoundException).when(userDetailsService).loadUserByUsername("username");

        userDeleter.deleteUser("emailAddress", Locale.ENGLISH);

        verifyZeroInteractions(forgotPasswordMailSender, userWriteDao);
    }

    @Test
    public void emailNotFoundThrowsValidationException() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("emailAddress");

        E2CUser user = new E2CUser("username", "password", true, 0, Lists.newArrayList(new SimpleGrantedAuthority(
                Role.BUYER)), new Party(1L), true);

        when(userDetailsService.loadUserByUsername("username")).thenReturn(user);

        doThrow(emptyResultDataAccessException).when(userReadDao).getUsernameByEmailAddress("emailAddress");

        userDeleter.deleteUser("emailAddress", Locale.ENGLISH);

        verifyZeroInteractions(forgotPasswordMailSender, userWriteDao);
    }

    @Test
    public void userNotEnabledThrowsValidationException() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("username");

        E2CUser user = new E2CUser("username", "password", false, 0, Lists.newArrayList(new SimpleGrantedAuthority(
                Role.BUYER)), new Party(1L), true);

        when(userDetailsService.loadUserByUsername("username")).thenReturn(user);

        when(userReadDao.getUsernameByEmailAddress("emailAddress")).thenReturn("username");

        userDeleter.deleteUser("emailAddress", Locale.ENGLISH);

        verifyZeroInteractions(forgotPasswordMailSender, userWriteDao);
    }

    @Test
    public void deleteUserSendsEmail() {
        E2CUser user = new E2CUser("username", "password", true, 0, Lists.newArrayList(new SimpleGrantedAuthority(
                Role.BUYER)), new Party(1L), true);

        when(userReadDao.getUsernameByEmailAddress("emailAddress")).thenReturn("username");
        when(userDetailsService.loadUserByUsername("username")).thenReturn(user);
        when(userReadDao.getEmailWithPersonalNameForPartyId(1L)).thenReturn("differentEmailAddress");

        userDeleter.deleteUser("emailAddress", Locale.ENGLISH);

        verify(userWriteDao).disableUser(eq(user.getUsername()), Mockito.anyString());
        verify(forgotPasswordMailSender).send(eq("username"), eq("differentEmailAddress"), Mockito.anyString(),
                eq(Locale.ENGLISH));
    }

    @Test
    public void deletePendingUserSendsEmail() {
        PendingUser pendingUser = new PendingUser(1L, "username", "password", 100L, "confirmationToken");

        when(pendingUserReadDao.loadPendingUserByEmailAddress("emailAddress")).thenReturn(pendingUser);
        when(userReadDao.getEmailWithPersonalNameForPartyId(100L)).thenReturn("differentEmailAddress");

        userDeleter.deleteUser("emailAddress", Locale.ENGLISH);

        verify(pendingUserWriteDao).disablePendingUser(eq(pendingUser.getUsername()), Mockito.anyString());
        verify(forgotPasswordMailSender).send(eq("username"), eq("differentEmailAddress"), Mockito.anyString(),
                eq(Locale.ENGLISH));
    }

    @Test
    public void disablesUserWhenBuyerIsDisaaproved() {
        when(buyer.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("Email Address");

        when(buyerReadDao.loadLatest(1L)).thenReturn(buyer);
        when(userReadDao.getUsernameByEmailAddress("Email Address")).thenReturn("Username");

        when(hashids.decode("vXP9l0Or")).thenReturn(new long[] { 1L });
        userDeleter.deleteDisapprovedBuyer("vXP9l0Or");

        verify(userWriteDao).disableUser("Username", UserDeleter.DISAPPROVED_BUYER);
        verifyZeroInteractions(forgotPasswordMailSender);
    }

    @Test
    public void disablesUserWhenSupplierIsDisaaproved() {
        when(supplier.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("Email Address");

        when(hashids.decode("vXP9l0Or")).thenReturn(new long[] { 1L });
        when(supplierReadDao.loadLatest(1L, Locale.ENGLISH)).thenReturn(supplier);
        when(userReadDao.getUsernameByEmailAddress("Email Address")).thenReturn("Username");

        userDeleter.deleteDisapprovedSupplier("vXP9l0Or");

        verify(userWriteDao).disableUser("Username", UserDeleter.DISAPPROVED_SUPPLIER);
        verifyZeroInteractions(forgotPasswordMailSender);
    }
}
