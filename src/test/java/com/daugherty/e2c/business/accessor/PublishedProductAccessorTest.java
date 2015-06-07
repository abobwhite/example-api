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
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.persistence.data.ProductReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class PublishedProductAccessorTest {

    private static final long SNAPSHOT_ID = 32L;
    private final Product product = new Product(1L, new ProductInformation(42L, "Name", "Description", "payPal", "100",
            "port", "10", "country", "10 days", "Model ZX 234", "specs"), new ProductMetadata(null, null, "metaTage",
            "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, SNAPSHOT_ID);

    private List<Product> products;

    @Mock
    private ProductReadDao productReadDao;
    @Mock
    private ProductCollectionHydrator productCollectionHydrator;

    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final PublishedProductAccessor accessor = new PublishedProductAccessor();

    @Before
    public void setup() {
        products = Lists.newArrayList(product);
    }

    @Test
    public void findWithoutFilterCriteriaCallsDaoGetAllPublishedMethod() throws Exception {
        ProductFilter emptyFilter = new ProductFilter(null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null);
        when(productReadDao.createSortingAndPaginationCriteria(null, Boolean.FALSE, 1, 250, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(productReadDao.findPublished(queryCriteria)).thenReturn(products);
        when(productCollectionHydrator.hydrateApproved(products, Locale.ENGLISH)).thenReturn(products);

        List<Product> products = accessor.find(emptyFilter);

        verify(productReadDao).findPublished(queryCriteria);
        assertThat(products, is(products));
    }

    @Test
    public void findWithFilterCriteriaIncludingCategoryIdListCallsDaoFindPublishedMethod() throws Exception {
        ProductCategory category = new ProductCategory(242L);
        ProductCategory firstChildCategory = new ProductCategory(586L);
        firstChildCategory.addChild(new ProductCategory(666L));
        category.addChild(firstChildCategory);
        category.addChild(new ProductCategory(999L));

        ProductFilter filter = new ProductFilter("Country", category, true, 13L, 3, null, null, null, null, null,
                "name", true, 26, 50, Locale.ENGLISH);
        when(
                productReadDao.createPublishedQueryCriteria(null, "Country",
                        Lists.newArrayList(242L, 586L, 666L, 999L), true, 13L, 3, "name", true, 26, 50, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(productReadDao.findPublished(queryCriteria)).thenReturn(products);
        when(productCollectionHydrator.hydrateApproved(products, Locale.ENGLISH)).thenReturn(products);

        List<Product> products = accessor.find(filter);

        assertThat(products, is(products));
    }

    @Test
    public void findWithFilterCriteriaExcludingCategoryIdListCallsDaoFindPublishedMethod() throws Exception {
        ProductFilter filter = new ProductFilter("Country", null, true, 13L, 3, null, null, null, null, null, "name",
                true, 26, 50, Locale.ENGLISH);
        when(
                productReadDao.createPublishedQueryCriteria(null, "Country", null, true, 13L, 3, "name", true, 26, 50,
                        Locale.ENGLISH)).thenReturn(queryCriteria);
        when(productReadDao.findPublished(queryCriteria)).thenReturn(products);
        when(productCollectionHydrator.hydrateApproved(products, Locale.ENGLISH)).thenReturn(products);

        List<Product> products = accessor.find(filter);

        assertThat(products, is(products));
    }

    @Test
    public void findWithFilterCriteriaProductIdsCallsDaoLoadPublishedByProductIdsMethod() throws Exception {
        ProductFilter filter = new ProductFilter("Country", null, true, 13L, 3, "1,2,3,4", null, null, null, null,
                "name", true, 26, 50, Locale.ENGLISH);

        when(productReadDao.loadPublishedByProductIds(Lists.newArrayList(1L, 2L, 3L, 4L), Locale.ENGLISH)).thenReturn(
                products);
        when(productCollectionHydrator.hydrateApproved(products, Locale.ENGLISH)).thenReturn(products);

        List<Product> products = accessor.find(filter);

        assertThat(products, is(products));
    }

    @Test
    public void loadDaoWithProductIdMethod() throws Exception {
        when(productReadDao.loadPublished(1L, Locale.ENGLISH)).thenReturn(product);
        when(productCollectionHydrator.hydrateApproved(product, Locale.ENGLISH)).thenReturn(product);

        Product persistedProduct = accessor.load(1L, Locale.ENGLISH);

        assertThat(persistedProduct, is(product));
    }
}
