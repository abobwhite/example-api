package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.MessageCollectionHydrator;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.persistence.data.ProductMessageReadDao;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

@RunWith(MockitoJUnitRunner.class)
public class ProductMessageAccessorTest {

    private static final long MESSAGE_42_PRODUCT_ID = 242L;

    @Mock
    private ProductMessageReadDao messageReadDao;
    @Mock
    private MessageCollectionHydrator messageCollectionHydrator;

    @InjectMocks
    private final ProductMessageAccessor accessor = new ProductMessageAccessor();

    private ProductMessage message42;

    @Before
    public void setUpFixtures() {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);

        message42 = new ProductMessage(42L, "jKNz4P4q", "subject", buyer, false, new Date(), false);
    }

    @Test
    public void loadCallsDaoLoadMethod() throws Exception {
        when(messageReadDao.load(message42.getId())).thenReturn(message42);

        Multimap<Long, Long> productIdsByMessageId = ArrayListMultimap.create();
        productIdsByMessageId.put(message42.getId(), MESSAGE_42_PRODUCT_ID);
        when(messageReadDao.findProductIdListsByMessageIds(Lists.newArrayList(message42.getId()))).thenReturn(
                productIdsByMessageId);

        ProductMessage message = accessor.load(message42.getId(), Locale.ENGLISH);

        assertThat(message, is(message42));
        assertThat(message.getProductIds().size(), is(1));
        assertThat(message.getProductIds().get(0), is(MESSAGE_42_PRODUCT_ID));
    }

}
