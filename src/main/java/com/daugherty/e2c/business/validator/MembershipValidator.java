package com.daugherty.e2c.business.validator;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.MembershipReadDao;
import com.google.common.collect.Lists;

@Component("membershipValidator")
public class MembershipValidator implements Validator<Membership> {
    @Inject
    private MembershipReadDao membershipReadDao;

    @Override
    public void validate(Membership membership) {
        ValidationError errors = new ValidationError();

        errors.add(membership.validate());

        List<Membership> memberships = membershipReadDao.getMemberships(membershipReadDao.createQueryCriteria(null,
                membership.getSupplierId(), "effectiveDate", false, 1, 250, Locale.ENGLISH));

        if (ApprovalStatus.PAID.equals(membership.getApprovalStatus())
                || (ApprovalStatus.PROVISIONAL.equals(membership.getApprovalStatus()) && !isUpgrade(membership))) {
            validateMembershipDates(membership, memberships, errors);
        }

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

    private boolean isUpgrade(Membership membership) {

        Membership activeMembership;
        try {
            activeMembership = membershipReadDao.loadBySupplierId(membership.getSupplierId());
        } catch (EmptyResultDataAccessException e) {
            return false;
        }

        return membership.getLevel().getValue() > activeMembership.getLevel().getValue();
    }

    private void validateMembershipDates(Membership membership, List<Membership> memberships, ValidationError errors) {

        List<Membership> existingMemberships = getExistingMemberships(membership, memberships);

        if (isDateWithinRange(membership.getEffectiveDate(), existingMemberships)) {
            errors.add(Membership.EFFECTIVE_DATE_SERIAL_PROPERTY, Validatable.EFFECTIVE_DATE_INVALID_RANGE);
        }

        if (isDateWithinRange(membership.getExpirationDate(), existingMemberships)) {
            errors.add(Membership.EXPIRATION_DATE_SERIAL_PROPERTY, Validatable.EXPIRATION_DATE_INVALID_RANGE);
        }
    }

    private List<Membership> getExistingMemberships(Membership membership, List<Membership> memberships) {
        List<Membership> existingMemberships = Lists.newArrayList();

        for (Membership existingMembership : memberships) {
            if (!membership.getId().equals(existingMembership.getId())) {
                existingMemberships.add(existingMembership);
            }
        }

        return existingMemberships;
    }

    private boolean isDateWithinRange(Date date, List<Membership> existingMemberships) {

        for (Membership membership : existingMemberships) {
            if (date.getTime() >= membership.getEffectiveDate().getTime()
                    && date.getTime() <= membership.getExpirationDate().getTime()) {
                return true;
            }
        }

        return false;
    }
}
