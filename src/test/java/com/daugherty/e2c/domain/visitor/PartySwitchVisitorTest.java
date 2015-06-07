package com.daugherty.e2c.domain.visitor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ApprovalStatus;

@RunWith(MockitoJUnitRunner.class)
public class PartySwitchVisitorTest {

    private PartySwitchVisitor visitor = new PartySwitchVisitor();

    @Test
    public void getNewVersionAlwaysIncrements() {
        assertThat(visitor.getNewVersion(1, ApprovalStatus.APPROVED), is(2));
    }

    @Test
    public void getNewApprovalStatusIsAlwaysDraft() {
        assertThat(visitor.getNewApprovalStatus(ApprovalStatus.APPROVED), is(ApprovalStatus.DRAFT));
    }

}
