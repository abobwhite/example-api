package com.daugherty.e2c.service.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.security.Role;
import com.paypal.ipn.IPNMessage;

/**
 * REST resource for User.
 */
@Controller
@RequestMapping("/ipn")
public class IPNService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private HashMap<String, String> configurationMap;
    @Inject
    private Accessor<Membership> membershipAccessor;
    @Inject
    private Mutator<Membership> provisionalMembershipMutator;

    @RequestMapping(method = RequestMethod.POST)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public void payPalIPNCallback(HttpServletRequest request, HttpServletResponse response) {
        IPNMessage ipnlistener = new IPNMessage(request, configurationMap);
        boolean isIpnVerified = ipnlistener.validate();
        String transactionType = ipnlistener.getTransactionType();
        Map<String, String> map = ipnlistener.getIpnMap();

        LOGGER.info("******* IPN (name:value) pair : " + map + "  " + "######### TransactionType : " + transactionType
                + "  ======== IPN verified : " + isIpnVerified);

        if ("Completed".equals(map.get("payment_status"))) {
            Long membershipId = Long.valueOf(map.get("custom"));
            String paymentInvoice = String.valueOf(map.get("txn_id"));
            BigDecimal paymentAmount = new BigDecimal(map.get("payment_gross"));

            LOGGER.info("******* Membership Id : " + membershipId + " *******");
            LOGGER.info("******* Transaction Id : " + paymentInvoice + " *******");
            LOGGER.info("******* Payment Amount : " + paymentAmount + " *******");

            Membership provisonalMembership = membershipAccessor.load(membershipId, Locale.ENGLISH);

            provisonalMembership.setPaymentType(PaymentType.PAYPAL);
            provisonalMembership.setApprovalStatus(ApprovalStatus.PAID);
            provisonalMembership.setPaymentInvoice(paymentInvoice);
            provisonalMembership.setPaymentAmount(paymentAmount);

            provisionalMembershipMutator.update(provisonalMembership);
        }
    }
}
