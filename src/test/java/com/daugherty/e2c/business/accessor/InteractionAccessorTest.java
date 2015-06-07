package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.InteractionFilter;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.InteractionReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class InteractionAccessorTest {

    private static final Company BUYER_COMPANY = new Company("buyer", null, null, null, null, null, null, null, null,
            null, null);
    private static final Party BUYER = new Party(11L, "g3Vl8VbW", null, BUYER_COMPANY, null, PartyType.BUYER, null, null);
    private static final Company SUPPLIER_COMPANY = new Company("supplier", null, null, null, null, null, null, null,
            null, null, null);
    private static final Party ENGLISH_SUPPLIER = new Party(666L, "pBVQwo0b", null, SUPPLIER_COMPANY, null, PartyType.SUPPLIER,
            null, null);

    @Mock
    private InteractionReadDao interactionReadDao;
    @Mock
    private QueryCriteria queryCriteria;
    @Mock
    private Hashids hashids;

    @InjectMocks
    private final InteractionAccessor accessor = new InteractionAccessor();

    @Test
    public void findWithFilterCriteriaCallsDaoFindMethod() throws Exception {
        when(interactionReadDao.createQueryCriteria(42L, "sentTime", true, 26, 50)).thenReturn(queryCriteria);
        List<Interaction> daoInteractions = Lists.newArrayList(new Interaction(421L, "body", BUYER, ENGLISH_SUPPLIER,
                new Date(), true));
        when(interactionReadDao.find(queryCriteria)).thenReturn(daoInteractions);
        when(hashids.decode("jKNz4P4q")).thenReturn(new long[]{42L});

        InteractionFilter filter = new InteractionFilter("jKNz4P4q", "sentTime", true, 26, 50);
        List<Interaction> messages = accessor.find(filter);

        assertThat(messages, is(daoInteractions));
    }

    @Test
    public void loadCallsDaoLoadMethod() throws Exception {
        Interaction daoInteraction = new Interaction(421L, "body", BUYER, ENGLISH_SUPPLIER, new Date(), true);
        when(interactionReadDao.load(421L)).thenReturn(daoInteraction);

        Interaction interaction = accessor.load(421L, Locale.ENGLISH);

        assertThat(interaction, is(daoInteraction));
    }

}
