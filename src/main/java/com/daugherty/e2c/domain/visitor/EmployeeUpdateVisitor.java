package com.daugherty.e2c.domain.visitor;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;

/**
 *
 */
@Component("employeeUpdateVisitor")
public class EmployeeUpdateVisitor implements ApprovalStateTransitionVisitor {

    @Override
    public int getNewVersion(int currentVersion, ApprovalStatus currentApprovalStatus) {
        return currentVersion + 1;
    }

    @Override
    public ApprovalStatus getNewApprovalStatus(ApprovalStatus currentApprovalStatus) {
        return currentApprovalStatus;
    }
}
