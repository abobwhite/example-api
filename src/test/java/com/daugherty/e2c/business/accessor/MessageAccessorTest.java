package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.MessageCollectionHydrator;
import com.daugherty.e2c.business.accessor.filter.MessageFilter;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.jdbc.JdbcMessageDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class MessageAccessorTest {
    @Mock
    private JdbcMessageDao messageDao;
    @Mock
    private MessageCollectionHydrator messageCollectionHydrator;
    @Mock
    private QueryCriteria queryCriteria;
    @Mock
    private Hashids hashids;

    @InjectMocks
    private final MessageAccessor accessor = new MessageAccessor();

    private Message message42;

    @Before
    public void setUpFixtures() {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);

        message42 = new ProductMessage(42L, "jKNz4P4q", "subject", buyer, false, new Date(), false);
    }

    @Test
    public void findWithFilterCriteriaCallsDaoFindMethod() throws Exception {
        when(messageDao.createQueryCriteria(666L, true, 777L, "lastInteractionSentTime", true, 26, 50)).thenReturn(
                queryCriteria);
        List<Message> daoMessages = Lists.newArrayList(message42);

        when(hashids.decode("pBVQwo0b")).thenReturn(new long[] { 666L });
        when(hashids.decode("94PabyVl")).thenReturn(new long[] { 777L });
        when(messageDao.find(queryCriteria)).thenReturn(daoMessages);

        MessageFilter filter = new MessageFilter("pBVQwo0b", true, "94PabyVl", "lastInteractionSentTime", true, 26, 50);
        List<Message> messages = accessor.find(filter);

        assertThat(messages, is(daoMessages));
    }

    @Test
    public void findWithFilterCriteriaCallsDaoFindPartyMethod() throws Exception {
        when(messageDao.createQueryCriteria(666L, true, 666L, "lastInteractionSentTime", true, 26, 50)).thenReturn(
                queryCriteria);
        List<Message> daoMessages = Lists.newArrayList(message42);

        when(hashids.decode("pBVQwo0b")).thenReturn(new long[] { 666L });
        when(messageDao.findForParty(queryCriteria)).thenReturn(daoMessages);

        MessageFilter filter = new MessageFilter("pBVQwo0b", true, "pBVQwo0b", "lastInteractionSentTime", true, 26, 50);
        List<Message> messages = accessor.find(filter);

        assertThat(messages, is(daoMessages));
    }
}
