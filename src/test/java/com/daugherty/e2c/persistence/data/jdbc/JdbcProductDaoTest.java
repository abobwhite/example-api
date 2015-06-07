package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.persistence.data.SortAndPaginationAware;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

public class JdbcProductDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcProductDao productDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("product.sql");
    }

    @Test
    public void getAllLatestReturnsAllLatestProducts() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productDao.createSortingAndPaginationCriteria(null, false, 1, 25,
                Locale.ENGLISH);
        List<Product> products = productDao.findLatest(sortingAndPaginationCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(9));
        assertProduct9991(products.get(0));
        assertProduct41InEnglish(products.get(1));
        assertProduct51(products.get(2));
        assertProduct22(products.get(3));
        assertProduct24(products.get(4), ApprovalStatus.DRAFT, 2402L, 4, "red leather cricket ball");
        assertProduct21(products.get(5));
        assertProduct23(products.get(6));
        assertProduct25(products.get(7));
        assertProduct253434(products.get(8));
    }

    @Test
    public void getAllLatestSortsByProductName() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productDao.createSortingAndPaginationCriteria(
                Product.NAME_SERIAL_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findLatest(sortingAndPaginationCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(9));
        assertProduct22(products.get(0));
        assertProduct41InEnglish(products.get(1));
        assertProduct51(products.get(2));
        assertProduct21(products.get(3));
        assertProduct23(products.get(4));
        assertProduct24(products.get(5), ApprovalStatus.DRAFT, 2402L, 4, "red leather cricket ball");
        assertProduct25(products.get(6));
        assertProduct253434(products.get(7));
        assertProduct9991(products.get(8));
    }

    @Test
    public void getAllLatestSortsByApprovalStatus() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productDao.createSortingAndPaginationCriteria(
                Product.APPROVAL_STATUS_SERIAL_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findLatest(sortingAndPaginationCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(9));
        assertProduct41InEnglish(products.get(0));
        assertProduct22(products.get(1));
        assertProduct23(products.get(2));
        assertProduct25(products.get(3));
        assertProduct253434(products.get(4));
        assertProduct51(products.get(5));
        assertProduct24(products.get(6), ApprovalStatus.DRAFT, 2402L, 4, "red leather cricket ball");
        assertProduct21(products.get(7));
        assertProduct9991(products.get(8));
    }

    @Test
    public void getAllLatestSortsByHotProducts() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productDao.createSortingAndPaginationCriteria(
                Product.HOT_PRODUCT_SERIAL_PROPERTY, true, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findLatest(sortingAndPaginationCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(9));
        assertProduct41InEnglish(products.get(0));
        assertProduct51(products.get(1));
        assertProduct24(products.get(2), ApprovalStatus.DRAFT, 2402L, 4, "red leather cricket ball");
        assertProduct21(products.get(3));
        assertProduct9991(products.get(4));
        assertProduct22(products.get(5));
        assertProduct23(products.get(6));
        assertProduct25(products.get(7));
        assertProduct253434(products.get(8));
    }

    @Test
    public void getAllLatestSortsByPublished() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productDao.createSortingAndPaginationCriteria(
                Product.PUBLISHED_SERIAL_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findLatest(sortingAndPaginationCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(9));
        assertProduct9991(products.get(0));
        assertProduct22(products.get(1));
        assertProduct41InEnglish(products.get(2));
        assertProduct51(products.get(3));
        assertProduct24(products.get(4), ApprovalStatus.DRAFT, 2402L, 4, "red leather cricket ball");
        assertProduct21(products.get(5));
        assertProduct23(products.get(6));
        assertProduct25(products.get(7));
        assertProduct253434(products.get(8));
    }

    @Test
    public void getAllLatestSortsInRandomOrder() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productDao.createSortingAndPaginationCriteria(
                SortAndPaginationAware.RANDOM_SORT_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<Product> firstSetOfProducts = productDao.findLatest(sortingAndPaginationCriteria);
        List<Product> secondSetOfProducts = productDao.findLatest(sortingAndPaginationCriteria);

        assertThat(firstSetOfProducts, is(not(secondSetOfProducts)));
    }

    @Test
    public void getAllLatestPaginates() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productDao.createSortingAndPaginationCriteria(null, false, 4, 25,
                Locale.ENGLISH);
        List<Product> products = productDao.findLatest(sortingAndPaginationCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(6));
        assertProduct22(products.get(0));
        assertProduct24(products.get(1), ApprovalStatus.DRAFT, 2402L, 4, "red leather cricket ball");
        assertProduct21(products.get(2));
        assertProduct23(products.get(3));
        assertProduct25(products.get(4));
        assertProduct253434(products.get(5));
    }

    @Test
    public void findLatestWithSupplierIdCriterionOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createLatestQueryCriteria(99912L, null, null, null, null, null, false,
                1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findLatest(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(2));
        assertProduct21(products.get(0));
        assertProduct23(products.get(1));
    }

    @Test
    public void findLatestWithProductNameCriterionOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createLatestQueryCriteria(99912L, "Product 1", null, null, null, null,
                false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findLatest(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(1));
        assertProduct21(products.get(0));
    }

    @Test
    public void findLatestWithSupplierIdAndApprovalStatusCriterionOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createLatestQueryCriteria(99912L, null,
                ApprovalStatus.APPROVED.getName(), null, null, null, false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findLatest(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(1));
        assertProduct23(products.get(0));
    }

    @Test
    public void findLatestWithSupplierIdAndHotProductCriterionOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createLatestQueryCriteria(99912L, null, null, true, null, null, false,
                1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findLatest(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(1));
        assertProduct21(products.get(0));
    }

    @Test
    public void findLatestWithSupplierIdAndPublishedCriterionOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createLatestQueryCriteria(99912L, null, null, null, true, null, false,
                1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findLatest(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(2));
        assertProduct21(products.get(0));
        assertProduct23(products.get(1));
    }

    @Test
    public void findLatestInChineseWithSupplierIdAndPublishedCriterionOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createLatestQueryCriteria(21L, null, null, null, true, null, false, 1,
                25, Locale.CHINESE);
        List<Product> products = productDao.findLatest(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(1));
        assertProduct41InEnglish(products.get(0));
    }

    @Test
    public void loadLatestByProductIdFindsLatestProduct() throws Exception {
        Product product = productDao.loadLatest(41L, Locale.ENGLISH);

        assertThat(product, is(notNullValue()));
        assertProduct41InEnglish(product);
    }

    @Test
    public void loadLatestByProductIdInChineseFindsLatestProduct() throws Exception {
        Product product = productDao.loadLatest(41L, Locale.CHINESE);

        assertThat(product, is(notNullValue()));
        assertProduct41InEnglish(product);
    }

    @Test
    public void getAllPublishedReturnsAllApprovedPublishedProductsForApprovedSuppliersWithPaidMemberships()
            throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productDao.createSortingAndPaginationCriteria(null, false, 1, 25,
                Locale.ENGLISH);
        List<Product> products = productDao.findPublished(sortingAndPaginationCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(2));
        assertProduct41InEnglish(products.get(0));
        assertProduct24(products.get(1), ApprovalStatus.APPROVED, 2401L, 3, "red cricket ball");
    }

    @Test
    public void getAllPublishedInChineseReturnsProductsNotYetTranslatedBecauseQueryPerformanceSucks() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productDao.createSortingAndPaginationCriteria(null, false, 1, 25,
                Locale.CHINESE);
        List<Product> products = productDao.findPublished(sortingAndPaginationCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(2));
        assertProduct41InEnglish(products.get(0));
        assertProduct24(products.get(1), ApprovalStatus.APPROVED, 2401L, 3, "red cricket ball");
    }

    @Test
    public void getAllPublishedSortsByPublicationDate() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productDao.createSortingAndPaginationCriteria("publicationDate",
                true, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findPublished(sortingAndPaginationCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(2));
        assertProduct24(products.get(0), ApprovalStatus.APPROVED, 2401L, 3, "red cricket ball");
        assertProduct41InEnglish(products.get(1));
    }

    @Test
    public void getAllPublishedPaginates() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productDao.createSortingAndPaginationCriteria(null, false, 2, 25,
                Locale.ENGLISH);
        List<Product> products = productDao.findPublished(sortingAndPaginationCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(1));
        assertProduct24(products.get(0), ApprovalStatus.APPROVED, 2401L, 3, "red cricket ball");
    }

    @Test
    public void findPublishedWithCountryCriterionOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createPublishedQueryCriteria(null, "United", null, null, null, null,
                null, false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findPublished(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(1));
        assertProduct24(products.get(0), ApprovalStatus.APPROVED, 2401L, 3, "red cricket ball");
    }

    @Test
    public void findPublishedWithOneCategoryIdCriterionValueOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createPublishedQueryCriteria(null, null, Lists.newArrayList(240702L),
                null, null, null, null, false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findPublished(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(1));
        assertProduct41InEnglish(products.get(0));
    }

    @Test
    public void findPublishedWithMultipleCategoryIdCriterionValuesOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createPublishedQueryCriteria(null, null,
                Lists.newArrayList(24L, 240601L, 240702L), null, null, null, null, false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findPublished(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(2));
        assertProduct41InEnglish(products.get(0));
        assertProduct24(products.get(1), ApprovalStatus.APPROVED, 2401L, 3, "red cricket ball");
    }

    @Test
    public void findPublishedWithHotCriterionWithoutRandomSortOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createPublishedQueryCriteria(null, null, null, true, null, null, null,
                false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findPublished(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(2));
        assertProduct41InEnglish(products.get(0));
        assertProduct24(products.get(1), ApprovalStatus.APPROVED, 2401L, 3, "red cricket ball");
    }

    @Test
    public void findPublishedWithHotCriterionWithRandomSortOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createPublishedQueryCriteria(null, null, null, true, null, null,
                SortAndPaginationAware.RANDOM_SORT_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findPublished(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(2));
        assertProduct41InEnglish(products.get(0));
        assertProduct24(products.get(1), ApprovalStatus.APPROVED, 2401L, 3, "red cricket ball");
    }

    @Test
    public void findPublishedWithBusinessTypeCriterionOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createPublishedQueryCriteria(null, null, null, null, 7L, null, null,
                false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findPublished(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(1));
        assertProduct41InEnglish(products.get(0));
    }

    @Test
    public void findPublishedWithMembershipTypeCriterionOnlyReturnsMatchingProducts() throws Exception {
        QueryCriteria queryCriteria = productDao.createPublishedQueryCriteria(null, null, null, null, null, 4, null,
                false, 1, 25, Locale.ENGLISH);
        List<Product> products = productDao.findPublished(queryCriteria);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(1));
        assertProduct41InEnglish(products.get(0));
    }

    @Test
    public void loadPublishedByProductIdFindsPublishedProduct() throws Exception {
        Product product = productDao.loadPublished(41L, Locale.ENGLISH);

        assertThat(product, is(notNullValue()));
        assertProduct41InEnglish(product);
    }

    @Test
    public void loadApprovedByProductIdFindsApprovedProduct() throws Exception {
        Product product = productDao.loadApproved(41L, Locale.ENGLISH);

        assertThat(product, is(notNullValue()));
        assertProduct41InEnglish(product);
    }

    @Test
    public void loadPublishedByProductIdInChineseFindsPublishedProductNotYetTranslatedBecauseQueryPerformanceSucks()
            throws Exception {
        Product product = productDao.loadPublished(41L, Locale.CHINESE);

        assertThat(product, is(notNullValue()));
        assertProduct41InEnglish(product);
    }

    @Test
    public void loadPublishedByProductIdThrowsExceptionWhenStatusNotApproved() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        productDao.loadPublished(51L, Locale.ENGLISH);
    }

    @Test
    public void loadProductIdMatchingLegacyIdForPublishedProductReturnsMatchingId() throws Exception {
        assertThat(productDao.loadProductIdMatchingLegacyId(141L), is(41L));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void loadProductIdMatchingLegacyIdThatDoesNotExistThrowsEmptyResultDataAccessException() throws Exception {
        productDao.loadProductIdMatchingLegacyId(999L);
    }

    @Test
    public void insertProductInsertsIntoBaseSnapshotAndEventTables() {
        List<ProductCategory> categories = Lists.newArrayList(new ProductCategory(1L));
        Product product = new Product(new ProductInformation(21L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                categories);

        Product createdProduct = productDao.insert(product);
        assertThat(createdProduct.getId(), is(notNullValue()));
        assertThat(createdProduct.getSnapshotId(), is(notNullValue()));

        Map<String, Object> productAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_audit_status WHERE product_id = ?", createdProduct.getId());
        assertThat((Long) productAuditStatusRowMap.get("product_id"), is(createdProduct.getId()));
        assertThat((Long) productAuditStatusRowMap.get("product_audit_id"), is(createdProduct.getSnapshotId()));
        assertThat((Integer) productAuditStatusRowMap.get("version_number"), is(1));
        assertThat(productAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.DRAFT.getName()));
        assertThat((Date) productAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> productRowMap = jdbcTemplate.queryForMap("SELECT * FROM product WHERE product_id = ?",
                createdProduct.getId());
        assertThat((Long) productRowMap.get("party_id"), is(product.getInformation().getSupplier().getId()));

        Map<String, Object> productAuditRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_audit WHERE product_audit_id = ?", createdProduct.getSnapshotId());
        assertThat(productAuditRowMap.get("payment_term").toString(), is(product.getInformation().getPaymentTerms()));
        assertThat(productAuditRowMap.get("minimum_order").toString(), is(product.getInformation().getMinimumOrder()));
        assertThat(productAuditRowMap.get("freight_on_board_port").toString(), is(product.getInformation()
                .getFreightOnBoardPort()));
        assertThat(productAuditRowMap.get("freight_on_board_price").toString(), is(product.getInformation()
                .getFreightOnBoardPrice()));
        assertThat(productAuditRowMap.get("country_of_origin").toString(), is(product.getInformation().getCountry()));
        assertThat(productAuditRowMap.get("lead_time").toString(), is(product.getInformation().getLeadTime()));
        assertThat(productAuditRowMap.get("model_number").toString(), is(product.getInformation().getModelNumber()));
        assertThat(productAuditRowMap.get("key_specification").toString(), is(product.getInformation()
                .getSpecifications()));
        assertThat(productAuditRowMap.get("product_name").toString(), is(product.getInformation().getName()));
        assertThat(productAuditRowMap.get("product_description").toString(), is(product.getInformation()
                .getDescription()));
    }

    @Test
    public void updateProductInsertsIntoSnapshotAndEventTables() {
        Product product = new Product(21L, new ProductInformation(99912L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                new ProductMetadata(null, null, "metaTags", "keywords", true, true, false, new Date()), 666L,
                ApprovalStatus.PENDING_APPROVAL, 2, 51L);
        product.getMetadata().setCategories(Lists.newArrayList(new ProductCategory(1L)));
        product.setImages(Lists.newArrayList(new ProductImage("productImageLink", true)));

        Product updatedProduct = productDao.update(product);
        assertThat(updatedProduct.getId(), is(21L));
        assertThat(updatedProduct.getSnapshotId(), is(notNullValue()));
        assertThat(updatedProduct.getSnapshotId(), is(not(51L)));

        Map<String, Object> productAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_audit_status WHERE product_id = ? AND product_audit_status_id  NOT IN (61)",
                updatedProduct.getId());
        assertThat((Long) productAuditStatusRowMap.get("product_id"), is(updatedProduct.getId()));
        assertThat((Long) productAuditStatusRowMap.get("product_audit_id"), is(updatedProduct.getSnapshotId()));
        assertThat((Integer) productAuditStatusRowMap.get("version_number"), is(2));
        assertThat(productAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.PENDING_APPROVAL.getName()));
        assertThat((Date) productAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> productRowMap = jdbcTemplate.queryForMap("SELECT * FROM product WHERE product_id = ?",
                updatedProduct.getId());
        assertThat((Long) productRowMap.get("party_id"), is(product.getInformation().getSupplier().getId()));

        Map<String, Object> productAuditRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_audit WHERE product_audit_id = ?", updatedProduct.getSnapshotId());
        assertThat(productAuditRowMap.get("payment_term").toString(), is(product.getInformation().getPaymentTerms()));
        assertThat(productAuditRowMap.get("minimum_order").toString(), is(product.getInformation().getMinimumOrder()));
        assertThat(productAuditRowMap.get("freight_on_board_port").toString(), is(product.getInformation()
                .getFreightOnBoardPort()));
        assertThat(productAuditRowMap.get("freight_on_board_price").toString(), is(product.getInformation()
                .getFreightOnBoardPrice()));
        assertThat(productAuditRowMap.get("country_of_origin").toString(), is(product.getInformation().getCountry()));
        assertThat(productAuditRowMap.get("lead_time").toString(), is(product.getInformation().getLeadTime()));
        assertThat(productAuditRowMap.get("model_number").toString(), is(product.getInformation().getModelNumber()));
        assertThat(productAuditRowMap.get("key_specification").toString(), is(product.getInformation()
                .getSpecifications()));
        assertThat(productAuditRowMap.get("product_name").toString(), is(product.getInformation().getName()));
        assertThat(productAuditRowMap.get("product_description").toString(), is(product.getInformation()
                .getDescription()));
    }

    @Test
    public void getPublishedByProductIdsReturnsApprovedPublishedProductForProductId() throws Exception {
        List<Product> products = productDao.loadPublishedByProductIds(Lists.newArrayList(41L), Locale.ENGLISH);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(1));
        assertProduct41InEnglish(products.get(0));
    }

    @Test
    public void getTranslatedPublishedByProductIdsReturnsApprovedPublishedProductForProductId() throws Exception {
        List<Product> products = productDao.loadPublishedByProductIds(Lists.newArrayList(41L), Locale.CHINESE);

        assertThat(products, is(notNullValue()));
        assertThat(products.size(), is(1));
        assertProduct41InEnglish(products.get(0));
    }

    @Test
    public void deleteProductsByPartyId() throws Exception {

        List<Product> products = productDao.findLatest(productDao.createLatestQueryCriteria(99911L, null, null, null,
                null, null, false, 1, 100, Locale.ENGLISH));

        assertThat(products.size(), is(productDao.deleteProductsByPartyId(99911L)));
    }

    private void assertProduct9991(Product product) {
        assertThat(product.getId(), is(9991L));
        assertThat(product.getInformation().getSupplier().getId(), is(26L));
        assertThat(product.getSnapshotId(), is(999124L));
        assertThat(product.getVersion(), is(1));
        assertThat(product.getApprovalStatus(), is(ApprovalStatus.PENDING_TRANSLATION));
        assertThat(product.getInformation().getMinimumOrder(), is("10000"));
        assertThat(product.getInformation().getFreightOnBoardPort(), is("freight on board"));
        assertThat(product.getInformation().getFreightOnBoardPrice(), is("203.42"));
        assertThat(product.getInformation().getCountry(), is("England"));
        assertThat(product.getInformation().getLeadTime(), is("lead time"));
        assertThat(product.getInformation().getModelNumber(), is("model number"));
        assertThat(product.getCertificationId(), is(nullValue()));
        assertFalse(product.getMetadata().isHot());
        assertFalse(product.getMetadata().isPublished());
        assertThat(product.getMetadata().getChineseName(), is("latest translated product name"));
        assertThat(product.getMetadata().getEnglishName(), is("product name"));
        assertThat(product.getInformation().getPaymentTerms(), is("payment term"));
        assertThat(product.getInformation().getSpecifications(), is("key specification"));
        assertThat(product.getMetadata().getMetaTags(), is("these are meta tags"));
        assertThat(product.getInformation().getName(), is("product name"));
        assertThat(product.getInformation().getDescription(), is("product description"));
        assertThat(product.getMetadata().getKeywords(), is("these are key words"));
    }

    private void assertProduct41InEnglish(Product product) {
        assertThat(product.getId(), is(41L));
        assertThat(product.getInformation().getSupplier().getId(), is(21L));
        assertThat(product.getSnapshotId(), is(71L));
        assertThat(product.getVersion(), is(1));
        assertThat(product.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(product.getInformation().getMinimumOrder(), is("1000 +"));
        assertThat(product.getInformation().getFreightOnBoardPort(), is("Mexico"));
        assertThat(product.getInformation().getFreightOnBoardPrice(), is("800.00"));
        assertThat(product.getInformation().getCountry(), is("Mexico"));
        assertThat(product.getInformation().getLeadTime(), is("3 + Weeks"));
        assertThat(product.getInformation().getModelNumber(), is("XJ900"));
        assertThat(product.getCertificationId(), is(nullValue()));
        assertTrue(product.getMetadata().isHot());
        assertTrue(product.getMetadata().isPublished());
        assertThat(product.getMetadata().getChineseName(), is("Translation of Black Beans (ABOB)"));
        assertThat(product.getMetadata().getEnglishName(), is("Black Beans (ABOB)"));
        assertThat(product.getInformation().getPaymentTerms(), is("payPal"));
        assertThat(product.getInformation().getSpecifications(), is("Home grown and tasty."));
        assertThat(product.getMetadata().getMetaTags(), is("mexican food, beans"));
        assertThat(product.getInformation().getName(), is("Black Beans (ABOB)"));
        assertThat(
                product.getInformation().getDescription(),
                is("ABOB Black Beans are made from the finest beans.  We promise you will love every spoonful of our delicious beans.  We gaurantee it."));
        assertThat(product.getMetadata().getKeywords(), is("Beans, Yummy in My Tummy"));
    }

    private void assertProduct51(Product product) {
        assertThat(product.getId(), is(51L));
        assertThat(product.getInformation().getSupplier().getId(), is(25L));
        assertThat(product.getSnapshotId(), is(81L));
        assertThat(product.getVersion(), is(2));
        assertThat(product.getApprovalStatus(), is(ApprovalStatus.DRAFT));
        assertThat(product.getInformation().getPaymentTerms(), is("payPal"));
        assertThat(product.getInformation().getMinimumOrder(), is("1000 +"));
        assertThat(product.getInformation().getFreightOnBoardPort(), is("China"));
        assertThat(product.getInformation().getFreightOnBoardPrice(), is("800.00"));
        assertThat(product.getInformation().getCountry(), is("United States"));
        assertThat(product.getInformation().getLeadTime(), is("4 + Weeks"));
        assertThat(product.getInformation().getModelNumber(), is("XJ900"));
        assertThat(product.getInformation().getSpecifications(), is("home grown and tasty"));
        assertThat(product.getMetadata().getMetaTags(), is("mexican food, beans"));
        assertThat(product.getInformation().getName(), is("Pinto Beans"));
        assertThat(product.getInformation().getDescription(), is("ABOB Pinto Beans"));
        assertThat(product.getMetadata().getKeywords(), is("beans, yummy"));
        assertThat(product.getCertificationId(), is(666L));
        assertTrue(product.getMetadata().isHot());
        assertTrue(product.getMetadata().isPublished());
        assertThat(product.getMetadata().getChineseName(), is(nullValue()));
        assertThat(product.getMetadata().getEnglishName(), is("Pinto Beans"));
    }

    private void assertProduct21(Product product) {
        assertThat(product.getId(), is(21L));
        assertThat(product.getInformation().getSupplier().getId(), is(99912L));
        assertThat(product.getSnapshotId(), is(51L));
        assertThat(product.getVersion(), is(1));
        assertThat(product.getApprovalStatus(), is(ApprovalStatus.DRAFT));
        assertThat(product.getInformation().getPaymentTerms(), is("payPal"));
        assertThat(product.getInformation().getMinimumOrder(), is("1000 +"));
        assertThat(product.getInformation().getFreightOnBoardPort(), is("China"));
        assertThat(product.getInformation().getFreightOnBoardPrice(), is("800.00"));
        assertThat(product.getInformation().getCountry(), is("United States"));
        assertThat(product.getInformation().getLeadTime(), is("4 + Weeks"));
        assertThat(product.getInformation().getModelNumber(), is("XJ900"));
        assertThat(product.getInformation().getSpecifications(), is("product is big"));
        assertThat(product.getMetadata().getMetaTags(), is("product"));
        assertThat(product.getInformation().getName(), is("Product 1"));
        assertThat(product.getInformation().getDescription(), is("ABOB Product"));
        assertThat(product.getMetadata().getKeywords(), is("beans, yummy"));
        assertThat(product.getCertificationId(), is(nullValue()));
        assertTrue(product.getMetadata().isHot());
        assertTrue(product.getMetadata().isPublished());
        assertThat(product.getMetadata().getChineseName(), is(nullValue()));
        assertThat(product.getMetadata().getEnglishName(), is("Product 1"));
    }

    private void assertProduct22(Product product) {
        assertThat(product.getId(), is(22L));
        assertThat(product.getInformation().getSupplier().getId(), is(99911L));
        assertThat(product.getSnapshotId(), is(52L));
        assertThat(product.getVersion(), is(1));
        assertThat(product.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(product.getInformation().getPaymentTerms(), is("payPal"));
        assertThat(product.getInformation().getMinimumOrder(), is("1000 +"));
        assertThat(product.getInformation().getFreightOnBoardPort(), is("China"));
        assertThat(product.getInformation().getFreightOnBoardPrice(), is("800.00"));
        assertThat(product.getInformation().getCountry(), is("United States"));
        assertThat(product.getInformation().getLeadTime(), is("4 + Weeks"));
        assertThat(product.getInformation().getModelNumber(), is("XJ900"));
        assertThat(product.getInformation().getSpecifications(), is("product is small"));
        assertThat(product.getMetadata().getMetaTags(), is("product"));
        assertThat(product.getInformation().getName(), is("A Product 2"));
        assertThat(product.getInformation().getDescription(), is("ABOB Product"));
        assertThat(product.getMetadata().getKeywords(), is("beans, yummy"));
        assertThat(product.getCertificationId(), is(nullValue()));
        assertFalse(product.getMetadata().isHot());
        assertFalse(product.getMetadata().isPublished());
        assertThat(product.getMetadata().getChineseName(), is(nullValue()));
        assertThat(product.getMetadata().getEnglishName(), is("A Product 2"));
    }

    private void assertProduct23(Product product) {
        assertThat(product.getId(), is(23L));
        assertThat(product.getInformation().getSupplier().getId(), is(99912L));
        assertThat(product.getSnapshotId(), is(2301L));
        assertThat(product.getVersion(), is(2));
        assertThat(product.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(product.getInformation().getPaymentTerms(), is("12 months"));
        assertThat(product.getInformation().getMinimumOrder(), is("1000 +"));
        assertThat(product.getInformation().getFreightOnBoardPort(), is("Los Angeles"));
        assertThat(product.getInformation().getFreightOnBoardPrice(), is("666.00"));
        assertThat(product.getInformation().getCountry(), is("United States"));
        assertThat(product.getInformation().getLeadTime(), is("2 Weeks"));
        assertThat(product.getInformation().getModelNumber(), is("2301"));
        assertThat(product.getInformation().getSpecifications(), is("skeleton keys"));
        assertThat(product.getMetadata().getMetaTags(), is("key"));
        assertThat(product.getInformation().getName(), is("Product 23"));
        assertThat(product.getInformation().getDescription(), is("Product 23 description"));
        assertThat(product.getMetadata().getKeywords(), is("magic key"));
        assertThat(product.getCertificationId(), is(nullValue()));
        assertFalse(product.getMetadata().isHot());
        assertTrue(product.getMetadata().isPublished());
        assertThat(product.getMetadata().getChineseName(), is(nullValue()));
        assertThat(product.getMetadata().getEnglishName(), is("Product 23"));
    }

    private void assertProduct24(Product product, ApprovalStatus approvalStatus, long snapshotId, int version,
            String keywords) {
        assertThat(product.getId(), is(24L));
        assertThat(product.getInformation().getSupplier().getId(), is(99911L));
        assertThat(product.getSnapshotId(), is(snapshotId));
        assertThat(product.getVersion(), is(version));
        assertThat(product.getApprovalStatus(), is(approvalStatus));
        assertThat(product.getInformation().getPaymentTerms(), is("9 months"));
        assertThat(product.getInformation().getMinimumOrder(), is("500 - 1000"));
        assertThat(product.getInformation().getFreightOnBoardPort(), is("London"));
        assertThat(product.getInformation().getFreightOnBoardPrice(), is("586.00"));
        assertThat(product.getInformation().getCountry(), is("United Kingdom"));
        assertThat(product.getInformation().getLeadTime(), is("2 Weeks"));
        assertThat(product.getInformation().getModelNumber(), is("2401"));
        assertThat(product.getInformation().getSpecifications(), is("hard, round and fast"));
        assertThat(product.getMetadata().getMetaTags(), is("cricket ball"));
        assertThat(product.getInformation().getName(), is("Product 24"));
        assertThat(product.getInformation().getDescription(), is("Product 24 description"));
        assertThat(product.getMetadata().getKeywords(), is(keywords));
        assertThat(product.getCertificationId(), is(nullValue()));
        assertTrue(product.getMetadata().isHot());
        assertTrue(product.getMetadata().isPublished());
        assertThat(product.getMetadata().getChineseName(), is(nullValue()));
        assertThat(product.getMetadata().getEnglishName(), is("Product 24"));
    }

    private void assertProduct25(Product product) {
        assertThat(product.getId(), is(25L));
        assertThat(product.getInformation().getSupplier().getId(), is(99913L));
        assertThat(product.getSnapshotId(), is(2501L));
        assertThat(product.getVersion(), is(1));
        assertThat(product.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(product.getInformation().getPaymentTerms(), is("5 months"));
        assertThat(product.getInformation().getMinimumOrder(), is("500 - 1000"));
        assertThat(product.getInformation().getFreightOnBoardPort(), is("Hobart"));
        assertThat(product.getInformation().getFreightOnBoardPrice(), is("123.00"));
        assertThat(product.getInformation().getCountry(), is("Australia"));
        assertThat(product.getInformation().getLeadTime(), is("3 Weeks"));
        assertThat(product.getInformation().getModelNumber(), is("2501"));
        assertThat(product.getInformation().getSpecifications(), is("bouncy and green"));
        assertThat(product.getMetadata().getMetaTags(), is("tennis ball"));
        assertThat(product.getInformation().getName(), is("Product 25"));
        assertThat(product.getInformation().getDescription(), is("Product 25 description"));
        assertThat(product.getMetadata().getKeywords(), is("green tennis ball"));
        assertThat(product.getCertificationId(), is(nullValue()));
        assertFalse(product.getMetadata().isHot());
        assertTrue(product.getMetadata().isHotProductOverride());
        assertTrue(product.getMetadata().isPublished());
        assertThat(product.getMetadata().getChineseName(), is(nullValue()));
        assertThat(product.getMetadata().getEnglishName(), is("Product 25"));
    }

    private void assertProduct253434(Product product) {
        assertThat(product.getId(), is(253434L));
        assertThat(product.getInformation().getSupplier().getId(), is(9991334343L));
        assertThat(product.getSnapshotId(), is(25023231L));
        assertThat(product.getVersion(), is(1));
        assertThat(product.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(product.getInformation().getPaymentTerms(), is("5 months"));
        assertThat(product.getInformation().getMinimumOrder(), is("500 - 1000"));
        assertThat(product.getInformation().getFreightOnBoardPort(), is("Hobart"));
        assertThat(product.getInformation().getFreightOnBoardPrice(), is("123.00"));
        assertThat(product.getInformation().getCountry(), is("Australia"));
        assertThat(product.getInformation().getLeadTime(), is("3 Weeks"));
        assertThat(product.getInformation().getModelNumber(), is("2501"));
        assertThat(product.getInformation().getSpecifications(), is("bouncy and green"));
        assertThat(product.getMetadata().getMetaTags(), is("tennis ball"));
        assertThat(product.getInformation().getName(), is("Product 25"));
        assertThat(product.getInformation().getDescription(), is("Product 25 description"));
        assertThat(product.getMetadata().getKeywords(), is("green tennis ball"));
        assertThat(product.getCertificationId(), is(nullValue()));
        assertFalse(product.getMetadata().isHot());
        assertTrue(product.getMetadata().isHotProductOverride());
        assertTrue(product.getMetadata().isPublished());
        assertThat(product.getMetadata().getChineseName(), is(nullValue()));
        assertThat(product.getMetadata().getEnglishName(), is("Product 25"));
    }

}
