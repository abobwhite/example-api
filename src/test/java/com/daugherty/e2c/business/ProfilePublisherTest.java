package com.daugherty.e2c.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.MembershipProductSummary;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.sender.RegistrationApprovedMailSender;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

@RunWith(MockitoJUnitRunner.class)
public class ProfilePublisherTest {
    private static final String EMAIL_ADDRESS = "Email Address";
    private static final String FIRST_NAME = "First Name";
    private static final Language LANGUAGE = Language.SPANISH;

    @Mock
    private SupplierReadDao supplierReadDao;
    @Mock
    private Supplier supplier;
    @Mock
    private Contact contact;
    @Mock
    private Accessor<MembershipProductSummary> supplierProductSummaryAccessor;
    @Mock
    private RegistrationApprovedMailSender supplierTranslationPublishedMailSender;
    @Mock
    private MembershipProductSummary supplierProductSummary;
    @Mock
    private Hashids hashids;

    @InjectMocks
    private final ProfilePublisher profilePublisher = new ProfilePublisher();

    @Before
    public void setUp() {
        when(supplierReadDao.loadLatest(1L, Locale.ENGLISH)).thenReturn(supplier);
        when(supplier.getMembershipId()).thenReturn(1L);
        when(supplierProductSummaryAccessor.load(1L, Locale.ENGLISH)).thenReturn(supplierProductSummary);
        when(supplier.getContact()).thenReturn(contact);
        when(supplier.getEmailWithPersonalName()).thenReturn(EMAIL_ADDRESS);
        when(contact.getFirstName()).thenReturn(FIRST_NAME);
        when(contact.getLanguage()).thenReturn(LANGUAGE);
    }

    @Test
    public void emailIsSentWithProductsWhenSupplierHasProducts() {
        when(supplier.getVersion()).thenReturn(1);
        when(supplierProductSummary.getPendingApproval()).thenReturn(1);
        when(supplierProductSummary.getPendingTranslation()).thenReturn(1);
        when(supplierProductSummary.getWaitingForInformation()).thenReturn(1);
        when(supplierProductSummary.getApproved()).thenReturn(1);
        
        when(hashids.decode("vXP9l0Or")).thenReturn(new long[]{1L});

        profilePublisher.publish("vXP9l0Or");

        verify(supplierTranslationPublishedMailSender).sendToSupplierWithProducts(FIRST_NAME, EMAIL_ADDRESS, LANGUAGE);
    }

    @Test
    public void emailIsSentWithoutProductsWhenSupplierHasNoProducts() {
        when(supplier.getVersion()).thenReturn(1);
        when(supplierProductSummary.getPendingApproval()).thenReturn(0);
        when(supplierProductSummary.getPendingTranslation()).thenReturn(0);
        when(supplierProductSummary.getWaitingForInformation()).thenReturn(0);
        when(supplierProductSummary.getApproved()).thenReturn(0);

        when(hashids.decode("vXP9l0Or")).thenReturn(new long[]{1L});
        
        profilePublisher.publish("vXP9l0Or");

        verify(supplierTranslationPublishedMailSender).sendToSupplier(FIRST_NAME, EMAIL_ADDRESS, LANGUAGE);
    }

    @Test
    public void emailIsNotSentWhenSupplierVersionIsGreaterThan1() {
        when(supplier.getVersion()).thenReturn(2);
        
        when(hashids.decode("vXP9l0Or")).thenReturn(new long[]{1L});

        profilePublisher.publish("vXP9l0Or");

        verifyZeroInteractions(supplierTranslationPublishedMailSender);
    }

}
