package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.BuyLeadFilter;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.persistence.data.BuyLeadReadDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BuyLeadAccessorTest {

    private final List<BusinessType> businessTypes = Lists.newArrayList(BusinessType.AGENT,
            BusinessType.DISTRIBUTOR_WHOLESALER);
    Company company = new Company("buyer", null, null, new ArrayList<BusinessType>(), null, null, null, null, null,
            null, null);
    private final BuyLead buyLead = new BuyLead(1L, new Party(10L, "Y9072Nmr", null, company, null, null, null, 500L),
            new ProductCategory(100L), new Date(), new Date());
    private final List<BuyLead> buyLeads = Lists.newArrayList(buyLead);

    @Mock
    private BuyLeadReadDao buyLeadReadDao;
    @Mock
    private PartyBusinessTypeReadDao businessTypeReadDao;

    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final BuyLeadAccessor accessor = new BuyLeadAccessor();

    @Test
    public void findWithoutFilterCriteriaCallsDaoFindAllBuyLeadsMethod() throws Exception {
        BuyLeadFilter emptyFilter = new BuyLeadFilter(null, null, null, null, null, null, null, null, null);

        ArrayListMultimap<Long, BusinessType> businessTypeMultimap = ArrayListMultimap.create();
        businessTypeMultimap.putAll(500L, businessTypes);

        ArrayListMultimap<Long, Long> respondingSupplierBuyLeadMap = ArrayListMultimap.create();
        respondingSupplierBuyLeadMap.putAll(1L, Lists.newArrayList(22L));

        when(businessTypeReadDao.findBySnapshotIds(Lists.newArrayList(500L))).thenReturn(businessTypeMultimap);
        when(buyLeadReadDao.findSuppliersThatRespondedToBuyLeads(Lists.newArrayList(1L))).thenReturn(
                respondingSupplierBuyLeadMap);
        when(buyLeadReadDao.createSortingAndPaginationCriteria(null, Boolean.FALSE, 1, 250, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(buyLeadReadDao.findBuyLeads(queryCriteria)).thenReturn(buyLeads);

        assertThat(accessor.find(emptyFilter), is(buyLeads));
        assertTrue(buyLead.getRespondingSuppliers().contains(22L));
        verify(buyLeadReadDao).findBuyLeads(queryCriteria);

    }

    @Test
    public void findWithFilterCriteriaCallsDaoFindBuyLeadMethod() throws Exception {
        BuyLeadFilter filter = new BuyLeadFilter("email", null, null, null, false, null, null, null, null);

        ArrayListMultimap<Long, BusinessType> businessTypeMultimap = ArrayListMultimap.create();
        businessTypeMultimap.putAll(500L, businessTypes);

        ArrayListMultimap<Long, Long> respondingSupplierBuyLeadMap = ArrayListMultimap.create();
        respondingSupplierBuyLeadMap.putAll(1L, Lists.newArrayList(22L));

        when(businessTypeReadDao.findBySnapshotIds(Lists.newArrayList(500L))).thenReturn(businessTypeMultimap);
        when(buyLeadReadDao.findSuppliersThatRespondedToBuyLeads(Lists.newArrayList(1L))).thenReturn(
                respondingSupplierBuyLeadMap);
        when(buyLeadReadDao.createBuyLeadQueryCriteria("email", null, null, null, false, null, false, 1, 250))
                .thenReturn(queryCriteria);
        when(buyLeadReadDao.findBuyLeads(queryCriteria)).thenReturn(buyLeads);

        assertThat(accessor.find(filter), is(buyLeads));
        assertTrue(buyLead.getRespondingSuppliers().contains(22L));
    }

}
