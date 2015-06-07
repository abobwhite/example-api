package com.daugherty.e2c.service.controller;

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
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.filter.BuyLeadFilter;
import com.daugherty.e2c.business.accessor.filter.ProductCategoryFilter;
import com.daugherty.e2c.business.mapper.BuyLeadMapper;
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.service.json.JsonBuyLead;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BuyLeadServiceTest {
	
	@Mock
    private Accessor<BuyLead> buyLeadAccessor;
	@Mock
    private Accessor<ProductCategory> productCategoryAccessor;
    @Mock
    private Mutator<BuyLead> buyLeadMutator;
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    @Mock
    private BuyLeadMapper buyLeadMapper;

    @InjectMocks
    private final BuyLeadService service = new BuyLeadService();
    
    @Test
    public void createBuyLeadDelegatesToBuyerMutator() {
        BuyLead  persistedBuyLead = new BuyLead(new Party(1L), new ProductCategory(10L), new Date(), new Date());
        JsonBuyLead jsonBuyLead = new JsonBuyLead(persistedBuyLead, documentUrlFactory, Locale.ENGLISH);
        
        persistedBuyLead.setId(42L);
        when(buyLeadMapper.toNewDomainObject(jsonBuyLead)).thenReturn(persistedBuyLead);
        when(buyLeadMutator.create(buyLeadMapper.toNewDomainObject(jsonBuyLead))).thenReturn(persistedBuyLead);

        JsonBuyLead responseBuyLead = service.createBuyLead(jsonBuyLead, Locale.ENGLISH);
        assertThat(responseBuyLead.getId(), is(42L));
    }
    
    @Test
    public void findBuyLeadsDelegatesToAccessor() {
    	ProductCategory productCategory = new ProductCategory(100L);
    	
		BuyLead buyLead = new BuyLead(1L, new Party(10L), productCategory, new Date(), new Date());
    	
        when(productCategoryAccessor.find(Mockito.any(ProductCategoryFilter.class))).thenReturn(Lists.newArrayList(productCategory));
        when(buyLeadAccessor.find(Mockito.any(BuyLeadFilter.class))).thenReturn(Lists.newArrayList(buyLead));

        List<JsonBuyLead> existingBuyLeads = service.findBuyLeads("email", 1L, "province", null, false, BuyLead.EFFECTIVE_DATE_SERIAL_PROPERTY, false, 1, 250, Locale.ENGLISH);

        assertThat(existingBuyLeads.get(0).getId(), is(1L));
    }

}
