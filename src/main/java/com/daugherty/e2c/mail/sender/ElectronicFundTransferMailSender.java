package com.daugherty.e2c.mail.sender;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;

public class ElectronicFundTransferMailSender {
    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private EmailFactory englishSupplierElectronicFundTransferEmail;

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private String eftMailAddress;
    
    public ElectronicFundTransferMailSender(String eftMailAddress){
        this.eftMailAddress = eftMailAddress;
    }

    public void send(String membershipLevel, String memberShipPrice, String contactName, String supplierEmailAddress, Language language) {
        try {
            Email electronicFundTransferEmail = getElectronicFundTransferEmail(membershipLevel, memberShipPrice, contactName, supplierEmailAddress);
            restTemplate.postForObject(emailUrlBuilder.buildMailUrl(language), electronicFundTransferEmail, Email.class);
            LOG.info("Sent EFT notice for supplier "+ contactName + " to " + eftMailAddress);
        } catch (RuntimeException e) {
            LOG.warn("Unable to send EFT mail for contact " + contactName + " to "+ eftMailAddress, e);
        }
    }

    private Email getElectronicFundTransferEmail(String membershipLevel, String memberShipPrice, String contactName, String supplierEmailAddress) {
        return englishSupplierElectronicFundTransferEmail.createEmail(eftMailAddress, new Object[]{
                membershipLevel, memberShipPrice, contactName, supplierEmailAddress, emailUrlBuilder.buildGlobalSiteUrl()});

    }
}
