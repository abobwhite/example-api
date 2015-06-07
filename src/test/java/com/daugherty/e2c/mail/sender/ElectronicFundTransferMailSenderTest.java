package com.daugherty.e2c.mail.sender;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: SHK0723
 * Date: 12/17/13
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class ElectronicFundTransferMailSenderTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private EmailFactory englishSupplierElectronicFundTransferEmail;

    @InjectMocks
    private ElectronicFundTransferMailSender electronicFundTransferMailSender = new ElectronicFundTransferMailSender("jerryRocks@gareth.com");

    @Before
    public void setup() {
        when(emailUrlBuilder.buildGlobalSiteUrl()).thenReturn("supplierUrl");
        when(emailUrlBuilder.buildMailUrl(Language.ENGLISH)).thenReturn("englishPostUrl");
    }

    @Test
    public void send(){
        String membershipLevel = "One";
        String membershipPrice = "123.00";
        String contactName = "John, Doe";
        String supplierEmailAddress = "john.doe@xyz.com";
        String toEmail = "jerryRocks@gareth.com";

        Email email = new Email();
        when(englishSupplierElectronicFundTransferEmail.createEmail(toEmail, new Object[] { membershipLevel, membershipPrice, contactName, supplierEmailAddress,  "supplierUrl"})).thenReturn(email);

        electronicFundTransferMailSender.send(membershipLevel, membershipPrice, contactName, supplierEmailAddress, Language.ENGLISH);

        verify(restTemplate).postForObject("englishPostUrl", email, Email.class);
    }
}
