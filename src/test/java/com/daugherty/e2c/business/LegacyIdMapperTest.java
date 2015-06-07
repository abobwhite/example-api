package com.daugherty.e2c.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.data.ProductCategoryReadDao;
import com.daugherty.e2c.persistence.data.ProductReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

@RunWith(MockitoJUnitRunner.class)
public class LegacyIdMapperTest {

    @Mock
    private ProductCategoryReadDao categoryReadDao;
    @Mock
    private ProductReadDao productReadDao;
    @Mock
    private SupplierReadDao supplierReadDao;

    @InjectMocks
    private final LegacyIdMapper mapper = new LegacyIdMapper();
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void lookupCategoryIdUsesCategoryDao() throws Exception {
        when(categoryReadDao.loadCategoryIdMatchingLegacyId(666L)).thenReturn(42L);

        Long categoryId = mapper.lookupCategoryId(666L);

        assertThat(categoryId, is(42L));
        verifyZeroInteractions(productReadDao, supplierReadDao);
    }

    @Test
    public void lookupProductIdUsesProductDao() throws Exception {
        when(productReadDao.loadProductIdMatchingLegacyId(666L)).thenReturn(42L);

        Long productId = mapper.lookupProductId(666L);

        assertThat(productId, is(42L));
        verifyZeroInteractions(categoryReadDao, supplierReadDao);
    }

    @Test
    public void lookupSupplierIdUsesSupplierDao() throws Exception {
        when(supplierReadDao.loadSupplierIdMatchingLegacyId(666L)).thenReturn("jKNz4P4q");

        String publicSupplierId = mapper.lookupSupplierId(666L);

        assertThat(publicSupplierId, is("jKNz4P4q"));
        verifyZeroInteractions(categoryReadDao, productReadDao);
    }
    
    @Test
    public void lookupLegacyIdFailsAndIsFoundByDatabaseId() {
        when(supplierReadDao.loadSupplierIdMatchingLegacyId(666L)).thenThrow(new EmptyResultDataAccessException(1));
        
        Supplier supplier = new Supplier(666L, "jKNz4P4q", null, null, null, null, null, null, null, null);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        
        String publicSupplierId = mapper.lookupSupplierId(666L);
        
        assertThat(publicSupplierId, is("jKNz4P4q"));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void lookupLegacyIdFailsAndIsNotFoundByDatabaseIdThrowsLegacyNotFoundException() {
        expectedException.expect(LegacyIdNotFoundException.class);
        
        when(supplierReadDao.loadSupplierIdMatchingLegacyId(666L)).thenThrow(new EmptyResultDataAccessException(1));
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenThrow(LegacyIdNotFoundException.class);
        
        mapper.lookupSupplierId(666L);
    }

}
