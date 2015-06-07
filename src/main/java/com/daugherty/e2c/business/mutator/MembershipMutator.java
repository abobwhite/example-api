package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.persistence.data.MembershipDiscountWriteDao;
import com.daugherty.e2c.persistence.data.MembershipLevelReadDao;
import com.daugherty.e2c.persistence.data.MembershipWriteDao;

/**
 * Mutator for Membership domain objects.
 */
@Service("membershipMutator")
@Transactional
public class MembershipMutator extends BaseMutator<Membership> {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private Validator<Membership> membershipValidator;
    @Inject
    private MembershipLevelReadDao membershipLevelReadDao;
    @Inject
    private MembershipWriteDao membershipWriteDao;
    @Inject
    private MembershipDiscountWriteDao membershipDiscountWriteDao;

    @Override
    public Membership create(Membership entity) {
        throw new UnsupportedOperationException();
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

        membershipValidator.validate(membership);
        membershipWriteDao.update(membership);
        membership.setMembershipDiscounts(membershipDiscountWriteDao.updateMembershipDiscounts(
                membership.getSnapshotId(), membership.getMembershipDiscounts()));

        return membership;
    }

    @Override
    public void delete(Long membershipId) {
        throw new UnsupportedOperationException();
    }
}