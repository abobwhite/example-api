package com.daugherty.e2c.security;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.persistence.data.BuyerReadDao;
import com.daugherty.e2c.persistence.data.BuyerWriteDao;
import com.daugherty.e2c.persistence.data.PendingUserReadDao;
import com.daugherty.e2c.persistence.data.PendingUserWriteDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.daugherty.e2c.persistence.data.SupplierWriteDao;
import com.daugherty.e2c.persistence.data.UserWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class UserConfirmerTest {

    private static final String CONFIRMATION_TOKEN = "confirmationToken";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String HASHED_PASSWORD = "$S$DkLUxyzDU2pF0pZmZZjaggcBKVZCPTjihMxzYnwH8Qff.QiSxh8u";

    @Mock
    private UserWriteDao userWriteDao;
    @Mock
    private PendingUserReadDao pendingUserReadDao;
    @Mock
    private PendingUserWriteDao pendingUserWriteDao;
    @Mock
    private BuyerReadDao buyerReadDao;
    @Mock
    private BuyerWriteDao buyerWriteDao;
    @Mock
    private SupplierReadDao supplierReadDao;
    @Mock
    private SupplierWriteDao supplierWriteDao;
    @Mock
    private ApprovalStateTransitionVisitor changeDataVisitor;

    @InjectMocks
    private final UserConfirmer userConfirmer = new UserConfirmer();

    @Captor
    private ArgumentCaptor<E2CUser> userCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void confirmExistingPendingBuyerUserCreatesNewUserAndAssociatesWithBuyer() {
        PendingUser pendingUser = new PendingUser(586L, USERNAME, HASHED_PASSWORD, 42L, CONFIRMATION_TOKEN);
        when(pendingUserReadDao.loadByConfirmationToken(CONFIRMATION_TOKEN)).thenReturn(pendingUser);

        Buyer buyer = new Buyer(null, null, null);
        buyer.setId(pendingUser.getPartyId());
        when(buyerReadDao.loadLatest(pendingUser.getPartyId())).thenReturn(buyer);

        when(changeDataVisitor.getNewVersion(buyer.getVersion(), buyer.getApprovalStatus())).thenReturn(13);
        when(changeDataVisitor.getNewApprovalStatus(buyer.getApprovalStatus())).thenReturn(ApprovalStatus.DRAFT);

        userConfirmer.confirmUser(CONFIRMATION_TOKEN, USERNAME, PASSWORD, Role.BUYER);

        verify(pendingUserReadDao).loadByConfirmationToken(CONFIRMATION_TOKEN);

        verify(userWriteDao).insert(userCaptor.capture());
        assertThat(userCaptor.getValue().getUsername(), is(pendingUser.getUsername()));
        assertThat(userCaptor.getValue().getPassword(), is(pendingUser.getPassword()));
        assertThat(userCaptor.getValue().getParty().getId(), is(pendingUser.getPartyId()));
        assertThat(userCaptor.getValue().getAuthorities().contains(new SimpleGrantedAuthority(Role.BUYER)), is(true));

        verify(pendingUserWriteDao).delete(pendingUser.getId());

        assertThat(buyer.getApprovalStatus(), is(ApprovalStatus.DRAFT));
        assertThat(buyer.getVersion(), is(13));
        verify(buyerWriteDao).recordEvent(buyer);

        verifyZeroInteractions(supplierReadDao, supplierWriteDao);
    }

    @Test
    public void confirmExistingPendingSupplierUserCreatesNewUserAndAssociatesWithSupplier() {
        PendingUser pendingUser = new PendingUser(586L, USERNAME, HASHED_PASSWORD, 42L, CONFIRMATION_TOKEN);
        when(pendingUserReadDao.loadByConfirmationToken(CONFIRMATION_TOKEN)).thenReturn(pendingUser);

        Supplier supplier = new Supplier(null, null, null);
        supplier.setId(pendingUser.getPartyId());
        when(supplierReadDao.loadLatest(pendingUser.getPartyId(), Locale.ENGLISH)).thenReturn(supplier);

        when(changeDataVisitor.getNewVersion(supplier.getVersion(), supplier.getApprovalStatus())).thenReturn(13);
        when(changeDataVisitor.getNewApprovalStatus(supplier.getApprovalStatus())).thenReturn(ApprovalStatus.DRAFT);

        userConfirmer.confirmUser(CONFIRMATION_TOKEN, USERNAME, PASSWORD, Role.SUPPLIER);

        verify(pendingUserReadDao).loadByConfirmationToken(CONFIRMATION_TOKEN);

        verify(userWriteDao).insert(userCaptor.capture());
        assertThat(userCaptor.getValue().getUsername(), is(pendingUser.getUsername()));
        assertThat(userCaptor.getValue().getPassword(), is(pendingUser.getPassword()));
        assertThat(userCaptor.getValue().getParty().getId(), is(pendingUser.getPartyId()));
        assertThat(userCaptor.getValue().getAuthorities().contains(new SimpleGrantedAuthority(Role.SUPPLIER)), is(true));

        verify(pendingUserWriteDao).delete(pendingUser.getId());

        assertThat(supplier.getApprovalStatus(), is(ApprovalStatus.DRAFT));
        assertThat(supplier.getVersion(), is(13));
        verify(supplierWriteDao).recordEvent(supplier);

        verifyZeroInteractions(buyerReadDao, buyerWriteDao);
    }

    @Test
    public void confirmWithNonExistentConfirmationTokenThrowsException() {
        expectedException.expect(EmptyResultDataAccessException.class);

        when(pendingUserReadDao.loadByConfirmationToken(CONFIRMATION_TOKEN)).thenThrow(
                new EmptyResultDataAccessException(1));

        userConfirmer.confirmUser(CONFIRMATION_TOKEN, USERNAME, PASSWORD, Role.BUYER);

        verifyZeroInteractions(buyerReadDao, buyerWriteDao, supplierReadDao, supplierWriteDao, userWriteDao,
                pendingUserWriteDao, changeDataVisitor);
    }

    @Test
    public void confirmWithMismatchingUsernameThrowsException() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError("username", Validatable.PENDING_USER_USERNAME));

        PendingUser pendingUser = new PendingUser(586L, USERNAME, HASHED_PASSWORD, 42L, CONFIRMATION_TOKEN);
        when(pendingUserReadDao.loadByConfirmationToken(CONFIRMATION_TOKEN)).thenReturn(pendingUser);

        userConfirmer.confirmUser(CONFIRMATION_TOKEN, "bad", PASSWORD, Role.BUYER);

        verifyZeroInteractions(buyerReadDao, buyerWriteDao, supplierReadDao, supplierWriteDao, userWriteDao,
                pendingUserWriteDao, changeDataVisitor);
    }

    @Test
    public void confirmWithMismatchingPasswordThrowsException() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError("password", Validatable.PENDING_USER_PASSWORD));

        PendingUser pendingUser = new PendingUser(586L, USERNAME, HASHED_PASSWORD, 42L, CONFIRMATION_TOKEN);
        when(pendingUserReadDao.loadByConfirmationToken(CONFIRMATION_TOKEN)).thenReturn(pendingUser);

        userConfirmer.confirmUser(CONFIRMATION_TOKEN, USERNAME, "bad", Role.BUYER);

        verifyZeroInteractions(buyerReadDao, buyerWriteDao, supplierReadDao, supplierWriteDao, userWriteDao,
                pendingUserWriteDao, changeDataVisitor);
    }
}
