package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.PartyFilter;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class PartyAccessorTest {
    @Mock
    private JdbcPartyDao partyDao;
    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final PartyAccessor accessor = new PartyAccessor();

    @Test
    public void findWithFilterCriteriaCallsDaoFindMethod() throws Exception {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);

        when(partyDao.createQueryCriteria("username", PartyType.BUYER.toString(), null, true, 26, 50, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        List<Party> daoParties = Lists.newArrayList(party);
        when(partyDao.getAll(queryCriteria)).thenReturn(daoParties);

        PartyFilter filter = new PartyFilter("username", PartyType.BUYER.toString(), null, true, 26, 50, Locale.ENGLISH);
        List<Party> parties = accessor.find(filter);

        assertThat(parties, is(daoParties));
        assertThat(parties.get(0), is(party));
    }

}
