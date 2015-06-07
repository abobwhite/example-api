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
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.BuyLeadMessage;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.persistence.data.BuyLeadMessageReadDao;

@RunWith(MockitoJUnitRunner.class)
public class BuyLeadMessageAccessorTest {

    @Mock
    private BuyLeadMessageReadDao buyLeadMessageReadDao;
    @Mock
    private MessageCollectionHydrator messageCollectionHydrator;

    @InjectMocks
    private final BuyLeadMessageAccessor accessor = new BuyLeadMessageAccessor();

    private BuyLeadMessage message42;

    @Before
    public void setUpFixtures() {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);
        BuyLead buyLead = new BuyLead(1L, new Party(10L), new ProductCategory(100L), new Date(), new Date());

        message42 = new BuyLeadMessage(42L, "jKNz4P4q", "subject", buyer, false, new Date(), false, buyLead);
    }

    @Test
    public void loadCallsDaoLoadMethod() throws Exception {
        when(buyLeadMessageReadDao.load(message42.getId())).thenReturn(message42);

        BuyLeadMessage message = accessor.load(message42.getId(), Locale.ENGLISH);

        assertThat(message, is(message42));
    }

}
