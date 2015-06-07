package com.daugherty.e2c.service.controller;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.ProfilePublisher;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.SupplierApprovalFilter;
import com.daugherty.e2c.business.mapper.SupplierApprovalMapper;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.ProfileTranslation;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.security.UserDeleter;
import com.daugherty.e2c.service.json.JsonSupplierApprovals;

/**
 * REST resource for Supplier Approvals.
 */
@Controller
@RequestMapping("/supplierApprovals")
public class SupplierApprovalService {

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
    private UserDeleter userDeleter;
    @Inject
    private SupplierApprovalMapper supplierApprovalMapper;
    @Inject
    private Accessor<Supplier> approvedSupplierAccessor;
    @Inject
    private Accessor<Supplier> latestSupplierAccessor;
    @Inject
    private Mutator<SupplierTranslation> supplierTranslationMutator;
    @Inject
    private Mutator<ProfileTranslation> profileTranslationMutator;
    @Inject
    private Accessor<SupplierTranslation> supplierTranslationAccessor;
    @Inject
    private ProfilePublisher profilePublisher;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public JsonSupplierApprovals retrieveSupplierApprovals(@RequestParam(value = SupplierApprovalFilter.TITLE_PREFIX,
            required = false) String titlePrefix, @RequestParam(value = SupplierApprovalFilter.EMAIL_PREFIX,
            required = false) String emailPrefix,
            @RequestParam(value = SupplierApprovalFilter.TYPE, required = false) String approvalType, @RequestParam(
                    value = SupplierApprovalFilter.STATUS, required = false) String approvalStatus, @RequestParam(
                    value = SupplierApprovalFilter.NEW_SUPPLIER, required = false) Boolean newSupplier, @RequestParam(
                    value = SupplierApprovalFilter.PAID_SUPPLIER, required = false) Boolean paidSupplier,
            @RequestParam(value = BaseFilter.SORT_BY, required = false) String sortBy, @RequestParam(
                    value = BaseFilter.SORT_DESC, defaultValue = "false") Boolean sortDesc, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count) {

        SupplierApprovalFilter filter = new SupplierApprovalFilter(titlePrefix, emailPrefix, approvalType,
                verifyApprovalStatusParameter(approvalStatus), newSupplier, paidSupplier, sortBy, sortDesc, startItem,
                count);
        List<SupplierApproval> approvals = supplierApprovalAccessor.find(filter);

        JsonSupplierApprovals jsonApprovals = new JsonSupplierApprovals();
        for (SupplierApproval approval : approvals) {
            jsonApprovals.add(supplierApprovalMapper.fromExistingDomainObject(approval));
        }
        return jsonApprovals;
    }

    private ApprovalStatus verifyApprovalStatusParameter(String approvalStatus) {
        return StringUtils.isBlank(approvalStatus) ? null : ApprovalStatus.findByName(approvalStatus);
    }

    @RequestMapping(value = "/{supplierId}/submitted", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public void submitForApproval(@PathVariable String supplierId) {
        SupplierApproval approval = supplierApprovalAccessor.load(supplierId, Locale.ENGLISH);
        approval.visit(submitForApprovalVisitor);
        supplierApprovalMutator.create(approval);
    }

    @RequestMapping(value = "/{supplierId}/deferred", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void deferApproval(@PathVariable String supplierId) {
        SupplierApproval approval = supplierApprovalAccessor.load(supplierId, Locale.ENGLISH);
        approval.visit(deferApprovalVisitor);
        supplierApprovalMutator.update(approval);
    }

    @RequestMapping(value = "/{supplierId}/disapproved", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void disapprove(@PathVariable String supplierId) {
        SupplierApproval approval = supplierApprovalAccessor.load(supplierId, Locale.ENGLISH);
        approval.visit(disapproveVisitor);
        supplierApprovalMutator.update(approval);

        if (SupplierApproval.PROFILE_TYPE.equals(approval.getType())) {
            userDeleter.deleteDisapprovedSupplier(supplierId);
        }
    }

    @RequestMapping(value = "/{supplierId}/approved", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void approve(@PathVariable String supplierId) {
        SupplierApproval approval = supplierApprovalAccessor.load(supplierId, Locale.ENGLISH);
        approval.visit(approveVisitor);
        supplierApprovalMutator.update(approval);
    }

    @RequestMapping(value = "/{supplierId}/translations", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR, Role.ADMIN })
    @ResponseBody
    public void requestTranslation(@PathVariable String supplierId) {
        SupplierApproval approval = supplierApprovalAccessor.load(supplierId, Locale.ENGLISH);
        approval.visit(requestTranslationVisitor);
        supplierApprovalMutator.update(approval);
    }

    @RequestMapping(value = "/{supplierId}/publish", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR, Role.ADMIN })
    @ResponseBody
    public void requestPublish(@PathVariable String supplierId) {
        requestTranslation(supplierId);

        Supplier approvedSupplier = approvedSupplierAccessor.load(supplierId, Locale.CHINESE);
        Supplier latestSupplier = latestSupplierAccessor.load(supplierId, Locale.CHINESE);

        ProfileTranslation translation = new ProfileTranslation(latestSupplier.getId(), latestSupplier.getPublicId(),
                latestSupplier.getSnapshotId(), latestSupplier.getCompany().getDescription(), approvedSupplier
                        .getCompany().getDescription());

        profileTranslationMutator.update(translation);

        SupplierTranslation supplierTranslation = supplierTranslationAccessor.load(supplierId, Locale.ENGLISH);
        supplierTranslation.visit(approveVisitor);
        supplierTranslationMutator.update(supplierTranslation);

        profilePublisher.publish(supplierId);
    }
}
