package com.daugherty.e2c.business;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipProductSummary;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.HotProductOverrideWriteDao;
import com.daugherty.e2c.persistence.data.MembershipProductSummaryReadDao;
import com.daugherty.e2c.persistence.data.ProductPublicationWriteDao;
import com.daugherty.e2c.persistence.data.ProductReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

/**
 * Updates the publication state of a Product.
 */
@Service
@Transactional
public class ProductPublisher {

    @Inject
    private MembershipProductSummaryReadDao productSummaryReadDao;
    @Inject
    private ProductPublicationWriteDao productPublicationWriteDao;
    @Inject
    private ProductReadDao productReadDao;
    @Inject
    private HotProductOverrideWriteDao hotProductOverrideWriteDao;
    @Inject
    private SupplierReadDao supplierReadDao;
    @Inject
    private MembershipRenewalTypeHelper membershipRenewalTypeHelper;

    public void publish(Long id) {
        if (publicationsAreAvailable(id)) {
            productPublicationWriteDao.insertPublished(id, true);
        } else {
            ValidationError validationError = new ValidationError();
            validationError.add(Product.PUBLISHED_SERIAL_PROPERTY, Validatable.PRODUCT_PUBLICATIONS_LENGTH);
            throw new ValidationException(validationError);
        }
    }

    private boolean publicationsAreAvailable(Long id) {
        Product product = productReadDao.loadLatest(id, Locale.ENGLISH);
        Long supplierId = product.getInformation().getSupplier().getId();
        Supplier supplier = supplierReadDao.loadLatest(supplierId, Locale.ENGLISH);
        Membership membership = supplier.getMembership();
        MembershipProductSummary membershipSummary = productSummaryReadDao.loadByMembershipId(membership.getId());

        return membershipSummary.getPublished() < membership.getLevel().getProductCount();
    }

    public void unpublish(Long id) {
        cool(id);
        productPublicationWriteDao.insertPublished(id, false);
    }

    public void heat(Long id) {
        if (hotsAreAvailable(id)) {
            productPublicationWriteDao.insertHot(id, true);
        } else {
            ValidationError validationError = new ValidationError();
            validationError.add(Product.HOT_PRODUCT_SERIAL_PROPERTY, Validatable.PRODUCT_HOTS_LENGTH);
            throw new ValidationException(validationError);
        }
    }

    private boolean hotsAreAvailable(Long id) {
        Product product = productReadDao.loadLatest(id, Locale.ENGLISH);

        Long supplierId = product.getInformation().getSupplier().getId();
        Supplier supplier = supplierReadDao.loadLatest(supplierId, Locale.ENGLISH);
        Membership membership = supplier.getMembership();
        Membership provisionalMembership = supplier.getProvisionalMembership();

        boolean hasHotProductsRemaining = false;
        MembershipProductSummary membershipSummary;

        if (membership != null) {
            membershipSummary = productSummaryReadDao.loadByMembershipId(membership.getId());
            hasHotProductsRemaining = membershipSummary.getHotProducts() < membership.getLevel().getHotProductCount();
        }

        if (!hasHotProductsRemaining && provisionalMembership != null
                && !membershipRenewalTypeHelper.hasProvisionalRenewalOrDowngrade(membership, provisionalMembership)) {
            membershipSummary = productSummaryReadDao.loadByMembershipId(provisionalMembership.getId());
            hasHotProductsRemaining = membershipSummary.getHotProducts() < provisionalMembership.getLevel()
                    .getHotProductCount();
        }

        return hasHotProductsRemaining;
    }

    public void cool(Long id) {
        productPublicationWriteDao.insertHot(id, false);
    }

    public void addHotProductOverride(Long productId) {
        hotProductOverrideWriteDao.addHotProductOverride(productId);
    }

    public void removeHotProductOverride(Long productId) {
        hotProductOverrideWriteDao.removeHotProductOverride(productId);
    }

}
