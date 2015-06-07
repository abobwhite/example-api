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
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.mail.sender.RegistrationConfirmationMailSender;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeWriteDao;
import com.daugherty.e2c.persistence.data.PendingUserWriteDao;
import com.daugherty.e2c.persistence.data.SupplierWriteDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class SupplierMutatorTest {

    @Mock
    private Validator<Supplier> supplierValidator;
    @Mock
    private SupplierWriteDao supplierWriteDao;
    @Mock
    private PendingUserWriteDao pendingUserWriteDao;
    @Mock
    private RegistrationConfirmationMailSender mailSender;
    @Mock
    private PartyBusinessTypeWriteDao businessTypeWriteDao;
    @Mock
    private ApprovalStateTransitionVisitor changeDataVisitor;

    @InjectMocks
    private final SupplierMutator supplierMutator = new SupplierMutator();

    @Captor
    private ArgumentCaptor<PendingUser> pendingUserCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createValidSupplierUpdatesDatabaseAndSendsEmail() {
        PendingUser pendingUser = new PendingUser("username", null, null);
        Supplier supplier = new Supplier(pendingUser, new Contact("first", "last", "country", null, "email", null,
                Language.ENGLISH, new Date()), null);
        Supplier persistedSupplier = new Supplier(null, new Contact("first", "last", "country", null, "email", null,
                Language.ENGLISH, new Date()), null);
        persistedSupplier.setId(42L);
        when(supplierWriteDao.insert(supplier)).thenReturn(persistedSupplier);

        supplierMutator.create(supplier);

        verify(supplierValidator).validate(supplier);
        verify(supplierWriteDao).insert(supplier);
        verify(pendingUserWriteDao).insert(pendingUserCaptor.capture());
        assertThat(pendingUserCaptor.getValue().getPartyId(), is(persistedSupplier.getId()));
        verify(mailSender).send(supplier.getContact().getEmailAddress(), pendingUser.getUsername(),
                pendingUser.getConfirmationToken(), persistedSupplier.getContact().getLanguage());
    }

    @Test
    public void createInvalidSupplierDoesNothingButThrowValidationException() {
        expectedException.expect(ValidationException.class);

        Supplier supplier = new Supplier(new PendingUser("username", null, null), new Contact("first", "last",
                "country", null, "email", null, null, new Date()), null);
        doThrow(new ValidationException(new ValidationError())).when(supplierValidator).validate(supplier);

        supplierMutator.create(supplier);

        verify(supplierValidator).validate(supplier);
        verifyZeroInteractions(supplierWriteDao, pendingUserWriteDao, mailSender);
    }

    @Test
    public void updateValidSupplierUpdatesDatabase() {
        List<BusinessType> businessTypes = Lists.newArrayList(BusinessType.BUSINESS_SERVICE);
        Supplier supplier = new Supplier(42L, "jKNz4P4q", new Contact(null, null, null, null, null, null, null, null), new Company(
                null, null, null, businessTypes, null, null, null, null, null, null, null), ApprovalStatus.APPROVED,
                13, 586L, null, null, null);

        when(changeDataVisitor.getNewVersion(supplier.getVersion(), supplier.getApprovalStatus())).thenReturn(25);
        when(changeDataVisitor.getNewApprovalStatus(supplier.getApprovalStatus())).thenReturn(ApprovalStatus.DRAFT);

        Supplier mutatedSupplier = supplierMutator.update(supplier);

        assertThat(mutatedSupplier, is(notNullValue()));
        assertThat(mutatedSupplier.getVersion(), is(25));
        assertThat(mutatedSupplier.getApprovalStatus(), is(ApprovalStatus.DRAFT));
        verify(supplierValidator).validate(supplier);
        verify(supplierWriteDao).update(supplier);
        verify(businessTypeWriteDao).updateBusinessTypes(586L, businessTypes);
        verifyZeroInteractions(pendingUserWriteDao, mailSender);
    }
}
