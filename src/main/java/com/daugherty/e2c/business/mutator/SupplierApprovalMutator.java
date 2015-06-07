package com.daugherty.e2c.business.mutator;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.e2c.persistence.data.ProductMembershipReadDao;
import com.daugherty.e2c.persistence.data.ProductMembershipWriteDao;
import com.daugherty.e2c.persistence.data.SupplierApprovalWriteDao;

/**
 * Mutator for SupplierApproval domain objects.
 */
@Service("supplierApprovalMutator")
@Transactional
public class SupplierApprovalMutator extends BaseMutator<SupplierApproval> {

    @Inject
    private ProductMembershipReadDao productMembershipReadDao;
    @Inject
    private Accessor<Product> latestProductAccessor;
    @Inject
    private ProductMembershipWriteDao productMembershipWriteDao;
    @Inject
    private SupplierApprovalWriteDao supplierApprovalWriteDao;

    @Override
    public SupplierApproval create(SupplierApproval approval) {
        if (SupplierApproval.PRODUCT_TYPE.equals(approval.getType())) {
            try {
                productMembershipReadDao.findMembershipByProductId(approval.getId());
            } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                Product product = latestProductAccessor.load(approval.getId(), Locale.ENGLISH);
                Supplier supplier = product.getInformation().getSupplier();
                Long membershipId = supplier.getMembershipId() != null ? supplier.getMembershipId() : supplier
                        .getProvisionalMembershipId();

                if (membershipId == null) {
                    throw new IllegalArgumentException("No Membership Found for Supplier " + supplier.getId());
                } else {
                    productMembershipWriteDao.create(approval.getId(), membershipId);
                }
            }
        }

        return supplierApprovalWriteDao.insert(approval);
    }

    @Override
    public SupplierApproval update(SupplierApproval approval) {
        return supplierApprovalWriteDao.update(approval);
    }

    @Override
    public void delete(Long entityId) {
        throw new UnsupportedOperationException();
    }

}
