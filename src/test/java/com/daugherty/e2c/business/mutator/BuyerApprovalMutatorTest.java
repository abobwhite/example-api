package com.daugherty.e2c.business.mutator;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BuyerApproval;
import com.daugherty.e2c.mail.sender.RegistrationApprovedMailSender;
import com.daugherty.e2c.mail.sender.RegistrationCompletionMailSender;
import com.daugherty.e2c.persistence.data.BuyerApprovalWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class BuyerApprovalMutatorTest {

    @Mock
    private BuyerApprovalWriteDao buyerApprovalWriteDao;
    @Mock
    private RegistrationCompletionMailSender registrationCompletionMailSender;
    @Mock
    private RegistrationApprovedMailSender approvalMailSender;

    @InjectMocks
    private final BuyerApprovalMutator buyerApprovalMutator = new BuyerApprovalMutator();

    @Test
    public void createUpdatesDatabaseAndSendsEmailIfSubmittingApprovalForFirstVersion() {
        BuyerApproval approval = new BuyerApproval(21L, "Y40dgNWM", "Arthur", "Dent", "East India Tea Company", "email",
                ApprovalStatus.PENDING_APPROVAL, 1, 2101L, "apdent", new Date());
        when(buyerApprovalWriteDao.insert(approval)).thenReturn(approval);

        BuyerApproval createdApproval = buyerApprovalMutator.create(approval);

        assertThat(createdApproval, is(approval));
        verify(buyerApprovalWriteDao).insert(approval);
        verify(registrationCompletionMailSender).sendToBuyer(approval.getEmailWithPersonalName());
        verifyZeroInteractions(approvalMailSender);
    }

    @Test
    public void createUpdatesDatabaseOnlyIfSubmittingApprovalForNotTheFirstVersion() {
        BuyerApproval approval = new BuyerApproval(21L, "Y40dgNWM", "Arthur", "Dent", "East India Tea Company", "email",
                ApprovalStatus.PENDING_APPROVAL, 2, 2101L, "apdent", new Date());
        when(buyerApprovalWriteDao.insert(approval)).thenReturn(approval);

        BuyerApproval createdApproval = buyerApprovalMutator.create(approval);

        assertThat(createdApproval, is(approval));
        verify(buyerApprovalWriteDao).insert(approval);
        verifyZeroInteractions(registrationCompletionMailSender, approvalMailSender);
    }

    @Test
    public void updateUpdatesDatabaseAndSendsEmailIfStatusApprovedForFirstVersion() throws Exception {
        BuyerApproval approval = new BuyerApproval(21L, "Y40dgNWM", "Arthur", "Dent", "East India Tea Company", "email",
                ApprovalStatus.APPROVED, 1, 2101L, "apdent", new Date());

        when(buyerApprovalWriteDao.update(approval)).thenReturn(approval);

        BuyerApproval mutatedApproval = buyerApprovalMutator.update(approval);

        assertThat(mutatedApproval, is(notNullValue()));
        verify(buyerApprovalWriteDao).update(approval);
        verify(approvalMailSender).sendToBuyer(approval.getEmailWithPersonalName());
        verifyZeroInteractions(registrationCompletionMailSender);
    }

    @Test
    public void updateUpdatesDatabaseOnlyIfStatusApprovedForNotTheFirstVersion() throws Exception {
        BuyerApproval approval = new BuyerApproval(21L, "Y40dgNWM", "Arthur", "Dent", "East India Tea Company", "email",
                ApprovalStatus.APPROVED, 2, 2101L, "apdent", new Date());

        when(buyerApprovalWriteDao.update(approval)).thenReturn(approval);

        BuyerApproval mutatedApproval = buyerApprovalMutator.update(approval);

        assertThat(mutatedApproval, is(notNullValue()));
        verify(buyerApprovalWriteDao).update(approval);
        verifyZeroInteractions(approvalMailSender, registrationCompletionMailSender);
    }

    @Test
    public void updateUpdatesDatabaseOnlyIfStatusNotApproved() throws Exception {
        BuyerApproval approval = new BuyerApproval(21L, "Y40dgNWM", "Arthur", "Dent", "East India Tea Company", "email",
                ApprovalStatus.WAITING_FOR_INFORMATION, 1, 2101L, "apdent", new Date());

        when(buyerApprovalWriteDao.update(approval)).thenReturn(approval);

        BuyerApproval mutatedApproval = buyerApprovalMutator.update(approval);

        assertThat(mutatedApproval, is(notNullValue()));
        verify(buyerApprovalWriteDao).update(approval);
        verifyZeroInteractions(approvalMailSender, registrationCompletionMailSender);
    }

}
