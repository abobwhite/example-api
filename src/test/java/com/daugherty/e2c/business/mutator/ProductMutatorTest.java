package com.daugherty.e2c.business.mutator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.ProductCollectionHydrator;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.ProductCategoryWriteDao;
import com.daugherty.e2c.persistence.data.ProductImageWriteDao;
import com.daugherty.e2c.persistence.data.ProductWriteDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ProductMutatorTest {

    @Mock
    private Validator<Product> productValidator;
    @Mock
    private ProductWriteDao productWriteDao;
    @Mock
    private ProductCategoryWriteDao productCategoryWriteDao;
    @Mock
    private ApprovalStateTransitionVisitor changeDataVisitor;
    @Mock
    private ApprovalStateTransitionVisitor submitForApprovalVisitor;
    @Mock
    private ProductImageWriteDao productImageWriteDao;
    @Mock
    protected ProductCollectionHydrator productCollectionHydrator;

    @InjectMocks
    private final ProductMutator productMutator = new ProductMutator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createValidProductUpdatesDatabaseAndSendsEmail() {
        List<ProductCategory> categories = Lists.newArrayList(new ProductCategory(1L));
        Product product = new Product(new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                categories);
        Product persistedProduct = new Product(new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                categories);
        persistedProduct.setId(42L);
        persistedProduct.setSnapshotId(242L);
        when(productWriteDao.insert(product)).thenReturn(persistedProduct);
        when(productCollectionHydrator.hydrateLatest(persistedProduct, Locale.ENGLISH)).thenReturn(persistedProduct);

        Product createdProduct = productMutator.create(product);

        assertThat(createdProduct, is(persistedProduct));
        verify(productValidator).validate(product);
        verify(productWriteDao).insert(product);
        verify(productCategoryWriteDao).updateCategories(242L, categories);
    }

    @Test
    public void createInvalidProductDoesNothingButThrowValidationException() {
        expectedException.expect(ValidationException.class);

        List<ProductCategory> categories = Lists.newArrayList(new ProductCategory(1L));
        Product product = new Product(new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                categories);
        doThrow(new ValidationException(new ValidationError())).when(productValidator).validate(product);

        productMutator.create(product);

        verify(productValidator).validate(product);
        verifyZeroInteractions(productWriteDao, productCategoryWriteDao);
    }

    @Test
    public void updateValidProductUpdatesDatabaseWhenVersion1AndNotApproved() {
        Product product = new Product(42L, new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                new ProductMetadata(null, null, "metaTags", "keywords", true, true, false, new Date()), 666L,
                ApprovalStatus.DRAFT, 1, 421L);
        product.getMetadata().setCategories(Lists.newArrayList(new ProductCategory(1L)));
        product.setImages(Lists.newArrayList(new ProductImage("productImageLink", true)));

        when(changeDataVisitor.getNewVersion(product.getVersion(), product.getApprovalStatus())).thenReturn(1);
        when(changeDataVisitor.getNewApprovalStatus(product.getApprovalStatus())).thenReturn(ApprovalStatus.DRAFT);

        Product persistedProduct = new Product(42L, new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                new ProductMetadata(null, null, "metaTags", "keywords", true, true, false, new Date()), 666L,
                ApprovalStatus.DRAFT, 1, 422L);
        when(productWriteDao.update(product)).thenReturn(persistedProduct);
        when(productCollectionHydrator.hydrateLatest(persistedProduct, Locale.ENGLISH)).thenReturn(persistedProduct);

        Product mutatedProduct = productMutator.update(product);

        assertThat(mutatedProduct, is(notNullValue()));
        assertThat(product.getVersion(), is(1));
        assertThat(product.getApprovalStatus(), is(ApprovalStatus.DRAFT));
        verify(productValidator).validate(product);
        verify(productWriteDao).update(product);
        verify(productCategoryWriteDao).updateCategories(persistedProduct.getSnapshotId(),
                product.getMetadata().getCategories());
        verify(productImageWriteDao).updateImages(persistedProduct.getSnapshotId(), product.getImages());
        verifyZeroInteractions(submitForApprovalVisitor);
    }

    @Test
    public void updateValidProductUpdatesDatabaseWhenVersion1AndApproved() {
        Product product = new Product(42L, new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                new ProductMetadata(null, null, "metaTags", "keywords", true, true, false, new Date()), 666L,
                ApprovalStatus.APPROVED, 1, 421L);
        product.getMetadata().setCategories(Lists.newArrayList(new ProductCategory(1L)));
        product.setImages(Lists.newArrayList(new ProductImage("productImageLink", true)));

        when(changeDataVisitor.getNewVersion(1, product.getApprovalStatus())).thenReturn(2);
        when(changeDataVisitor.getNewApprovalStatus(product.getApprovalStatus())).thenReturn(ApprovalStatus.DRAFT);

        when(submitForApprovalVisitor.getNewVersion(2, ApprovalStatus.DRAFT)).thenReturn(2);
        when(submitForApprovalVisitor.getNewApprovalStatus(ApprovalStatus.DRAFT)).thenReturn(
                ApprovalStatus.PENDING_APPROVAL);

        Product persistedProduct = new Product(42L, new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                new ProductMetadata(null, null, "metaTags", "keywords", true, true, false, new Date()), 666L,
                ApprovalStatus.PENDING_APPROVAL, 2, 422L);
        when(productWriteDao.update(product)).thenReturn(persistedProduct);
        when(productCollectionHydrator.hydrateLatest(persistedProduct, Locale.ENGLISH)).thenReturn(persistedProduct);

        Product mutatedProduct = productMutator.update(product);

        assertThat(mutatedProduct, is(notNullValue()));
        assertThat(product.getVersion(), is(2));
        assertThat(product.getApprovalStatus(), is(ApprovalStatus.DRAFT));
        verify(productValidator).validate(product);
        verify(productWriteDao).update(product);
        verify(productCategoryWriteDao).updateCategories(persistedProduct.getSnapshotId(),
                product.getMetadata().getCategories());
        verify(productImageWriteDao).updateImages(persistedProduct.getSnapshotId(), product.getImages());
    }
}
