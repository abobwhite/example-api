package com.daugherty.e2c.business.mutator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.mail.sender.RegistrationConfirmationMailSender;
import com.daugherty.e2c.persistence.data.BuyerWriteDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeWriteDao;
import com.daugherty.e2c.persistence.data.PendingUserWriteDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BuyerMutatorTest {

    @Mock
    private Validator<Buyer> buyerValidator;
    @Mock
    private BuyerWriteDao buyerWriteDao;
    @Mock
    private PendingUserWriteDao pendingUserWriteDao;
    @Mock
    private PartyBusinessTypeWriteDao businessTypeWriteDao;
    @Mock
    private ApprovalStateTransitionVisitor changeDataVisitor;
    @Mock
    private RegistrationConfirmationMailSender mailSender;

    @InjectMocks
    private final BuyerMutator buyerMutator = new BuyerMutator();

    @Captor
    private ArgumentCaptor<PendingUser> pendingUserCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createValidBuyerUpdatesDatabaseAndSendsEmail() {
        PendingUser pendingUser = new PendingUser("username", null, null);
        Buyer buyer = new Buyer(pendingUser, new Contact("first", "last", "country", null, "email", null, null,
                new Date()), null);
        Buyer persistedBuyer = new Buyer(null, new Contact("first", "last", "country", null, "email", null, null,
                new Date()), null);
        persistedBuyer.setId(42L);
        when(buyerWriteDao.insert(buyer)).thenReturn(persistedBuyer);

        buyerMutator.create(buyer);

        verify(buyerValidator).validate(buyer);
        verify(buyerWriteDao).insert(buyer);
        verify(pendingUserWriteDao).insert(pendingUserCaptor.capture());
        assertThat(pendingUserCaptor.getValue().getPartyId(), is(persistedBuyer.getId()));
        verify(mailSender).send(buyer.getContact().getEmailAddress(), pendingUser.getUsername(),
                pendingUser.getConfirmationToken(), Language.CHINESE);
    }

    @Test
    public void createInvalidBuyerDoesNothingButThrowValidationException() {
        expectedException.expect(ValidationException.class);

        Buyer buyer = new Buyer(new PendingUser("username", null, null), new Contact("first", "last", "country", null,
                "email", null, null, new Date()), null);
        doThrow(new ValidationException(new ValidationError())).when(buyerValidator).validate(buyer);

        buyerMutator.create(buyer);

        verify(buyerValidator).validate(buyer);
        verifyZeroInteractions(buyerWriteDao, pendingUserWriteDao, mailSender);
    }

    @Test
    public void updateValidBuyerUpdatesDatabase() {
        List<BusinessType> businessTypes = Lists.newArrayList(BusinessType.BUSINESS_SERVICE);
        Buyer buyer = new Buyer(42L, "jKNz4P4q", new Contact(null, null, null, null, null, null, null, new Date()), new Company(
                null, null, null, businessTypes, null, null, null, null, null, null, null), ApprovalStatus.APPROVED,
                13, 586L, null);

        when(changeDataVisitor.getNewVersion(buyer.getVersion(), buyer.getApprovalStatus())).thenReturn(25);
        when(changeDataVisitor.getNewApprovalStatus(buyer.getApprovalStatus())).thenReturn(ApprovalStatus.DRAFT);

        Buyer mutatedBuyer = buyerMutator.update(buyer);

        assertThat(mutatedBuyer, is(notNullValue()));
        assertThat(mutatedBuyer.getVersion(), is(25));
        assertThat(mutatedBuyer.getApprovalStatus(), is(ApprovalStatus.DRAFT));
        verify(buyerValidator).validate(buyer);
        verify(buyerWriteDao).update(buyer);
        verify(businessTypeWriteDao).updateBusinessTypes(586L, businessTypes);
        verifyZeroInteractions(pendingUserWriteDao, mailSender);
    }
}