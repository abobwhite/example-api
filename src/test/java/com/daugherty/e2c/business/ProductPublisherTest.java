package com.daugherty.e2c.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.MembershipProductSummary;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.persistence.data.HotProductOverrideWriteDao;
import com.daugherty.e2c.persistence.data.MembershipProductSummaryReadDao;
import com.daugherty.e2c.persistence.data.ProductPublicationWriteDao;
import com.daugherty.e2c.persistence.data.ProductReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

@RunWith(MockitoJUnitRunner.class)
public class ProductPublisherTest {

    private static final long SNAPSHOT_ID = 32L;

    @Mock
    private MembershipProductSummaryReadDao productSummaryReadDao;
    @Mock
    private SupplierReadDao supplierReadDao;
    @Mock
    private ProductPublicationWriteDao productPublicationWriteDao;
    @Mock
    private HotProductOverrideWriteDao hotProductOverrideWriteDao;
    @Mock
    private ProductReadDao productReadDao;
    @Mock
    private MembershipRenewalTypeHelper membershipRenewalTypeHelper;

    @InjectMocks
    private final ProductPublisher publisher = new ProductPublisher();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void publishWhenSupplierActiveMembershipNotMaxedOutInvokesWriteDao() {
        MembershipProductSummary summary = new MembershipProductSummary(586L);
        summary.setPublished(5);

        ProductInformation productInformation = new ProductInformation(99912L, "name", "description", "paymentTerms",
                "100", "port", "10", "country", "10 days", "Model ZX 234", "keySpecifications");

        Product product = new Product(21L, productInformation, new ProductMetadata(null, null, "metaTags", "keywords",
                false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, SNAPSHOT_ID);
        MembershipLevel level = new MembershipLevel(101L, 1, new BigDecimal("0.000"), 6, summary.getPublished() + 1,
                2147483647, 600, true, 1, false, false, false, 0, false, false, false, false, false, false,
                false);
        Membership membership = new Membership(product.getInformation().getSupplier().getId(), level);
        membership.setId(summary.getId());

        productInformation.getSupplier().getMemberships().add(membership);

        when(productReadDao.loadLatest(21L, Locale.ENGLISH)).thenReturn(product);
        when(supplierReadDao.loadLatest(product.getInformation().getSupplier().getId(), Locale.ENGLISH)).thenReturn(
                product.getInformation().getSupplier());
        when(productSummaryReadDao.loadByMembershipId(586L)).thenReturn(summary);

        publisher.publish(21L);

        verify(productPublicationWriteDao).insertPublished(21L, true);
    }

    @Test
    public void publishWhenSupplierMaxedOutThrowsException() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Product.PUBLISHED_SERIAL_PROPERTY,
                Validatable.PRODUCT_PUBLICATIONS_LENGTH));

        MembershipProductSummary summary = new MembershipProductSummary(586L);
        summary.setPublished(5);
        ProductInformation productInformation = new ProductInformation(99912L, "name", "description", "paymentTerms",
                "100", "port", "10", "country", "10 days", "Model ZX 234", "keySpecifications");
        Product product = new Product(21L, productInformation, new ProductMetadata(null, null, "metaTags", "keywords",
                false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, SNAPSHOT_ID);
        MembershipLevel level = new MembershipLevel(101L, 1, new BigDecimal("0.000"), 6, summary.getPublished(),
                2147483647, 600, true, 1, false, false, false, 0, false, false, false, false, false, false,
                false);
        Membership membership = new Membership(product.getInformation().getSupplier().getId(), level);
        membership.setId(summary.getId());
        productInformation.getSupplier().getMemberships().add(membership);

        when(productReadDao.loadLatest(21L, Locale.ENGLISH)).thenReturn(product);
        when(supplierReadDao.loadLatest(product.getInformation().getSupplier().getId(), Locale.ENGLISH)).thenReturn(
                product.getInformation().getSupplier());
        when(productSummaryReadDao.loadByMembershipId(586L)).thenReturn(summary);

        publisher.publish(21L);

        verifyZeroInteractions(productPublicationWriteDao, membershipRenewalTypeHelper);
    }

    @Test
    public void unpublishInvokesWriteDao() {
        publisher.unpublish(42L);

        verify(productPublicationWriteDao).insertPublished(42L, false);
        verifyZeroInteractions(productSummaryReadDao, supplierReadDao);
    }

    @Test
    public void heatWhenSupplierActiveMembershipNotMaxedOutInvokesWriteDao() {
        MembershipProductSummary summary = new MembershipProductSummary(586L);
        summary.setHotProducts(5);
        ProductInformation productInformation = new ProductInformation(99912L, "name", "description", "paymentTerms",
                "100", "port", "10", "country", "10 days", "Model ZX 234", "keySpecifications");
        Product product = new Product(21L, productInformation, new ProductMetadata(null, null, "metaTags", "keywords",
                false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, SNAPSHOT_ID);
        MembershipLevel level = new MembershipLevel(101L, 1, new BigDecimal("0.000"), 6, 5, 2147483647, 600, true,
                summary.getHotProducts() + 1, false, false, false, 0, false, false, false, false, false, false,
                false);
        Membership membership = new Membership(product.getInformation().getSupplier().getId(), level);
        membership.setId(summary.getId());
        productInformation.getSupplier().getMemberships().add(membership);

        when(productReadDao.loadLatest(21L, Locale.ENGLISH)).thenReturn(product);
        when(supplierReadDao.loadLatest(product.getInformation().getSupplier().getId(), Locale.ENGLISH)).thenReturn(
                product.getInformation().getSupplier());
        when(productSummaryReadDao.loadByMembershipId(586L)).thenReturn(summary);

        publisher.heat(21L);

        verify(productPublicationWriteDao).insertHot(21L, true);
    }

    @Test
    public void heatWhenSupplierHasNoActiveMembershipAndProvisionalMembershipNotMaxedOutInvokesWriteDao() {
        MembershipProductSummary summary = new MembershipProductSummary(586L);
        summary.setHotProducts(5);

        MembershipLevel level = new MembershipLevel(101L, 1, new BigDecimal("0.000"), 6, 5, 2147483647, 600, true,
                summary.getHotProducts() + 1, false, false, false, 0, false, false, false, false, false, false,
                false);

        Membership provisional = new Membership(586L, 22L, level, ApprovalStatus.PROVISIONAL, 1, new Date(),
                new Date(), new Date(), BigDecimal.TEN, BigDecimal.TEN, 1L, PaymentType.NONE, null, null, null);

        Supplier supplier = new Supplier(22L, "pBVQJNb4", null, null, ApprovalStatus.DRAFT, 1, 44L,
                "import_license_ref_id", provisional, null);

        Product product = new Product(21L, new ProductInformation(99912L, "name", "description", "paymentTerms", "100",
                "port", "10", "country", "10 days", "Model ZX 234", "keySpecifications"), new ProductMetadata(null,
                null, "metaTags", "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1,
                SNAPSHOT_ID);

        when(productReadDao.loadLatest(21L, Locale.ENGLISH)).thenReturn(product);
        when(supplierReadDao.loadLatest(product.getInformation().getSupplier().getId(), Locale.ENGLISH)).thenReturn(
                supplier);
        when(membershipRenewalTypeHelper.hasProvisionalRenewalOrDowngrade(null, provisional)).thenReturn(false);
        when(productSummaryReadDao.loadByMembershipId(586L)).thenReturn(summary);

        publisher.heat(21L);

        verify(productPublicationWriteDao).insertHot(21L, true);
    }

    @Test
    public void heatWhenSupplierActiveMembershipMaxedOutButProvisionalMembershipNotMaxedOutAndIsUpgradeInvokesWriteDao() {
        Product product = new Product(21L, new ProductInformation(99912L, "name", "description", "paymentTerms", "100",
                "port", "10", "country", "10 days", "Model ZX 234", "keySpecifications"), new ProductMetadata(null,
                null, "metaTags", "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1,
                SNAPSHOT_ID);

        MembershipProductSummary activeSummary = new MembershipProductSummary(586L);
        activeSummary.setHotProducts(5);

        MembershipProductSummary provisionalSummary = new MembershipProductSummary(587L);
        provisionalSummary.setHotProducts(5);

        MembershipLevel activeLevel = new MembershipLevel(101L, 1, new BigDecimal("0.000"), 6, 5, 2147483647, 600,
                true, activeSummary.getHotProducts(), false, false, false, 0, false, false, false, false, false,
                false, false);

        MembershipLevel provisionalLevel = new MembershipLevel(102L, 2, new BigDecimal("0.000"), 6, 5, 2147483647, 600,
                true, provisionalSummary.getHotProducts() + 1, false, false, false, 0, false, false, false, true,
                false, false, false);

        Membership active = new Membership(product.getInformation().getSupplier().getId(), activeLevel);
        active.setId(activeSummary.getId());

        Membership provisional = new Membership(product.getInformation().getSupplier().getId(), provisionalLevel);
        provisional.setId(provisionalSummary.getId());

        Supplier supplier = new Supplier(22L, "pBVQJNb4", null, null, ApprovalStatus.DRAFT, 1, 44L,
                "import_license_ref_id", provisional, null);

        supplier.getMemberships().add(active);

        when(productReadDao.loadLatest(21L, Locale.ENGLISH)).thenReturn(product);
        when(supplierReadDao.loadLatest(product.getInformation().getSupplier().getId(), Locale.ENGLISH)).thenReturn(
                supplier);
        when(membershipRenewalTypeHelper.hasProvisionalRenewalOrDowngrade(active, provisional)).thenReturn(false);
        when(productSummaryReadDao.loadByMembershipId(586L)).thenReturn(activeSummary);
        when(productSummaryReadDao.loadByMembershipId(587L)).thenReturn(provisionalSummary);

        publisher.heat(21L);

        verify(productPublicationWriteDao).insertHot(21L, true);
    }

    @Test
    public void heatWhenSupplierActiveMembershipMaxedOutThrowsException() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Product.HOT_PRODUCT_SERIAL_PROPERTY,
                Validatable.PRODUCT_HOTS_LENGTH));

        MembershipProductSummary summary = new MembershipProductSummary(586L);
        summary.setHotProducts(28);
        Product product = new Product(21L, new ProductInformation(99912L, "name", "description", "paymentTerms", "100",
                "port", "10", "country", "10 days", "Model ZX 234", "keySpecifications"), new ProductMetadata(null,
                null, "metaTags", "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1,
                SNAPSHOT_ID);
        MembershipLevel level = new MembershipLevel(101L, 1, new BigDecimal("0.000"), 6, 5, 2147483647, 600, true,
                summary.getHotProducts(), false, false, false, 0, false, false, false, false, false, false,
                false);
        Membership membership = new Membership(product.getInformation().getSupplier().getId(), level);
        membership.setId(summary.getId());

        Supplier supplier = new Supplier(22L, "pBVQJNb4", null, null, ApprovalStatus.DRAFT, 1, 44L,
                "import_license_ref_id", null, null);

        supplier.getMemberships().add(membership);

        when(productReadDao.loadLatest(21L, Locale.ENGLISH)).thenReturn(product);
        when(supplierReadDao.loadLatest(product.getInformation().getSupplier().getId(), Locale.ENGLISH)).thenReturn(
                supplier);
        when(productSummaryReadDao.loadByMembershipId(586L)).thenReturn(summary);

        publisher.heat(21L);

        verifyZeroInteractions(productPublicationWriteDao, membershipRenewalTypeHelper);
    }

    @Test
    public void heatWhenSupplierActiveMembershipMaxedOutButProvisionalMembershipNotMaxedOutAndIsRenewalThrowsException() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Product.HOT_PRODUCT_SERIAL_PROPERTY,
                Validatable.PRODUCT_HOTS_LENGTH));

        Product product = new Product(21L, new ProductInformation(99912L, "name", "description", "paymentTerms", "100",
                "port", "10", "country", "10 days", "Model ZX 234", "keySpecifications"), new ProductMetadata(null,
                null, "metaTags", "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1,
                SNAPSHOT_ID);

        MembershipProductSummary activeSummary = new MembershipProductSummary(586L);
        activeSummary.setHotProducts(5);

        MembershipProductSummary provisionalSummary = new MembershipProductSummary(587L);
        provisionalSummary.setHotProducts(5);

        MembershipLevel activeLevel = new MembershipLevel(101L, 2, new BigDecimal("0.000"), 6, 5, 2147483647, 600,
                true, activeSummary.getHotProducts(), false, false, false, 0, false, false, false, false, false,
                false, false);

        MembershipLevel provisionalLevel = new MembershipLevel(102L, 2, new BigDecimal("0.000"), 6, 5, 2147483647, 600,
                true, provisionalSummary.getHotProducts() + 1, false, false, false, 0, false, false, false, true,
                false, false, false);

        Membership active = new Membership(product.getInformation().getSupplier().getId(), activeLevel);
        active.setId(activeSummary.getId());

        Membership provisional = new Membership(product.getInformation().getSupplier().getId(), provisionalLevel);
        provisional.setId(provisionalSummary.getId());

        Supplier supplier = new Supplier(22L, "pBVQJNb4", null, null, ApprovalStatus.DRAFT, 1, 44L,
                "import_license_ref_id", null, null);

        supplier.getMemberships().add(active);

        when(productReadDao.loadLatest(21L, Locale.ENGLISH)).thenReturn(product);
        when(supplierReadDao.loadLatest(product.getInformation().getSupplier().getId(), Locale.ENGLISH)).thenReturn(
                supplier);
        when(membershipRenewalTypeHelper.hasProvisionalRenewalOrDowngrade(active, provisional)).thenReturn(true);
        when(productSummaryReadDao.loadByMembershipId(586L)).thenReturn(activeSummary);
        when(productSummaryReadDao.loadByMembershipId(587L)).thenReturn(provisionalSummary);

        publisher.heat(21L);

        verifyZeroInteractions(productPublicationWriteDao);
    }

    @Test
    public void heatWhenSupplierActiveMembershipMaxedOutButProvisionalMembershipNotMaxedOutAndIsDowngradeThrowsException() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Product.HOT_PRODUCT_SERIAL_PROPERTY,
                Validatable.PRODUCT_HOTS_LENGTH));

        Product product = new Product(21L, new ProductInformation(99912L, "name", "description", "paymentTerms", "100",
                "port", "10", "country", "10 days", "Model ZX 234", "keySpecifications"), new ProductMetadata(null,
                null, "metaTags", "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1,
                SNAPSHOT_ID);

        MembershipProductSummary activeSummary = new MembershipProductSummary(586L);
        activeSummary.setHotProducts(5);

        MembershipProductSummary provisionalSummary = new MembershipProductSummary(587L);
        provisionalSummary.setHotProducts(5);

        MembershipLevel activeLevel = new MembershipLevel(101L, 2, new BigDecimal("0.000"), 6, 5, 2147483647, 600,
                true, activeSummary.getHotProducts(), false, false, false, 0, false, false, false, true, false,
                false, false);

        MembershipLevel provisionalLevel = new MembershipLevel(102L, 1, new BigDecimal("0.000"), 6, 5, 2147483647, 600,
                true, provisionalSummary.getHotProducts() + 1, false, false, false, 0, false, false, false, false,
                false, false, false);

        Membership active = new Membership(product.getInformation().getSupplier().getId(), activeLevel);
        active.setId(activeSummary.getId());

        Membership provisional = new Membership(product.getInformation().getSupplier().getId(), provisionalLevel);
        provisional.setId(provisionalSummary.getId());

        Supplier supplier = new Supplier(22L, "pBVQJNb4", null, null, ApprovalStatus.DRAFT, 1, 44L,
                "import_license_ref_id", null, null);

        supplier.getMemberships().add(active);

        when(productReadDao.loadLatest(21L, Locale.ENGLISH)).thenReturn(product);
        when(supplierReadDao.loadLatest(product.getInformation().getSupplier().getId(), Locale.ENGLISH)).thenReturn(
                supplier);
        when(membershipRenewalTypeHelper.hasProvisionalRenewalOrDowngrade(active, provisional)).thenReturn(true);
        when(productSummaryReadDao.loadByMembershipId(586L)).thenReturn(activeSummary);
        when(productSummaryReadDao.loadByMembershipId(587L)).thenReturn(provisionalSummary);

        publisher.heat(21L);

        verifyZeroInteractions(productPublicationWriteDao);
    }

    @Test
    public void coolInvokesWriteDao() {
        publisher.cool(42L);

        verify(productPublicationWriteDao).insertHot(42L, false);
        verifyZeroInteractions(productSummaryReadDao, supplierReadDao);
    }

    @Test
    public void addHotProductOverrideInvokesWriteDao() {
        publisher.addHotProductOverride(42L);

        verify(hotProductOverrideWriteDao).addHotProductOverride(42L);
        verifyZeroInteractions(productSummaryReadDao, supplierReadDao, productPublicationWriteDao);
    }

    @Test
    public void removeHotProductOverrideInvokesWriteDao() {
        publisher.removeHotProductOverride(42L);

        verify(hotProductOverrideWriteDao).removeHotProductOverride(42L);
        verifyZeroInteractions(productSummaryReadDao, supplierReadDao, productPublicationWriteDao);
    }

}
