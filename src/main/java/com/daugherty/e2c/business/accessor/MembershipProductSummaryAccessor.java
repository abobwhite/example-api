package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipProductSummary;
import com.daugherty.e2c.persistence.data.MembershipProductSummaryReadDao;
import com.daugherty.e2c.persistence.data.MembershipReadDao;

/**
 * Accessor for Membership Product Summary domain object.
 */
@Service("membershipProductSummaryAccessor")
public class MembershipProductSummaryAccessor extends BaseAccessor<MembershipProductSummary> {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private MembershipProductSummaryReadDao membershipProductSummaryReadDao;
    @Inject
    private MembershipReadDao membershipReadDao;

    @Override
    public MembershipProductSummary load(Long id, Locale locale) {
        MembershipProductSummary productSummary;
        try {
            productSummary = membershipProductSummaryReadDao.loadByMembershipId(id);
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("This membership does not have any products yet");
            productSummary = new MembershipProductSummary(id);
        }

        setRemainingProducts(id, productSummary);
        setRemainingPublishedProducts(id, productSummary);

        return productSummary;
    }

    private void setRemainingProducts(Long membershipId, MembershipProductSummary productSummary) {

        try {
            productSummary.setProductsRemaining(membershipReadDao.loadTotalProductsByMembershipId(membershipId));
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("No Membership found");
        }

    }

    private void setRemainingPublishedProducts(Long membershipId, MembershipProductSummary productSummary) {
        try {
            Membership membership = membershipReadDao.loadByMembershipId(membershipId);
            productSummary.setPublishRemaining(membership.getLevel().getProductCount() - productSummary.getPublished());
            productSummary.setHotProductsRemaining(membership.getLevel().getHotProductCount()
                    - productSummary.getHotProducts());

        } catch (EmptyResultDataAccessException e) {
            LOG.debug("No Membership found, this is probably a new Supplier");
        }
    }

    @Override
    public List<MembershipProductSummary> find(Filter<MembershipProductSummary> filter) {
        throw new UnsupportedOperationException();
    }
}
