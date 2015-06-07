package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.ProductPublisher;
import com.daugherty.e2c.business.accessor.MembershipProductSummaryAccessor;
import com.daugherty.e2c.business.accessor.filter.ProductCategoryFilter;
import com.daugherty.e2c.business.accessor.filter.ProductFilter;
import com.daugherty.e2c.business.search.MembershipLevelPrioritizer;
import com.daugherty.e2c.business.search.ProductSearch;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.CategoryType;
import com.daugherty.e2c.domain.MembershipLevelAware;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.domain.ProductSearchResult;
import com.daugherty.e2c.service.json.JsonProduct;
import com.daugherty.e2c.service.json.JsonProductCategories;
import com.daugherty.e2c.service.json.JsonProductIds;
import com.daugherty.e2c.service.json.JsonProducts;
import com.daugherty.e2c.service.json.JsonSupplierProducts;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private Accessor<ProductCategory> productCategoryAccessor;
    @Mock
    private Accessor<Product> publishedProductAccessor;
    @Mock
    private MembershipProductSummaryAccessor membershipProductSummaryAccessor;
    @Mock
    private Mutator<Product> productMutator;

    @Mock
    private DocumentUrlFactory documentUrlFactory;
    @Mock
    private ProductPublisher publisher;
    @Mock
    private ProductSearch productSearch;
    @Mock
    private MembershipLevelPrioritizer membershipLevelPrioritizer;

    @InjectMocks
    private final ProductService service = new ProductService();

    @Captor
    private ArgumentCaptor<Filter<ProductCategory>> categoryFilterCaptor;
    @Captor
    private ArgumentCaptor<Filter<Product>> productFilterCaptor;
    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveProductCategoriesDelegatesToAccessor() {
        List<ProductCategory> categories = Lists.newArrayList(new ProductCategory(1L, "name", 0, "link", true,
                CategoryType.PRODUCT));
        when(productCategoryAccessor.find(any(Filter.class))).thenReturn(categories);

        JsonProductCategories jsonCategories = service.retrieveProductCategories(8, Locale.CHINESE);

        verify(productCategoryAccessor).find(categoryFilterCaptor.capture());
        Filter<ProductCategory> filter = categoryFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(ProductCategoryFilter.class)));
        assertThat(filter.getIntegerCriterion(ProductCategoryFilter.LEVEL2_COUNT), is(8));
        assertThat(filter.getLocale(), is(Locale.CHINESE));

        assertThat(jsonCategories, is(notNullValue()));
        assertThat(jsonCategories.size(), is(1));
        assertThat(jsonCategories.get(0).getId(), is(1L));
        assertThat(jsonCategories.get(0).getName(), is("name"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveSupplierProductsWithQueryStringArgumentsDelegatesToAccessor() {
        Product product = new Product(1L, new ProductInformation(42L, "Name", "Description", "payPal", "100", "port",
                "10", "country", "10 days", "Model ZX 234", "specs"), new ProductMetadata(null, null, "metaTage",
                "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, 32L);
        when(publishedProductAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(product));

        JsonSupplierProducts jsonProducts = service.retrieveSupplierProducts("jKNz4P4q", "Name", product
                .getApprovalStatus().getName(), true, false, "title", Boolean.TRUE, 26, 50, Locale.ENGLISH, null);

        verify(publishedProductAccessor).find(productFilterCaptor.capture());
        Filter<Product> filter = productFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(ProductFilter.class)));
        assertThat(filter.getStringCriterion(ProductFilter.PRODUCT_NAME), is(product.getInformation().getName()));
        assertThat(filter.getStringCriterion(ProductFilter.STATUS), is(product.getApprovalStatus().getName()));
        assertThat(filter.getBooleanCriterion(ProductFilter.HOT), is(Boolean.TRUE));
        assertThat(filter.getBooleanCriterion(ProductFilter.PUBLISHED), is(Boolean.FALSE));
        assertThat(filter.getSortBy(), is("title"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));

        assertThat(jsonProducts, is(notNullValue()));
        assertThat(jsonProducts.size(), is(1));
        assertThat(jsonProducts.get(0).getId(), is(product.getId()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveSupplierProductsWithEmptyArgumentsDelegatesToAccessor() {
        Product product = new Product(1L, new ProductInformation(42L, "Name", "Description", "payPal", "100", "port",
                "10", "country", "10 days", "Model ZX 234", "specs"), new ProductMetadata(null, null, "metaTage",
                "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, 32L);
        when(publishedProductAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(product));

        JsonSupplierProducts jsonProducts = service.retrieveSupplierProducts("jKNz4P4q", null, null, null, null,
                "title", Boolean.TRUE, 26, 50, Locale.ENGLISH, null);

        verify(publishedProductAccessor).find(productFilterCaptor.capture());
        Filter<Product> filter = productFilterCaptor.getValue();

        assertThat(filter, is(instanceOf(ProductFilter.class)));
        assertThat(filter.getStringCriterion(ProductFilter.PRODUCT_NAME), is(nullValue()));
        assertThat(filter.getStringCriterion(ProductFilter.STATUS), is(nullValue()));
        assertThat(filter.getBooleanCriterion(ProductFilter.HOT), is(nullValue()));
        assertThat(filter.getBooleanCriterion(ProductFilter.PUBLISHED), is(nullValue()));
        assertThat(filter.getSortBy(), is("title"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));

        assertThat(jsonProducts, is(notNullValue()));
        assertThat(jsonProducts.size(), is(1));
        assertThat(jsonProducts.get(0).getId(), is(product.getId()));
    }

    @Test
    public void retrievePublishedDelegatesToPublishedProductAccessor() {
        Product product = new Product(1L, new ProductInformation(42L, "Name", "Description", "payPal", "100", "port",
                "10", "country", "10 days", "Model ZX 234", "specs"), new ProductMetadata(null, null, "metaTage",
                "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, 32L);
        when(publishedProductAccessor.load(1L, Locale.ENGLISH)).thenReturn(product);

        JsonProduct jsonProduct = service.retrieveProduct(1L, Locale.ENGLISH, null);

        verify(publishedProductAccessor).load(1L, Locale.ENGLISH);
        assertThat(jsonProduct.getId(), is(product.getId()));
    }

    @Test
    public void retrieveProductIdsWithSearchTermsDelegatesToProductSearch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "name", "description", "keywords", 1,
                1);

        when(productSearch.search("searchTerms", "country", null, Locale.ENGLISH)).thenReturn(
                Lists.newArrayList(productSearchResult));
        when(documentUrlFactory.createDocumentUrl("link", Locale.ENGLISH)).thenReturn("documentLink");

        JsonProductIds jsonProductIds = service.retrieveProductIds(Locale.ENGLISH, "searchTerms", "country", null,
                null, null, null, null, "name", Boolean.TRUE, 26, 50, null);

        verify(productSearch).search("searchTerms", "country", null, Locale.ENGLISH);
        verifyZeroInteractions(publishedProductAccessor);

        assertThat(jsonProductIds.size(), is(1));
        assertThat(jsonProductIds.get(0), is(productSearchResult.getId()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveProductsWithoutSearchTermsOrCategoryDelegatesToPublishedProductAccessor() {
        List<ProductCategory> categories = Lists.newArrayList(new ProductCategory(1L));
        ProductImage productImage = new ProductImage("link", true);
        Product product = new Product(new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                categories);
        product.getImages().add(productImage);
        List<Product> products = Lists.newArrayList(product);

        when(publishedProductAccessor.find(any(Filter.class))).thenReturn(products);
        when(documentUrlFactory.createDocumentUrl("link", Locale.CHINESE)).thenReturn("documentLink");
        when(
                membershipLevelPrioritizer.prioritize(products, MembershipLevelAware.DEFAULT_RELEVANCE_BUCKETS,
                        MembershipLevelAware.DEFAULT_RELEVANCE_FLOOR)).thenReturn(products);

        JsonProducts jsonProducts = service.retrieveProducts(Locale.CHINESE, null, "countryParam", null, Boolean.TRUE,
                13L, 3, null, "name", Boolean.TRUE, 26, 50, null);

        verifyZeroInteractions(productSearch, productCategoryAccessor);

        verify(publishedProductAccessor).find(productFilterCaptor.capture());
        Filter<Product> filter = productFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(ProductFilter.class)));
        assertThat(filter.getStringCriterion(ProductFilter.COUNTRY), is("countryParam"));
        assertThat(((ProductFilter) filter).getProductCategoryCriterion(ProductFilter.CATEGORY), is(nullValue()));
        assertThat(filter.getBooleanCriterion(ProductFilter.HOT), is(Boolean.TRUE));
        assertThat(filter.getLongCriterion(ProductFilter.BUSINESS_TYPE), is(13L));
        assertThat(filter.getIntegerCriterion(ProductFilter.MEMBERSHIP_LEVEL), is(3));
        assertThat(filter.getSortBy(), is("name"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));
        assertThat(filter.getLocale(), is(Locale.CHINESE));

        assertThat(jsonProducts, is(notNullValue()));
        assertThat(jsonProducts.size(), is(1));
        assertThat(jsonProducts.get(0).getId(), is(product.getId()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveProductIdsWithoutSearchTermsOrCategoryDelegatesToPublishedProductAccessor() {
        List<ProductCategory> categories = Lists.newArrayList(new ProductCategory(1L));
        ProductImage productImage = new ProductImage("link", true);
        Product product = new Product(new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                categories);
        product.getImages().add(productImage);
        List<Product> products = Lists.newArrayList(product);

        when(publishedProductAccessor.find(any(Filter.class))).thenReturn(products);
        when(documentUrlFactory.createDocumentUrl("link", Locale.CHINESE)).thenReturn("documentLink");
        when(
                membershipLevelPrioritizer.prioritize(products, MembershipLevelAware.DEFAULT_RELEVANCE_BUCKETS,
                        MembershipLevelAware.DEFAULT_RELEVANCE_FLOOR)).thenReturn(products);

        JsonProductIds jsonProductIds = service.retrieveProductIds(Locale.CHINESE, null, "countryParam", null,
                Boolean.TRUE, 13L, 3, null, "name", Boolean.TRUE, 26, 50, null);

        verifyZeroInteractions(productSearch, productCategoryAccessor);

        verify(publishedProductAccessor).find(productFilterCaptor.capture());
        Filter<Product> filter = productFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(ProductFilter.class)));
        assertThat(filter.getStringCriterion(ProductFilter.COUNTRY), is("countryParam"));
        assertThat(((ProductFilter) filter).getProductCategoryCriterion(ProductFilter.CATEGORY), is(nullValue()));
        assertThat(filter.getBooleanCriterion(ProductFilter.HOT), is(Boolean.TRUE));
        assertThat(filter.getLongCriterion(ProductFilter.BUSINESS_TYPE), is(13L));
        assertThat(filter.getIntegerCriterion(ProductFilter.MEMBERSHIP_LEVEL), is(3));
        assertThat(filter.getSortBy(), is("name"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));
        assertThat(filter.getLocale(), is(Locale.CHINESE));

        assertThat(jsonProductIds, is(notNullValue()));
        assertThat(jsonProductIds.size(), is(1));
        assertThat(jsonProductIds.get(0), is(product.getId()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveProductsDelegatesToPublishedProductAccessorWithProductIds() {
        List<ProductCategory> categories = Lists.newArrayList(new ProductCategory(1L));
        ProductImage productImage = new ProductImage("link", true);
        Product product = new Product(new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                categories);
        product.getImages().add(productImage);
        List<Product> products = Lists.newArrayList(product);

        when(publishedProductAccessor.find(any(Filter.class))).thenReturn(products);
        when(documentUrlFactory.createDocumentUrl("link", Locale.CHINESE)).thenReturn("documentLink");
        when(
                membershipLevelPrioritizer.prioritize(products, MembershipLevelAware.DEFAULT_RELEVANCE_BUCKETS,
                        MembershipLevelAware.DEFAULT_RELEVANCE_FLOOR)).thenReturn(products);

        JsonProducts jsonProducts = service.retrieveProducts(Locale.CHINESE, null, null, null, null, null, null,
                "1,2,3,4", null, null, null, null, null);

        verifyZeroInteractions(productSearch, productCategoryAccessor);

        verify(publishedProductAccessor).find(productFilterCaptor.capture());
        Filter<Product> filter = productFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(ProductFilter.class)));
        assertThat(filter.getStringCriterion(ProductFilter.PRODUCT_IDS), is("1,2,3,4"));

        assertThat(jsonProducts, is(notNullValue()));
        assertThat(jsonProducts.size(), is(1));
        assertThat(jsonProducts.get(0).getId(), is(product.getId()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveProductsWithoutSearchTermsButWithCategoryDelegatesToPublishedProductAccessor() {
        List<ProductCategory> productCategories = Lists.newArrayList(new ProductCategory(1L));
        ProductImage productImage = new ProductImage("link", true);
        Product product = new Product(new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                productCategories);
        product.getImages().add(productImage);

        List<ProductCategory> allCategories = Lists.newArrayList(new ProductCategory(1L), new ProductCategory(2L));
        when(productCategoryAccessor.find(any(Filter.class))).thenReturn(allCategories);
        ArrayList<Product> products = Lists.newArrayList(product);
        when(publishedProductAccessor.find(any(Filter.class))).thenReturn(products);
        when(
                membershipLevelPrioritizer.prioritize(products, MembershipLevelAware.DEFAULT_RELEVANCE_BUCKETS,
                        MembershipLevelAware.DEFAULT_RELEVANCE_FLOOR)).thenReturn(products);
        when(documentUrlFactory.createDocumentUrl("link", Locale.CHINESE)).thenReturn("documentLink");

        JsonProducts jsonProducts = service.retrieveProducts(Locale.CHINESE, null, "countryParam", 1L, Boolean.TRUE,
                13L, 3, null, "name", Boolean.TRUE, 26, 50, null);

        verifyZeroInteractions(productSearch);

        verify(productCategoryAccessor).find(categoryFilterCaptor.capture());
        Filter<ProductCategory> categoryFilter = categoryFilterCaptor.getValue();
        assertThat(categoryFilter, is(instanceOf(ProductCategoryFilter.class)));
        assertThat(categoryFilter.getLongCriterion(ProductCategoryFilter.CATEGORY_ID), is(1L));

        verify(publishedProductAccessor).find(productFilterCaptor.capture());
        Filter<Product> filter = productFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(ProductFilter.class)));
        assertThat(filter.getStringCriterion(ProductFilter.COUNTRY), is("countryParam"));
        assertThat(((ProductFilter) filter).getProductCategoryCriterion(ProductFilter.CATEGORY),
                is(new ProductCategory(1L)));
        assertThat(filter.getBooleanCriterion(ProductFilter.HOT), is(Boolean.TRUE));
        assertThat(filter.getLongCriterion(ProductFilter.BUSINESS_TYPE), is(13L));
        assertThat(filter.getIntegerCriterion(ProductFilter.MEMBERSHIP_LEVEL), is(3));
        assertThat(filter.getSortBy(), is("name"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));
        assertThat(filter.getLocale(), is(Locale.CHINESE));

        assertThat(jsonProducts, is(notNullValue()));
        assertThat(jsonProducts.size(), is(1));
        assertThat(jsonProducts.get(0).getId(), is(product.getId()));
    }
}
