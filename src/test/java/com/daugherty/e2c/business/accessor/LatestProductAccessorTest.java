package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.ProductCollectionHydrator;
import com.daugherty.e2c.business.accessor.filter.ProductFilter;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.ProductReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class LatestProductAccessorTest {

    private static final long SNAPSHOT_ID = 32L;
    private final Product product = new Product(1L, new ProductInformation(42L, "Name", "Description", "payPal", "100",
            "port", "10", "country", "10 days", "Model ZX 234", "specs"), new ProductMetadata(null, null, "metaTage",
            "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, SNAPSHOT_ID);

    private List<Product> products;

    @Mock
    private ProductReadDao productReadDao;
    @Mock
    private Hashids hashIds;
    @Mock
    private ProductCollectionHydrator productCollectionHydrator;
    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final LatestProductAccessor accessor = new LatestProductAccessor();

    @Before
    public void setup() {
        products = Lists.newArrayList(product);
    }

    @Test
    public void findWithoutFilterCriteriaCallsDaoGetAllMethod() throws Exception {
        ProductFilter emptyFilter = new ProductFilter(null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null);
        when(productReadDao.createSortingAndPaginationCriteria(null, Boolean.FALSE, 1, 250, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(productReadDao.findLatest(queryCriteria)).thenReturn(products);
        when(productCollectionHydrator.hydrateLatest(products, Locale.ENGLISH)).thenReturn(products);

        List<Product> products = accessor.find(emptyFilter);

        verify(productReadDao).findLatest(queryCriteria);
        assertThat(products, is(products));
    }

    @Test
    public void findWithFilterCriteriaCallsDaoFindMethod() throws Exception {
        ProductFilter filter = new ProductFilter(null, null, true, null, null, null, "jKNz4P4q", "Name",
                ApprovalStatus.PENDING_APPROVAL, false, "title", true, 26, 50, Locale.ENGLISH);

        when(hashIds.decode("jKNz4P4q")).thenReturn(new long[] { 1L });

        when(
                productReadDao.createLatestQueryCriteria(1L, "Name", ApprovalStatus.PENDING_APPROVAL.getName(), true,
                        false, "title", true, 26, 50, Locale.ENGLISH)).thenReturn(queryCriteria);
        when(productReadDao.findLatest(queryCriteria)).thenReturn(products);
        when(productCollectionHydrator.hydrateLatest(products, Locale.ENGLISH)).thenReturn(products);

        List<Product> products = accessor.find(filter);

        assertThat(products, is(products));
    }

    @Test
    public void loadDaoWithProductIdMethod() throws Exception {
        when(productReadDao.loadLatest(SNAPSHOT_ID, Locale.ENGLISH)).thenReturn(product);
        when(productCollectionHydrator.hydrateLatest(product, Locale.ENGLISH)).thenReturn(product);

        Product persistedProduct = accessor.load(SNAPSHOT_ID, Locale.ENGLISH);

        assertThat(persistedProduct, is(product));
    }
}
