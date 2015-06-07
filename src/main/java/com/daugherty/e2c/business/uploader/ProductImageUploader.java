package com.daugherty.e2c.business.uploader;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.daugherty.e2c.business.MembershipRenewalTypeHelper;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.MembershipReadDao;
import com.daugherty.e2c.persistence.data.ProductImageReadDao;
import com.daugherty.e2c.persistence.data.ProductImageWriteDao;
import com.daugherty.e2c.persistence.data.ProductReadDao;
import com.daugherty.e2c.persistence.document.DocumentType;
import com.google.common.collect.Lists;

@Service
@Transactional
public class ProductImageUploader extends BaseDocumentUploader<Product> {

    private static final int BASE_IMAGES_ALLOWED = 1;
    @Inject
    private ProductReadDao productReadDao;
    @Inject
    private ProductImageReadDao productImageReadDao;
    @Inject
    private ProductImageWriteDao productImageWriteDao;
    @Inject
    private MembershipReadDao membershipReadDao;
    @Inject
    private MembershipRenewalTypeHelper membershipRenewalTypeHelper;

    @Override
    protected Product loadEntityById(Long id) {
        Product product = productReadDao.loadLatest(id, Locale.ENGLISH);
        product.setImages(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(product.getSnapshotId())));
        return product;
    }

    @Override
    protected boolean isValid(Product product, ValidationError validationError) {
        if (!CollectionUtils.isEmpty(product.getImages())) {
            Long supplierId = product.getInformation().getSupplier().getId();
            Membership membership = getMembershipBySupplierId(supplierId);
            Membership provisionalMembership = getProvisionalMembershipBySupplierId(supplierId);
            int totalImagesIncludingNew = product.getImages().size() + 1;

            int totalAllowedByActiveMembership = 0;
            if (membership != null) {
                totalAllowedByActiveMembership = membership.getLevel().getAdditionalProductImageCount()
                        + BASE_IMAGES_ALLOWED;
                if (totalImagesIncludingNew <= totalAllowedByActiveMembership) {
                    return true;
                }
            }

            if (totalImagesIncludingNew > totalAllowedByActiveMembership && provisionalMembership != null
                    && !membershipRenewalTypeHelper.hasProvisionalRenewalOrDowngrade(membership, provisionalMembership)) {

                int totalAllowedByProvisionalMembership = provisionalMembership.getLevel()
                        .getAdditionalProductImageCount() + BASE_IMAGES_ALLOWED;
                if (totalImagesIncludingNew <= totalAllowedByProvisionalMembership) {
                    return true;
                }
            }

            validationError.add(Product.IMAGES_SERIAL_PROPERTY, Validatable.PRODUCT_IMAGES_LENGTH);
            return false;
        }

        return true;
    }

    @Override
    protected DocumentType getDocumentType() {
        return DocumentType.PRODUCT_IMAGE;
    }

    @Override
    protected void persistNewDocumentKey(Product product, String documentName, String documentKey) {
        ProductImage productImage = new ProductImage(product.getSnapshotId(), documentKey, isPrimaryImage());
        productImageWriteDao.updateImages(product.getSnapshotId(), Lists.newArrayList(productImage));
    }

    /**
     * Determines if image is primary.
     */
    protected Boolean isPrimaryImage() {
        return false;
    }

    private Membership getMembershipBySupplierId(Long supplierId) {
        try {
            return membershipReadDao.loadBySupplierId(supplierId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Membership getProvisionalMembershipBySupplierId(Long supplierId) {
        try {
            return membershipReadDao.loadProvisionalBySupplierId(supplierId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
