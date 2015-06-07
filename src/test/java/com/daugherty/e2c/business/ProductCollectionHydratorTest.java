package com.daugherty.e2c.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.domain.ProductTranslation;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.e2c.persistence.data.ProductCategoryReadDao;
import com.daugherty.e2c.persistence.data.ProductImageReadDao;
import com.daugherty.e2c.persistence.data.ProductTranslationReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class ProductCollectionHydratorTest {

    private static final long SNAPSHOT_ID = 32L;
    private static final ProductTranslation PRODUCT_TRANSLATION = new ProductTranslation(586L, SNAPSHOT_ID,
            "translatedPaymentTerms", "translatedProductName", "translatedProductDescription",
            "translatedKeySpecifications", "translatedKeywords", "translatedMetaTags");
    private static final ProductImage PRODUCT_IMAGE = new ProductImage(SNAPSHOT_ID, "productImageLink", true);
    private static final ProductCategory CATEGORY_1 = new ProductCategory(1L, "CATEGORY_1", SNAPSHOT_ID, "link1", null,
            null);
    private static final ProductCategory CATEGORY_2 = new ProductCategory(2L, "CATEGORY_2", SNAPSHOT_ID, "link2", null,
            null);

    private static final Contact CONTACT = new Contact("title", null, "first_name", "last_name", "country", "province",
            "email_address", "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123",
            Language.ENGLISH, new Date());
    private static final Company COMPANY = new Company("english_name", "chinese_name", "description", null, "1-10",
            "website", 2013, "200-300", "100-150", "logo_ref_id", "video_ref_id");
    private static final Supplier SUPPLIER = new Supplier(42L, "jKNz4P4q", CONTACT, COMPANY, ApprovalStatus.DRAFT, 1,
            44L, "import_license_ref_id", null, null);

    private Product product;
    private List<Product> products;
    private List<ProductImage> productImages;
    private List<ProductCategory> productCategories;
    private List<Supplier> suppliers;
    private ArrayListMultimap<Long, BusinessType> businessTypeBySnapshotId;
    private Map<Long, ProductTranslation> productTranslationsBySnapshotId;

    @Mock
    private ProductImageReadDao productImageReadDao;
    @Mock
    private ProductCategoryReadDao productCategoryReadDao;
    @Mock
    private SupplierReadDao supplierReadDao;
    @Mock
    private PartyBusinessTypeReadDao businessTypeReadDao;
    @Mock
    private ProductTranslationReadDao productTranslationReadDao;

    @InjectMocks
    private final ProductCollectionHydrator hydrator = new ProductCollectionHydrator();

    @BeforeClass
    public static void setupClass() {
        System.setProperty("file.encoding", "UTF8");
    }

    @Before
    public void setUpFixtures() {
        product = new Product(1L, new ProductInformation(42L, "name", "description", "paymentTerms", "100", "port",
                "10", "country", "10 days", "Model ZX 234", "keySpecifications"), new ProductMetadata(null, null,
                "metaTags", "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, SNAPSHOT_ID);
        products = Lists.newArrayList(product);

        productImages = Lists.newArrayList(PRODUCT_IMAGE);
        productCategories = Lists.newArrayList(CATEGORY_1, CATEGORY_2);
        suppliers = Lists.newArrayList(SUPPLIER);
        businessTypeBySnapshotId = ArrayListMultimap.create();
        businessTypeBySnapshotId.put(44L, BusinessType.AGENT);
        productTranslationsBySnapshotId = Maps.newHashMap();
        productTranslationsBySnapshotId.put(SNAPSHOT_ID, PRODUCT_TRANSLATION);
    }

    @Test
    public void hydrateLatestProductsWithoutTranslations() throws Exception {
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(productImages);
        when(productCategoryReadDao.findCategoriesBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                productCategories);
        when(supplierReadDao.loadLatestBySupplierIds(Lists.newArrayList(42L), Locale.ENGLISH)).thenReturn(suppliers);
        when(businessTypeReadDao.findBySnapshotIds(Lists.newArrayList(44L))).thenReturn(businessTypeBySnapshotId);
        when(productTranslationReadDao.findBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID), Locale.ENGLISH)).thenReturn(
                Maps.<Long, ProductTranslation> newHashMap());

        List<Product> persistedProducts = hydrator.hydrateLatest(products, Locale.ENGLISH);

        assertThat(persistedProducts, is(products));
        assertThatProductHasUntranslatedFieldValues();
        assertThat(persistedProducts.get(0).getImages(), is(productImages));
        assertThat(persistedProducts.get(0).getMetadata().getCategories(), is(productCategories));
        assertTrue(persistedProducts.get(0).getInformation().getSupplier().getCompany().getBusinessTypes()
                .contains(BusinessType.AGENT));
    }

    @Test
    public void hydrateLatestProductsWithTranslations() throws Exception {
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(productImages);
        when(productCategoryReadDao.findCategoriesBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                productCategories);
        when(supplierReadDao.loadLatestBySupplierIds(Lists.newArrayList(42L), Locale.ENGLISH)).thenReturn(suppliers);
        when(businessTypeReadDao.findBySnapshotIds(Lists.newArrayList(44L))).thenReturn(businessTypeBySnapshotId);
        when(productTranslationReadDao.findBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID), Locale.ENGLISH)).thenReturn(
                productTranslationsBySnapshotId);

        List<Product> persistedProducts = hydrator.hydrateLatest(products, Locale.ENGLISH);

        assertThat(persistedProducts, is(products));
        assertThatProductHasTranslatedFieldValues();
        assertThat(persistedProducts.get(0).getImages(), is(productImages));
        assertThat(persistedProducts.get(0).getMetadata().getCategories(), is(productCategories));
        assertTrue(persistedProducts.get(0).getInformation().getSupplier().getCompany().getBusinessTypes()
                .contains(BusinessType.AGENT));
    }

    @Test
    public void hydrateApprovedProductsWithoutTranslations() throws Exception {
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(productImages);
        when(productCategoryReadDao.findCategoriesBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                productCategories);
        when(supplierReadDao.loadApprovedBySupplierIds(Lists.newArrayList(42L), Locale.ENGLISH)).thenReturn(suppliers);
        when(businessTypeReadDao.findBySnapshotIds(Lists.newArrayList(44L))).thenReturn(businessTypeBySnapshotId);
        when(productTranslationReadDao.findBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID), Locale.ENGLISH)).thenReturn(
                Maps.<Long, ProductTranslation> newHashMap());

        List<Product> persistedProducts = hydrator.hydrateApproved(products, Locale.ENGLISH);

        assertThat(persistedProducts, is(products));
        assertThatProductHasUntranslatedFieldValues();
        assertThat(persistedProducts.get(0).getImages(), is(productImages));
        assertThat(persistedProducts.get(0).getMetadata().getCategories(), is(productCategories));
        assertTrue(persistedProducts.get(0).getInformation().getSupplier().getCompany().getBusinessTypes()
                .contains(BusinessType.AGENT));
    }

    @Test
    public void hydrateApprovedProductsWithTranslations() throws Exception {
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(productImages);
        when(productCategoryReadDao.findCategoriesBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                productCategories);
        when(supplierReadDao.loadApprovedBySupplierIds(Lists.newArrayList(42L), Locale.ENGLISH)).thenReturn(suppliers);
        when(businessTypeReadDao.findBySnapshotIds(Lists.newArrayList(44L))).thenReturn(businessTypeBySnapshotId);
        when(productTranslationReadDao.findBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID), Locale.ENGLISH)).thenReturn(
                productTranslationsBySnapshotId);

        List<Product> persistedProducts = hydrator.hydrateApproved(products, Locale.ENGLISH);

        assertThat(persistedProducts, is(products));
        assertThatProductHasTranslatedFieldValues();
        assertThat(persistedProducts.get(0).getImages(), is(productImages));
        assertThat(persistedProducts.get(0).getMetadata().getCategories(), is(productCategories));
        assertTrue(persistedProducts.get(0).getInformation().getSupplier().getCompany().getBusinessTypes()
                .contains(BusinessType.AGENT));
    }

    @Test
    public void hydrateApprovedProductWithoutTranslations() throws Exception {
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(productImages);
        when(productCategoryReadDao.findCategoriesBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                productCategories);
        when(supplierReadDao.loadApprovedBySupplierIds(Lists.newArrayList(42L), Locale.ENGLISH)).thenReturn(suppliers);
        when(businessTypeReadDao.findBySnapshotIds(Lists.newArrayList(44L))).thenReturn(businessTypeBySnapshotId);
        when(productTranslationReadDao.findBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID), Locale.ENGLISH)).thenReturn(
                Maps.<Long, ProductTranslation> newHashMap());

        Product persistedProduct = hydrator.hydrateApproved(product, Locale.ENGLISH);

        assertThat(persistedProduct, is(product));
        assertThatProductHasUntranslatedFieldValues();
        assertThat(persistedProduct.getImages(), is(productImages));
        assertThat(persistedProduct.getMetadata().getCategories(), is(productCategories));
        assertTrue(persistedProduct.getInformation().getSupplier().getCompany().getBusinessTypes()
                .contains(BusinessType.AGENT));
    }

    @Test
    public void hydrateApprovedProductWithTranslations() throws Exception {
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(productImages);
        when(productCategoryReadDao.findCategoriesBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                productCategories);
        when(supplierReadDao.loadApprovedBySupplierIds(Lists.newArrayList(42L), Locale.ENGLISH)).thenReturn(suppliers);
        when(businessTypeReadDao.findBySnapshotIds(Lists.newArrayList(44L))).thenReturn(businessTypeBySnapshotId);
        when(productTranslationReadDao.findBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID), Locale.ENGLISH)).thenReturn(
                productTranslationsBySnapshotId);

        Product persistedProduct = hydrator.hydrateApproved(product, Locale.ENGLISH);

        assertThat(persistedProduct, is(product));
        assertThatProductHasTranslatedFieldValues();
        assertThat(persistedProduct.getImages(), is(productImages));
        assertThat(persistedProduct.getMetadata().getCategories(), is(productCategories));
        assertTrue(persistedProduct.getInformation().getSupplier().getCompany().getBusinessTypes()
                .contains(BusinessType.AGENT));
    }

    @Test
    public void hydrateLatestProductWithoutTranslations() throws Exception {
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(productImages);
        when(productCategoryReadDao.findCategoriesBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                productCategories);
        when(supplierReadDao.loadLatestBySupplierIds(Lists.newArrayList(42L), Locale.ENGLISH)).thenReturn(suppliers);
        when(businessTypeReadDao.findBySnapshotIds(Lists.newArrayList(44L))).thenReturn(businessTypeBySnapshotId);
        when(productTranslationReadDao.findBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID), Locale.ENGLISH)).thenReturn(
                Maps.<Long, ProductTranslation> newHashMap());

        Product persistedProduct = hydrator.hydrateLatest(product, Locale.ENGLISH);

        assertThat(persistedProduct, is(product));
        assertThatProductHasUntranslatedFieldValues();
        assertThat(persistedProduct.getImages(), is(productImages));
        assertThat(persistedProduct.getMetadata().getCategories(), is(productCategories));
        assertTrue(persistedProduct.getInformation().getSupplier().getCompany().getBusinessTypes()
                .contains(BusinessType.AGENT));
    }

    @Test
    public void hydrateLatestProductWithTranslations() throws Exception {
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(productImages);
        when(productCategoryReadDao.findCategoriesBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                productCategories);
        when(supplierReadDao.loadLatestBySupplierIds(Lists.newArrayList(42L), Locale.ENGLISH)).thenReturn(suppliers);
        when(businessTypeReadDao.findBySnapshotIds(Lists.newArrayList(44L))).thenReturn(businessTypeBySnapshotId);
        when(productTranslationReadDao.findBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID), Locale.ENGLISH)).thenReturn(
                productTranslationsBySnapshotId);

        Product persistedProduct = hydrator.hydrateLatest(product, Locale.ENGLISH);

        assertThat(persistedProduct, is(product));
        assertThatProductHasTranslatedFieldValues();
        assertThat(persistedProduct.getImages(), is(productImages));
        assertThat(persistedProduct.getMetadata().getCategories(), is(productCategories));
        assertTrue(persistedProduct.getInformation().getSupplier().getCompany().getBusinessTypes()
                .contains(BusinessType.AGENT));
    }

    private void assertThatProductHasUntranslatedFieldValues() {
        assertThat(product.getInformation().getName(), is("name"));
        assertThat(product.getInformation().getDescription(), is("description"));
        assertThat(product.getInformation().getPaymentTerms(), is("paymentTerms"));
        assertThat(product.getInformation().getSpecifications(), is("keySpecifications"));
        assertThat(product.getMetadata().getKeywords(), is("keywords"));
        assertThat(product.getMetadata().getMetaTags(), is("metaTags"));
    }

    private void assertThatProductHasTranslatedFieldValues() {
        assertThat(product.getInformation().getName(), is("translatedProductName"));
        assertThat(product.getInformation().getDescription(), is("translatedProductDescription"));
        assertThat(product.getInformation().getPaymentTerms(), is("translatedPaymentTerms"));
        assertThat(product.getInformation().getSpecifications(), is("translatedKeySpecifications"));
        assertThat(product.getMetadata().getKeywords(), is("translatedKeywords"));
        assertThat(product.getMetadata().getMetaTags(), is("translatedMetaTags"));
    }

}
