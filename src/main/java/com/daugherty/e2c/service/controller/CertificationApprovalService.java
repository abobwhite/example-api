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
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.e2c.security.Role;

/**
 * REST resource for Certification Approvals.
 */
@Controller
@RequestMapping("/certificationApprovals")
public class CertificationApprovalService {

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

    @RequestMapping(value = "/{certificationId}/submitted", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public void submitForApproval(@PathVariable Long certificationId) {
        SupplierApproval approval = supplierApprovalAccessor.load(certificationId, Locale.ENGLISH);
        approval.visit(submitForApprovalVisitor);
        supplierApprovalMutator.create(approval);
    }
    
    @RequestMapping(value = "/{certificationId}/deferred", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void deferApproval(@PathVariable Long certificationId) {
        SupplierApproval approval = supplierApprovalAccessor.load(certificationId, Locale.ENGLISH);
        approval.visit(deferApprovalVisitor);
        supplierApprovalMutator.update(approval);
    }
    
    @RequestMapping(value = "/{certificationId}/disapproved", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void disapprove(@PathVariable Long certificationId) {
        SupplierApproval approval = supplierApprovalAccessor.load(certificationId, Locale.ENGLISH);
        approval.visit(disapproveVisitor);
        supplierApprovalMutator.update(approval);
    }
    
    @RequestMapping(value = "/{certificationId}/approved", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void approve(@PathVariable Long certificationId) {
        SupplierApproval approval = supplierApprovalAccessor.load(certificationId, Locale.ENGLISH);
        approval.visit(approveVisitor);
        supplierApprovalMutator.update(approval);
    }
}

