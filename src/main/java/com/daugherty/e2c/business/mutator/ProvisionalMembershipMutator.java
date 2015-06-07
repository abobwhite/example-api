package com.daugherty.e2c.business.mutator;

import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateMidnight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.sender.MembershipRenewalMailSender;
import com.daugherty.e2c.mail.sender.MembershipUpgradeMailSender;
import com.daugherty.e2c.mail.sender.NewMembershipMailSender;
import com.daugherty.e2c.persistence.data.MembershipDiscountWriteDao;
import com.daugherty.e2c.persistence.data.MembershipLevelReadDao;
import com.daugherty.e2c.persistence.data.MembershipWriteDao;

/**
 * Mutator for Membership domain objects.
 */
@SuppressWarnings("deprecation")
@Service("provisionalMembershipMutator")
@Transactional
public class ProvisionalMembershipMutator extends BaseMutator<Membership> {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private MembershipLevelReadDao membershipLevelReadDao;
    @Inject
    private MembershipWriteDao membershipWriteDao;
    @Inject
    private MembershipDiscountWriteDao membershipDiscountWriteDao;
    @Inject
    private Accessor<Membership> latestPaidSupplierMembershipAccessor;
    @Inject
    private Accessor<Supplier> latestSupplierAccessor;
    @Inject
    private NewMembershipMailSender newMembershipMailSender;
    @Inject
    private MembershipUpgradeMailSender upgradeMembershipMailSender;
    @Inject
    private MembershipRenewalMailSender renewalMembershipMailSender;

    @Override
    public Membership create(Membership entity) {
        MembershipLevel membershipLevel = membershipLevelReadDao.loadByValue(entity.getLevel().getValue());

        Membership membership = new Membership(null, entity.getSupplierId(), membershipLevel,
                entity.getApprovalStatus(), entity.getVersion(), entity.getPurchaseDate(), entity.getEffectiveDate(),
                entity.getExpirationDate(), entity.getPurchasePrice(), entity.getPaymentAmount(),
                entity.getSnapshotId(), entity.getPaymentType(), entity.getPaymentInvoice(),
                entity.getEarlyRenewalDiscount(), entity.getUpgradeCredit(), entity.getMembershipDiscounts());

        membershipWriteDao.insert(membership);
        membership.setMembershipDiscounts(membershipDiscountWriteDao.updateMembershipDiscounts(
                membership.getSnapshotId(), membership.getMembershipDiscounts()));

        return membership;
    }

    @Override
    public Membership update(Membership entity) {
        MembershipLevel membershipLevelReferenceData = membershipLevelReadDao.loadByValue(entity.getLevel().getValue());

        MembershipLevel membershipLevel = new MembershipLevel(membershipLevelReferenceData.getId(),
                membershipLevelReferenceData.getValue(), entity.getLevel().getPrice(), entity.getLevel()
                        .getMonthsOfTerm(), entity.getLevel().getProductCount(),
                membershipLevelReferenceData.getMessageCount(), membershipLevelReferenceData.getTranslationCount(),
                membershipLevelReferenceData.isProfilePublic(), entity.getLevel().getHotProductCount(), entity
                        .getLevel().isIncludedInProductAlerts(), entity.getLevel().isSupplierMessagingEnabled(), entity
                        .getLevel().isExportTutorialAccessible(), entity.getLevel().getAdditionalProductImageCount(),
                entity.getLevel().isVerifiableByThirdParty(),
                entity.getLevel().isAdvancedWebAndMailCapabilityEnabled(), entity.getLevel().isVideoUploadable(),
                entity.getLevel().isContactChinaDirect(), entity.getLevel().isMarketAnalysis(), entity.getLevel()
                        .isBuyerSearch(), entity.getLevel().isLogisticsAssistance());

        Membership membership = new Membership(entity.getId(), entity.getSupplierId(), membershipLevel,
                entity.getApprovalStatus(), entity.getVersion(), entity.getPurchaseDate(), entity.getEffectiveDate(),
                entity.getExpirationDate(), entity.getPurchasePrice(), entity.getPaymentAmount(),
                entity.getSnapshotId(), entity.getPaymentType(), entity.getPaymentInvoice(),
                entity.getEarlyRenewalDiscount(), entity.getUpgradeCredit(), entity.getMembershipDiscounts());

        if (ApprovalStatus.PAID.equals(membership.getApprovalStatus())) {
            try {
                Supplier supplier = latestSupplierAccessor.load(membership.getSupplierId(), Locale.ENGLISH);
                Membership activeMembership = latestPaidSupplierMembershipAccessor.load(membership.getSupplierId(),
                        Locale.ENGLISH);

                if (isNewSupplierMembership(activeMembership)) {
                    newMembershipMailSender.sendEmail(supplier, membershipLevel);
                } else {
                    if (isUpgrade(membership, activeMembership)) {
                        updateActiveMembershipExpirationDateOnUpgrade(activeMembership, membership, supplier);
                    } else {
                        renewalMembershipMailSender.sendEmail(supplier, membershipLevel);
                    }
                }
            } catch (EmptyResultDataAccessException e) {
                LOGGER.debug("No Membership found, this is probably a new Supplier " + membership.getSupplierId());
            }

        }

        updateMembership(membership);
        return membership;
    }

    @Override
    public void delete(Long membershipId) {
        throw new UnsupportedOperationException();
    }

    private void updateActiveMembershipExpirationDateOnUpgrade(Membership activeMembership,
            Membership provisonalMembership, Supplier supplier) {

        if (activeMembership != null && !activeMembership.isExpired()
                && isUpgrade(provisonalMembership, activeMembership)) {
            LOGGER.info("Active Membership Found for ID" + activeMembership.getId());
            Date expirationDate = new DateMidnight(provisonalMembership.getEffectiveDate()).minusDays(1).toDate();
            LOGGER.info("Setting Expiration Date on Membsership " + activeMembership.getId() + " to "
                    + expirationDate.toString());

            if (DateUtils.isSameDay(activeMembership.getEffectiveDate(), new Date())) {
                activeMembership.setEffectiveDate(expirationDate);
            }

            activeMembership.setExpirationDate(expirationDate);

            updateMembership(activeMembership);

            upgradeMembershipMailSender.sendEmail(supplier, provisonalMembership.getLevel());
        }
    }

    private void updateMembership(Membership membership) {
        membershipWriteDao.update(membership);
        membership.setMembershipDiscounts(membershipDiscountWriteDao.updateMembershipDiscounts(
                membership.getSnapshotId(), membership.getMembershipDiscounts()));
    }

    private boolean isNewSupplierMembership(Membership activeMembership) {
        return activeMembership == null;
    }

    private boolean isUpgrade(Membership provisonalMembership, Membership activeMembership) {
        return provisonalMembership.getLevel().getValue() > activeMembership.getLevel().getValue();
    }

}