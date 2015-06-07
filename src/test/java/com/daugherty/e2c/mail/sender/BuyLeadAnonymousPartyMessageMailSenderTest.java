package com.daugherty.e2c.mail.sender;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;

@RunWith(MockitoJUnitRunner.class)
public class BuyLeadAnonymousPartyMessageMailSenderTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Email email;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private EmailFactory buyLeadAnonymousPartyMessageNotificationEmail;

    @InjectMocks
    private final BuyLeadAnonymousPartyMessageMailSender mailSender = new BuyLeadAnonymousPartyMessageMailSender();

    @Captor
    private ArgumentCaptor<Object[]> bodyArgumentsCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUpCommonMocks() {
        when(emailUrlBuilder.buildBuyerSiteUrl()).thenReturn("buyerSiteUrl");
        when(emailUrlBuilder.buildMailUrl(Language.CHINESE)).thenReturn("chinaMailUrl");
    }

    @Test
    public void doesNotSendEmailWhenSupplier() throws Exception {
        Membership membership = new Membership(1L, new MembershipLevel(1L, 2, BigDecimal.valueOf(10), 6, 10, 20, 20,
                false, 10, false, false, false, 10, false, true, false, true, false, false, false));

        Supplier supplier = new Supplier(2L, "GpP8xPem", new Contact("supplierFirst", "supplierLast", null, null,
                "supplier@somewhere.com", null, Language.ENGLISH, null), null, null, null, null, null, null, null);

        supplier.getMemberships().add(membership);

        BuyLead buyLead = new BuyLead(3L, supplier, new ProductCategory(10L), new Date(), new Date());

        mailSender.send(buyLead);

        verifyZeroInteractions(buyLeadAnonymousPartyMessageNotificationEmail, restTemplate);
    }

    @Test
    public void sendEmailToAnonymous() throws Exception {
        Anonymous anonymous = new Anonymous(1L, "vXP9l0Or", new Contact("anonymousFirst", "anonymousLast", null, null,
                "anonymous@somewhere.com", null, null, null), null, null, null, null);
        BuyLead buyLead = new BuyLead(3L, anonymous, new ProductCategory(10L), new Date(), new Date());

        when(
                buyLeadAnonymousPartyMessageNotificationEmail.createEmail(eq(anonymous.getEmailWithPersonalName()),
                        any(Object[].class))).thenReturn(email);

        mailSender.send(buyLead);

        verify(buyLeadAnonymousPartyMessageNotificationEmail).createEmail(eq(anonymous.getEmailWithPersonalName()),
                bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(1));
        assertThat(bodyArguments[0].toString(), is("buyerSiteUrl/registration/buyer"));

        verify(restTemplate).postForObject("chinaMailUrl", email, Email.class);
    }
}
