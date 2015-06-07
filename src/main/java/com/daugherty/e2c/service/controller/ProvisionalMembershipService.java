package com.daugherty.e2c.service.controller;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.MembershipFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.PayPalLinkBuilder;
import com.daugherty.e2c.business.accessor.ProvisionalMembershipAccessor;
import com.daugherty.e2c.business.mapper.ProvisionalMembershipMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.sender.ElectronicFundTransferMailSender;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonProvisionalMembership;

/**
 * REST resource for Provisional Memberships.
 */
@Controller
@RequestMapping("/provisionalMemberships")
public class ProvisionalMembershipService {
    @Inject
    private MembershipFactory membershipFactory;
    @Inject
    private Accessor<Membership> membershipAccessor;
    @Inject
    private Mutator<Membership> provisionalMembershipMutator;
    @Inject
    private PayPalLinkBuilder payPalLinkBuilder;
    @Inject
    private ElectronicFundTransferMailSender electronicFundTransferMailSender;
    @Inject
    private Accessor<Supplier> latestSupplierAccessor;
    @Inject
    private ProvisionalMembershipMapper provisionalMembershipMapper;
    @Inject
    private ProvisionalMembershipAccessor provisionalMembershipAccessor;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN, Role.SUPPLIER })
    @ResponseBody
    public JsonProvisionalMembership retrieveMembership(@PathVariable Long id, Locale locale) {
        return buildJsonMemberShip(locale, membershipAccessor.load(id, Locale.ENGLISH));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN, Role.SUPPLIER })
    @ResponseBody
    public JsonProvisionalMembership createMembership(@RequestBody JsonProvisionalMembership jsonMembership) {
        Membership existingProvisionalMembership = provisionalMembershipAccessor.load(jsonMembership.getSupplierId(),
                Locale.ENGLISH);
        Membership membershipResult = null;

        if (existingProvisionalMembership == null) {
            Membership builtMembership = membershipFactory.build(provisionalMembershipMapper
                    .toDomainObject(jsonMembership));
            builtMembership.setApprovalStatus(ApprovalStatus.DRAFT);
            membershipResult = provisionalMembershipMutator.create(builtMembership);
        } else {
            Membership builtMembership = membershipFactory.build(provisionalMembershipMapper.toExistingDomainObject(
                    existingProvisionalMembership.getId(), jsonMembership));
            builtMembership.setApprovalStatus(ApprovalStatus.DRAFT);
            membershipResult = provisionalMembershipMutator.update(builtMembership);
        }

        return provisionalMembershipMapper.fromDomainObject(membershipResult);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN, Role.SUPPLIER })
    @ResponseBody
    public JsonProvisionalMembership updateMembership(@PathVariable Long id,
            @RequestBody JsonProvisionalMembership jsonMembership, Locale locale) {

        Membership membership = provisionalMembershipMapper.toExistingDomainObject(id, jsonMembership);

        JsonProvisionalMembership updatedJsonMembership = buildJsonMemberShip(locale,
                provisionalMembershipMutator.update(membership));
        handleMembershipBasedOnPaymentType(locale, membership);
        return updatedJsonMembership;
    }

    private void handleMembershipBasedOnPaymentType(Locale locale, Membership membership) {
        if (PaymentType.EFT.equals(membership.getPaymentType())
                && ApprovalStatus.PROVISIONAL.equals(membership.getApprovalStatus())) {
            Supplier supplier = latestSupplierAccessor.load(membership.getSupplierId(), locale);
            Contact contact = supplier.getContact();
            String contactName = contact.getFirstName() + ", " + contact.getLastName();
            electronicFundTransferMailSender.send(String.valueOf(membership.getLevel().getValue()),
                    String.valueOf(membership.getPurchasePrice().doubleValue()), contactName,
                    contact.getEmailAddress(), Language.ENGLISH);
        }
    }

    private JsonProvisionalMembership buildJsonMemberShip(Locale locale, Membership membership) {
        JsonProvisionalMembership jsonMembership = provisionalMembershipMapper.fromDomainObject(membership);
        if (PaymentType.PAYPAL.equals(membership.getPaymentType())
                && ApprovalStatus.PROVISIONAL.equals(membership.getApprovalStatus())) {
            jsonMembership.setPaypalLink(payPalLinkBuilder.build(jsonMembership, locale));
        }
        return jsonMembership;
    }
}
