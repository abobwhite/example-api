package com.daugherty.e2c.business.mutator;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.mail.sender.BuyLeadAnonymousPartyMessageMailSender;
import com.daugherty.e2c.persistence.data.BuyLeadWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class BuyLeadMutatorTest {
    @Mock
    private Validator<BuyLead> buyLeadValidator;
    @Mock
    private BuyLeadWriteDao buyLeadWriteDao;
    @Mock
    private BuyLeadAnonymousPartyMessageMailSender mailSender;

    @InjectMocks
    private BuyLeadMutator buyLeadMutator = new BuyLeadMutator();

    @Test
    public void insertWhenBuyLeadIsValid() {
        BuyLead buyLead = new BuyLead(new Party(1L), new ProductCategory(10L), new Date(), new Date());

        buyLeadMutator.create(buyLead);

        verify(buyLeadWriteDao).insert(buyLead);
        verifyZeroInteractions(mailSender);
    }

    @Test
    public void insertWhenBuyLeadPartyIsAnonymousSendsEmail() throws NonEssentialMailFailureException {
        Anonymous anonymousParty = new Anonymous(1L, "vXP9l0Or", null, null, ApprovalStatus.UNPROFILED, 1, 1L);

        BuyLead buyLead = new BuyLead(anonymousParty, new ProductCategory(10L), new Date(), new Date());

        buyLeadMutator.create(buyLead);

        verify(buyLeadWriteDao).insert(buyLead);
        verify(mailSender).send(buyLead);
    }
}
