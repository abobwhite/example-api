package com.daugherty.e2c.business;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import urn.ebay.api.PayPalAPI.BMCreateButtonReq;
import urn.ebay.api.PayPalAPI.BMCreateButtonRequestType;
import urn.ebay.api.PayPalAPI.BMCreateButtonResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.apis.eBLBaseComponents.ButtonCodeType;
import urn.ebay.apis.eBLBaseComponents.ButtonTypeType;
import urn.ebay.apis.eBLBaseComponents.CountryCodeType;
import urn.ebay.apis.eBLBaseComponents.ErrorType;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.SecurityUtils;
import com.daugherty.e2c.domain.UrlBuilder;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonMembership;
import com.google.common.collect.Lists;

/**
 * Creates an Encrypted Button by calling Pay Pal API with membership information
 */
@Component
public class PayPalLinkBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String SUBTOTAL_KEY = "subtotal";
    private static final String CUSTOM_KEY = "custom";
    private static final String NOTIFY_URL_KEY = "notify_url";
    private static final String CANCEL_URL_KEY = "cancel_return";
    private static final String RETURN_URL_KEY = "return";

    @Inject
    private PayPalAPIInterfaceServiceService payPalService;
    @Inject
    private Accessor<Party> partyAccessor;
    @Inject
    private UrlBuilder urlBuilder;

    public String build(JsonMembership membership, Locale locale) {
        String payPalLink = null;
        if (ApprovalStatus.PROVISIONAL.equals(ApprovalStatus.findByName(membership.getApprovalStatus()))) {

            Party supplier = partyAccessor.load(membership.getSupplierId(), locale);

            String subtotal = SUBTOTAL_KEY + "=" + membership.getPurchasePrice().doubleValue();
            String customId = CUSTOM_KEY + "=" + membership.getId();
            String cancelUrl = getCancelUrl();
            String confirmUrl = getReturnUrl();
            String notifyUrl = NOTIFY_URL_KEY + "=" + urlBuilder.buildHttpsUrl("/e2c-api/ipn", Locale.ENGLISH);
            String email = "email=" + supplier.getContact().getEmailAddress();
            String firstName = "first_name=" + supplier.getContact().getFirstName();
            String lastName = "last_name=" + supplier.getContact().getLastName();
            String billingFirstName = "billing_first_name=" + supplier.getContact().getFirstName();
            String billingLastName = "billing_last_name=" + supplier.getContact().getLastName();
            String language = Language.ENGLISH.equals(supplier.getContact().getLanguage()) ? "lc="
                    + CountryCodeType.US.getValue() : "lc=" + CountryCodeType.ES.getValue();
            String paymentAction = "paymentaction=sale";
            String currencyCode = "currency_code=USD";
            String template = "template=templateB";
            String logoImage = "logoImage=" + urlBuilder.buildGlobalSiteUrl() + "/img/paypal/header.png";
            String orderSummaryImage = "orderSummaryBgImage=" + urlBuilder.buildGlobalSiteUrl()
                    + "/img/paypal/summary.jpg";

            List<String> buttonVariable = Lists.newArrayList(paymentAction, currencyCode, subtotal, customId,
                    cancelUrl, confirmUrl, notifyUrl, email, firstName, lastName, billingFirstName, billingLastName,
                    language, template, logoImage, orderSummaryImage);

            BMCreateButtonRequestType bmCreateButtonRequestType = new BMCreateButtonRequestType();
            bmCreateButtonRequestType.setButtonCode(ButtonCodeType.TOKEN);
            bmCreateButtonRequestType.setButtonType(ButtonTypeType.PAYMENT);
            bmCreateButtonRequestType.setButtonLanguage(locale.getLanguage());
            bmCreateButtonRequestType.setButtonVar(buttonVariable);
            bmCreateButtonRequestType.setButtonCountry(Locale.ENGLISH.equals(locale) ? CountryCodeType.US
                    : CountryCodeType.ES);

            BMCreateButtonReq bmCreateButtonReq = new BMCreateButtonReq();
            bmCreateButtonReq.setBMCreateButtonRequest(bmCreateButtonRequestType);

            try {
                BMCreateButtonResponseType bMCreateButtonResponseType = payPalService.bMCreateButton(bmCreateButtonReq);

                payPalLink = bMCreateButtonResponseType.getEmail();

                for (ErrorType errorType : bMCreateButtonResponseType.getErrors()) {
                    System.out.println(errorType.getErrorCode());
                    System.out.println(errorType.getLongMessage());
                }
            } catch (Exception e) {
                LOGGER.error("Error Creating PayPal Payment Link", e);

                ValidationError validationError = new ValidationError();
                validationError.add("payPalLink", "Error Creating PayPal Payment Link");

                throw new ValidationException(validationError);
            }

        }

        return payPalLink;
    }

    private String getCancelUrl() {

        String route = (SecurityUtils.authenticatedUserHasRole(Role.SALES) || SecurityUtils
                .authenticatedUserHasRole(Role.ADMIN)) ? "/dashboard/admin/users"
                : "/dashboard/supplier/membership/checkout";

        return CANCEL_URL_KEY + "=" + urlBuilder.buildGlobalSiteUrl() + route;
    }

    private String getReturnUrl() {

        String route = (SecurityUtils.authenticatedUserHasRole(Role.SALES) || SecurityUtils
                .authenticatedUserHasRole(Role.ADMIN)) ? "/dashboard/admin/users"
                : "/dashboard/supplier/membership/confirm";

        return RETURN_URL_KEY + "=" + urlBuilder.buildGlobalSiteUrl() + route;
    }
}
