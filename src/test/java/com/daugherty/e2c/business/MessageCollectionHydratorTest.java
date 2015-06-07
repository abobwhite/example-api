package com.daugherty.e2c.business;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageAttachment;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.persistence.data.MessageAttachmentReadDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class MessageCollectionHydratorTest {

    @Mock
    private MessageAttachmentReadDao messageAttachmentReadDao;

    @InjectMocks
    private MessageCollectionHydrator messageCollectionHydrator = new MessageCollectionHydrator();

    private Message message;

    @Before
    public void setUpFixtures() {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);

        message = new ProductMessage(42L, "jKNz4P4q", "subject", buyer, false, new Date(), false);
    }

    @Test
    public void hydrateMessage() {
        MessageAttachment messageAttachement = new MessageAttachment(1L, 42L, "name", "link");

        when(messageAttachmentReadDao.loadByMessageIds(Mockito.anyList())).thenReturn(
                Lists.newArrayList(messageAttachement));

        messageCollectionHydrator.hydrateAll(Lists.newArrayList(message));

        assertTrue(message.getAttachements().contains(messageAttachement));

    }
}
