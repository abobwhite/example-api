package com.daugherty.e2c.business;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import urn.ebay.api.PayPalAPI.BMCreateButtonReq;
import urn.ebay.api.PayPalAPI.BMCreateButtonResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.UrlBuilder;
import com.daugherty.e2c.service.json.JsonMembership;

@RunWith(MockitoJUnitRunner.class)
public class PayPalLinkBuilderTest {
    private MembershipLevel membershipLevel = new MembershipLevel(1L, 1, new BigDecimal(1000.00), 6, 5, 2147483647, 600, true, 0, false,
            false, false, 0, false, false, false, false, false, false, false);
    private Membership membership = new Membership(42L, membershipLevel);

    @Mock
    private PayPalAPIInterfaceServiceService payPalService;
    @Mock
    private Accessor<Supplier> latestSupplierAccessor;
    @Mock
    private Supplier supplier;
    @Mock
    private Contact contact;
    @Mock
    private UrlBuilder urlBuilder;
    @Mock
    private BMCreateButtonResponseType response;
    @Captor
    private ArgumentCaptor<BMCreateButtonReq> requestArgumentCaptor;

    @InjectMocks
    private PayPalLinkBuilder payPalLinkBuilder = new PayPalLinkBuilder();

    @Before
    public void setup() {
        when(latestSupplierAccessor.load(Mockito.anyString(), Mockito.any(Locale.class))).thenReturn(supplier);
        when(supplier.getContact()).thenReturn(contact);
    }

    @Test
    public void payPalLinkNotCreatedWhenApprovalStatusIsNotProvisional() {
        JsonMembership jsonMembership = new JsonMembership(membership, "ABC123");
        jsonMembership.setPaymentType(PaymentType.PAYPAL.getType());
        jsonMembership.setApprovalStatus("Paid");

        String payPalLink = payPalLinkBuilder.build(jsonMembership, Locale.ENGLISH);
        assertThat(payPalLink, nullValue());
        verifyZeroInteractions(urlBuilder, payPalService);

    }

    @Test
    public void payPalLinkCreatedWhenPaymentMethodPayPalApprovalStatusProvisional() throws Exception {
        String expectedLink = "random_pay_pal_link";
        JsonMembership jsonMembership = new JsonMembership(membership, "ABC123");
        jsonMembership.setPaymentType(PaymentType.PAYPAL.getType());
        jsonMembership.setApprovalStatus(ApprovalStatus.PROVISIONAL.getName());

        when(urlBuilder.buildHttpsUrl("/dashboard/supplier/membership/checkout", Locale.ENGLISH)).thenReturn(
                "e2c/dashboard/supplier/membership/checkout");
        when(urlBuilder.buildHttpsUrl("/dashboard/supplier/membership/confirm", Locale.ENGLISH)).thenReturn(
                "e2c/dashboard/supplier/membership/confirm");

        when(payPalService.bMCreateButton(Mockito.any(BMCreateButtonReq.class))).thenReturn(response);
        when(response.getEmail()).thenReturn(expectedLink);

        String payPalLink = payPalLinkBuilder.build(jsonMembership, Locale.ENGLISH);
        assertThat(payPalLink, is(expectedLink));

        verify(payPalService).bMCreateButton(requestArgumentCaptor.capture());

    }
}
