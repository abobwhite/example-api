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
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.BuyerApprovalFilter;
import com.daugherty.e2c.business.mapper.BuyerApprovalMapper;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BuyerApproval;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.security.UserDeleter;
import com.daugherty.e2c.service.json.JsonBuyerApprovals;

/**
 * REST resource for Supplier Approvals.
 */
@Controller
@RequestMapping("/buyerApprovals")
public class BuyerApprovalService {

    @Inject
    private Accessor<BuyerApproval> buyerApprovalAccessor;
    @Inject
    private Mutator<BuyerApproval> buyerApprovalMutator;
    @Inject
    private ApprovalStateTransitionVisitor submitForApprovalVisitor;
    @Inject
    private ApprovalStateTransitionVisitor deferApprovalVisitor;
    @Inject
    private ApprovalStateTransitionVisitor disapproveVisitor;
    @Inject
    private ApprovalStateTransitionVisitor approveVisitor;
    @Inject
    private UserDeleter userDeleter;
    @Inject
    private BuyerApprovalMapper buyerApprovalMapper;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER_MODERATOR })
    @ResponseBody
    public JsonBuyerApprovals retrieveBuyerApprovals(@RequestParam(value = BuyerApprovalFilter.TITLE_PREFIX,
            required = false) String titlePrefix, @RequestParam(value = BuyerApprovalFilter.EMAIL_PREFIX,
            required = false) String emailPrefix,
            @RequestParam(value = BuyerApprovalFilter.STATUS, required = false) String approvalStatus, @RequestParam(
                    value = BuyerApprovalFilter.NEW_BUYER, required = false) Boolean newBuyer, @RequestParam(
                    value = BaseFilter.SORT_BY, required = false) String sortBy, @RequestParam(
                    value = BaseFilter.SORT_DESC, defaultValue = "false") Boolean sortDesc, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count) {

        BuyerApprovalFilter filter = new BuyerApprovalFilter(titlePrefix, emailPrefix,
                verifyApprovalStatusParameter(approvalStatus), newBuyer, sortBy, sortDesc, startItem, count);
        List<BuyerApproval> approvals = buyerApprovalAccessor.find(filter);

        JsonBuyerApprovals jsonApprovals = new JsonBuyerApprovals();
        for (BuyerApproval approval : approvals) {
            jsonApprovals.add(buyerApprovalMapper.fromExistinDomainObject(approval));
        }
        return jsonApprovals;
    }

    private ApprovalStatus verifyApprovalStatusParameter(String approvalStatus) {
        return StringUtils.isBlank(approvalStatus) ? null : ApprovalStatus.findByName(approvalStatus);
    }

    @RequestMapping(value = "/{buyerId}/submitted", method = RequestMethod.POST)
    @Secured({ Role.BUYER })
    @ResponseBody
    public void submitForApproval(@PathVariable String buyerId) {
        BuyerApproval approval = buyerApprovalAccessor.load(buyerId, Locale.ENGLISH);
        approval.visit(submitForApprovalVisitor);
        buyerApprovalMutator.create(approval);
    }

    @RequestMapping(value = "/{buyerId}/deferred", method = RequestMethod.POST)
    @Secured({ Role.BUYER_MODERATOR })
    @ResponseBody
    public void deferApproval(@PathVariable String buyerId) {
        BuyerApproval approval = buyerApprovalAccessor.load(buyerId, Locale.ENGLISH);
        approval.visit(deferApprovalVisitor);
        buyerApprovalMutator.update(approval);
    }

    @RequestMapping(value = "/{buyerId}/disapproved", method = RequestMethod.POST)
    @Secured({ Role.BUYER_MODERATOR })
    @ResponseBody
    public void disapprove(@PathVariable String buyerId) {
        BuyerApproval approval = buyerApprovalAccessor.load(buyerId, Locale.ENGLISH);
        approval.visit(disapproveVisitor);
        buyerApprovalMutator.update(approval);

        userDeleter.deleteDisapprovedBuyer(buyerId);
    }

    @RequestMapping(value = "/{buyerId}/approved", method = RequestMethod.POST)
    @Secured({ Role.BUYER_MODERATOR })
    @ResponseBody
    public void approve(@PathVariable String buyerId) {
        BuyerApproval approval = buyerApprovalAccessor.load(buyerId, Locale.ENGLISH);
        approval.visit(approveVisitor);
        buyerApprovalMutator.update(approval);
    }
}
