package com.daugherty.e2c.business.mutator;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.persistence.data.DiscountMembershipLevelReadDao;
import com.daugherty.e2c.persistence.data.DiscountMembershipLevelWriteDao;
import com.daugherty.e2c.persistence.data.DiscountSubscriptionTypeReadDao;
import com.daugherty.e2c.persistence.data.DiscountSubscriptionTypeWriteDao;
import com.daugherty.e2c.persistence.data.DiscountWriteDao;
import com.google.common.collect.Lists;

/**
 * Mutator for Discount domain objects.
 */
@Service("discountMutator")
@Transactional
public class DiscountMutator extends BaseMutator<Discount> {

    @Inject
    private Validator<Discount> discountValidator;
    @Inject
    private DiscountWriteDao discountWriteDao;
    @Inject
    private DiscountMembershipLevelWriteDao discountMembershipLevelWriteDao;
    @Inject
    private DiscountMembershipLevelReadDao discountMembershipLevelReadDao;
    @Inject
    private DiscountSubscriptionTypeWriteDao discountSubscriptionTypeWriteDao;
    @Inject
    private DiscountSubscriptionTypeReadDao discountSubscriptionTypeReadDao;

    @Override
    public Discount create(Discount discount) {
        // TODO Validate Code
        discountValidator.validate(discount);

        Discount persistedDiscount = discountWriteDao.insert(discount);
        discountMembershipLevelWriteDao.insert(persistedDiscount.getId(),
                Lists.newArrayList(persistedDiscount.getMembershipLevels()));
        discountSubscriptionTypeWriteDao.insert(persistedDiscount.getId(),
                Lists.newArrayList(persistedDiscount.getDiscountTypes()));

        return persistedDiscount;
    }

    @Override
    public Discount update(Discount discount) {
        discountValidator.validate(discount);

        Discount persistedDiscount = discountWriteDao.update(discount);
        updateMembershipLevels(discount);
        updateSubscriptionTypes(discount);

        return persistedDiscount;
    }

    @Override
    public void delete(Long entityId) {
        throw new UnsupportedOperationException();
    }

    private void updateMembershipLevels(Discount discount) {
        List<Integer> membershipLevels = Lists.newArrayList(discount.getMembershipLevels());
        List<Integer> persistedMembershipLevels = discountMembershipLevelReadDao.find(discount.getId());
        List<Integer> deletes = Lists.newArrayList();
        List<Integer> adds = Lists.newArrayList();

        for (Integer membershipLevel : membershipLevels) {
            if (!persistedMembershipLevels.contains(membershipLevel)) {
                adds.add(membershipLevel);
            }
        }

        for (Integer persistedMembershipLevel : persistedMembershipLevels) {
            if (!membershipLevels.contains(persistedMembershipLevel)) {
                deletes.add(persistedMembershipLevel);
            }
        }

        discountMembershipLevelWriteDao.delete(discount.getId(), deletes);
        discountMembershipLevelWriteDao.insert(discount.getId(), adds);
    }

    private void updateSubscriptionTypes(Discount discount) {
        List<SubscriptionType> subscriptionTypes = Lists.newArrayList(discount.getDiscountTypes());
        List<SubscriptionType> persistedSubscriptionTypes = discountSubscriptionTypeReadDao.find(discount.getId());
        List<SubscriptionType> deletes = Lists.newArrayList();
        List<SubscriptionType> adds = Lists.newArrayList();

        for (SubscriptionType subscriptionType : subscriptionTypes) {
            if (!persistedSubscriptionTypes.contains(subscriptionType)) {
                adds.add(subscriptionType);
            }
        }

        for (SubscriptionType persistedSubscriptionType : persistedSubscriptionTypes) {
            if (!subscriptionTypes.contains(persistedSubscriptionType)) {
                deletes.add(persistedSubscriptionType);
            }
        }

        discountSubscriptionTypeWriteDao.delete(discount.getId(), deletes);
        discountSubscriptionTypeWriteDao.insert(discount.getId(), adds);
    }
}
