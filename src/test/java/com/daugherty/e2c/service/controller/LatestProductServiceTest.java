package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.ProductPublisher;
import com.daugherty.e2c.business.Uploader;
import com.daugherty.e2c.business.UploaderException;
import com.daugherty.e2c.business.accessor.MembershipProductSummaryAccessor;
import com.daugherty.e2c.business.accessor.filter.ProductFilter;
import com.daugherty.e2c.business.mapper.ProductMapper;
import com.daugherty.e2c.business.search.MembershipLevelPrioritizer;
import com.daugherty.e2c.business.search.ProductSearch;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.MembershipProductSummary;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.service.json.JsonLatestProduct;
import com.daugherty.e2c.service.json.JsonMembershipProductSummary;
import com.daugherty.e2c.service.json.JsonSupplier;
import com.daugherty.e2c.service.json.JsonSupplierLatestProducts;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class LatestProductServiceTest {

    @Mock
    private Accessor<ProductCategory> productCategoryAccessor;
    @Mock
    private Accessor<Product> latestProductAccessor;
    @Mock
    private Accessor<Product> publishedProductAccessor;
    @Mock
    private MembershipProductSummaryAccessor membershipProductSummaryAccessor;
    @Mock
    private Mutator<Product> productMutator;
    @Mock
    private Uploader productImageUploader;
    @Mock
    private Uploader primaryProductImageUploader;
    @Mock
    private MultipartHttpServletRequest multipartHttpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    @Mock
    private ProductPublisher publisher;
    @Mock
    private ProductSearch productSearch;
    @Mock
    private MembershipLevelPrioritizer membershipLevelPrioritizer;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private final LatestProductService service = new LatestProductService();

    @Captor
    private ArgumentCaptor<Filter<ProductCategory>> categoryFilterCaptor;
    @Captor
    private ArgumentCaptor<Filter<Product>> productFilterCaptor;
    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        Iterator<String> filenames = mock(Iterator.class);
        when(filenames.next()).thenReturn("filename");
        when(multipartHttpServletRequest.getFileNames()).thenReturn(filenames);
        when(multipartHttpServletRequest.getFile("filename")).thenReturn(multipartFile);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveSupplierProductsWithQueryStringArgumentsDelegatesToAccessor() {
        Product product = new Product(1L, new ProductInformation(42L, "Name", "Description", "payPal", "100", "port",
                "10", "country", "10 days", "Model ZX 234", "specs"), new ProductMetadata(null, null, "metaTage",
                "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, 32L);
        when(latestProductAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(product));

        JsonSupplierLatestProducts jsonProducts = service.retrieveSupplierProducts("jKNz4P4q", "Name", product
                .getApprovalStatus().getName(), true, false, "title", Boolean.TRUE, 26, 50, Locale.ENGLISH, null);

        verify(latestProductAccessor).find(productFilterCaptor.capture());
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
        when(latestProductAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(product));

        JsonSupplierLatestProducts jsonProducts = service.retrieveSupplierProducts("jKNz4P4q", null, null, null, null,
                "title", Boolean.TRUE, 26, 50, Locale.ENGLISH, null);

        verify(latestProductAccessor).find(productFilterCaptor.capture());
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
    public void retrieveLatestDelegatesToLatestProductAccessor() {
        Product product = new Product(1L, new ProductInformation(42L, "Name", "Description", "payPal", "100", "port",
                "10", "country", "10 days", "Model ZX 234", "specs"), new ProductMetadata(null, null, "metaTage",
                "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, 32L);
        when(latestProductAccessor.load(1L, Locale.ENGLISH)).thenReturn(product);

        JsonLatestProduct jsonProduct = service.retrieveProduct(1L, Locale.ENGLISH, null);

        verify(latestProductAccessor).load(1L, Locale.ENGLISH);

        assertThat(jsonProduct.getId(), is(product.getId()));
    }

    @Test
    public void retrieveSupplierProductSummary() {
        MembershipProductSummary productSummary = new MembershipProductSummary(1L, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 9);
        when(membershipProductSummaryAccessor.load(1L, Locale.ENGLISH)).thenReturn(productSummary);

        JsonMembershipProductSummary jsonProductSummary = service.retrieveMembershipProductSummary(1L);

        verify(membershipProductSummaryAccessor).load(1L, Locale.ENGLISH);

        assertThat(jsonProductSummary.getId(), is(productSummary.getId()));
        assertThat(jsonProductSummary.getPublished(), is(productSummary.getPublished()));
        assertThat(jsonProductSummary.getPendingApproval(), is(productSummary.getPendingApproval()));
        assertThat(jsonProductSummary.getPendingTranslation(), is(productSummary.getPendingTranslation()));
        assertThat(jsonProductSummary.getWaitingForInformation(), is(productSummary.getWaitingForInformation()));
        assertThat(jsonProductSummary.getUnpublished(), is(productSummary.getUnpublished()));
        assertThat(jsonProductSummary.getHotProducts(), is(productSummary.getHotProducts()));
        assertThat(jsonProductSummary.getProductsRemaining(), is(productSummary.getProductsRemaining()));
        assertThat(jsonProductSummary.getApproved(), is(productSummary.getApproved()));
        assertThat(jsonProductSummary.getDraft(), is(productSummary.getDraft()));
    }

    @Test
    public void createProductDelegatesToMutator() throws Exception {
        JsonLatestProduct requestProduct = new JsonLatestProduct();
        JsonSupplier supplier = new JsonSupplier();
        supplier.setId("jKNzKB04");
        requestProduct.setSupplier(supplier);
        requestProduct.setName("name");
        requestProduct.setDescription("description");
        requestProduct.setMinimumOrder("mimimumOrder");
        requestProduct.setCountry("country");

        Product createdProduct = new Product(42L, new ProductInformation(null, null, null, null, null, null, null,
                null, null, null, null), new ProductMetadata(null, null, null, null, null, null, null, new Date()),
                null, ApprovalStatus.DRAFT, 1, null);
        when(productMapper.toNewDomainObject(requestProduct)).thenReturn(createdProduct);
        when(productMutator.create(productMapper.toNewDomainObject(requestProduct))).thenReturn(createdProduct);

        JsonLatestProduct responseProduct = service.createProduct(requestProduct, null, Locale.ENGLISH);

        assertThat(responseProduct, is(notNullValue()));
        assertThat(responseProduct.getId(), is(42L));
    }

    @Test(expected = UploaderException.class)
    public void addImageThrowsUploaderExceptionWhenGettingInputStreamFails() throws IOException {
        when(multipartFile.getInputStream()).thenThrow(new IOException());

        service.addImage(1L, false, multipartHttpServletRequest, httpServletResponse, Locale.ENGLISH);

        fail("You should have thrown an IOException");
    }

    @Test
    public void addImageReturnJsonDocument() throws IOException {
        InputStream inputStream = mock(InputStream.class);

        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getContentType()).thenReturn("content type");
        when(productImageUploader.uploadDocument(1L, "content type", inputStream)).thenReturn("document");
        when(documentUrlFactory.createDocumentUrl("document", Locale.ENGLISH)).thenReturn("documentUrl");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        service.addImage(1L, false, multipartHttpServletRequest, httpServletResponse, Locale.ENGLISH);

        verify(printWriter).write("{\"documentUrl\":{\"url\":\"documentUrl\"}}");
    }

    @Test
    public void updateProductDelegatesToMutator() throws Exception {
        JsonSupplier supplier = new JsonSupplier();
        supplier.setId("jKNzKB04");

        JsonLatestProduct requestProduct = new JsonLatestProduct();
        requestProduct.setId(42L);
        requestProduct.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        requestProduct.setSupplier(supplier);
        requestProduct.setName("name");
        requestProduct.setDescription("description");
        requestProduct.setMinimumOrder("mimimumOrder");
        requestProduct.setCountry("country");
        requestProduct.setPublicationDate(new Date());

        Product productToUpdate = new Product(42L, new ProductInformation(null, null, null, null, null, null, null,
                null, null, null, null), new ProductMetadata(null, null, null, null, null, null, null, new Date()),
                null, ApprovalStatus.DRAFT, 1, null);
        when(productMapper.toExistingDomainObject(42L, requestProduct, Locale.ENGLISH)).thenReturn(productToUpdate);
        when(productMutator.update(productMapper.toExistingDomainObject(42L, requestProduct, Locale.ENGLISH)))
                .thenReturn(productToUpdate);

        JsonLatestProduct responseProduct = service.updateProduct(requestProduct.getId(), requestProduct, null,
                Locale.ENGLISH);

        assertThat(responseProduct, is(notNullValue()));
        assertThat(responseProduct.getId(), is(42L));
    }

    @Test
    public void publishProductDelegatesToPublisher() throws Exception {
        service.publishProduct(42L);

        verify(publisher).publish(42L);
    }

    @Test
    public void unpublishProductDelegatesToPublisher() throws Exception {
        service.unpublishProduct(42L);

        verify(publisher).unpublish(42L);
    }

    @Test
    public void heatProductDelegatesToPublisher() throws Exception {
        service.heatProduct(42L);

        verify(publisher).heat(42L);
    }

    @Test
    public void coolProductDelegatesToPublisher() throws Exception {
        service.coolProduct(42L);

        verify(publisher).cool(42L);
    }

    @Test
    public void addHotProductOverrideDelegatesToPublisher() throws Exception {
        service.addHotProductOverride(42L);

        verify(publisher).addHotProductOverride(42L);
    }

    @Test
    public void removeHotProductOverrideDelegatesToPublisher() throws Exception {
        service.removeHotProductOverride(42L);

        verify(publisher).removeHotProductOverride(42L);
    }
}
