package com.daugherty.e2c.service.controller;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.ProductPublisher;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductTranslation;
import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.security.Role;

/**
 * REST resource for Product Approvals.
 */
@Controller
@RequestMapping("/productApprovals")
public class ProductApprovalService {

    @Inject
    private Accessor<SupplierApproval> supplierApprovalAccessor;
    @Inject
    private Mutator<SupplierApproval> supplierApprovalMutator;
    @Inject
    private ApprovalStateTransitionVisitor submitForApprovalVisitor;
    @Inject
    private ApprovalStateTransitionVisitor deferApprovalVisitor;
    @Inject
    private ApprovalStateTransitionVisitor disapproveVisitor;
    @Inject
    private ApprovalStateTransitionVisitor approveVisitor;
    @Inject
    private ApprovalStateTransitionVisitor requestTranslationVisitor;
    @Inject
    private Accessor<Product> latestProductAccessor;
    @Inject
    private Accessor<Product> approvedProductAccessor;
    @Inject
    private Mutator<ProductTranslation> productTranslationMutator;
    @Inject
    private Accessor<SupplierTranslation> supplierTranslationAccessor;
    @Inject
    private Mutator<SupplierTranslation> supplierTranslationMutator;
    @Inject
    private ProductPublisher productPublisher;

    @RequestMapping(value = "/{productId}/submitted", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public void submitForApproval(@PathVariable Long productId) {
        SupplierApproval approval = supplierApprovalAccessor.load(productId, Locale.ENGLISH);
        approval.visit(submitForApprovalVisitor);
        supplierApprovalMutator.create(approval);
    }

    @RequestMapping(value = "/{productId}/deferred", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void deferApproval(@PathVariable Long productId) {
        SupplierApproval approval = supplierApprovalAccessor.load(productId, Locale.ENGLISH);
        approval.visit(deferApprovalVisitor);
        supplierApprovalMutator.update(approval);
    }

    @RequestMapping(value = "/{productId}/disapproved", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void disapprove(@PathVariable Long productId) {
        SupplierApproval approval = supplierApprovalAccessor.load(productId, Locale.ENGLISH);
        approval.visit(disapproveVisitor);
        supplierApprovalMutator.update(approval);
    }

    @RequestMapping(value = "/{productId}/approved", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void approve(@PathVariable Long productId) {
        SupplierApproval approval = supplierApprovalAccessor.load(productId, Locale.ENGLISH);
        approval.visit(approveVisitor);
        supplierApprovalMutator.update(approval);
    }

    @RequestMapping(value = "/{productId}/translations", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR, Role.ADMIN })
    @ResponseBody
    public void requestTranslation(@PathVariable Long productId) {
        SupplierApproval approval = supplierApprovalAccessor.load(productId, Locale.ENGLISH);
        approval.visit(requestTranslationVisitor);
        supplierApprovalMutator.update(approval);
    }

    @RequestMapping(value = "/{productId}/publish", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR, Role.ADMIN })
    @ResponseBody
    public void requestPublish(@PathVariable Long productId) {
        requestTranslation(productId);

        Product approvedProduct = approvedProductAccessor.load(productId, Locale.CHINESE);
        Product latestProduct = latestProductAccessor.load(productId, Locale.CHINESE);

        ProductTranslation translation = new ProductTranslation(latestProduct.getId(), latestProduct.getSnapshotId(),
                latestProduct.getInformation().getCountry(), latestProduct.getInformation().getModelNumber(),
                latestProduct.getInformation().getPaymentTerms(), approvedProduct.getInformation().getPaymentTerms(),
                latestProduct.getInformation().getMinimumOrder(), latestProduct.getInformation()
                        .getFreightOnBoardPort(), latestProduct.getInformation().getFreightOnBoardPrice(),
                latestProduct.getInformation().getLeadTime(), latestProduct.getInformation().getName(), approvedProduct
                        .getInformation().getName(), latestProduct.getInformation().getDescription(), approvedProduct
                        .getInformation().getDescription(), latestProduct.getInformation().getSpecifications(),
                approvedProduct.getInformation().getSpecifications(), latestProduct.getMetadata().getKeywords(),
                approvedProduct.getMetadata().getKeywords(), latestProduct.getMetadata().getMetaTags(), approvedProduct
                        .getMetadata().getMetaTags());

        productTranslationMutator.update(translation);

        SupplierTranslation supplierTranslation = supplierTranslationAccessor.load(productId, Locale.ENGLISH);
        supplierTranslation.visit(approveVisitor);
        supplierTranslationMutator.update(supplierTranslation);

        productPublisher.publish(productId);
    }
}
