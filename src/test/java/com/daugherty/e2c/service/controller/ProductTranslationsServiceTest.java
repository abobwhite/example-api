package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.ProductPublisher;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.domain.ProductTranslation;
import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.service.json.JsonProductTranslation;

@RunWith(MockitoJUnitRunner.class)
public class ProductTranslationsServiceTest {
    @Mock
    private Accessor<SupplierTranslation> supplierTranslationAccessor;
    @Mock
    private ProductPublisher productPublisher;
    @Mock
    private Mutator<SupplierTranslation> supplierTranslationMutator;
    @Mock
    private Accessor<ProductTranslation> productTranslationAccessor;
    @Mock
    private Accessor<Product> latestProductAccessor;
    @Mock
    private Mutator<ProductTranslation> productTranslationMutator;
    @Mock
    private ApprovalStateTransitionVisitor approveVisitor;

    @InjectMocks
    private final ProductTranslationsService service = new ProductTranslationsService();

    @Captor
    private ArgumentCaptor<ProductTranslation> productTranslationCaptor;

    @Test
    public void approveTransalationDelegatesToProductPublisher() {
        SupplierTranslation supplierTranslation = new SupplierTranslation(1L, "1", "title", ApprovalStatus.APPROVED, 4,
                232L, SupplierTranslation.PRODUCT_TYPE, false, "lastUpdater", new Date());

        when(supplierTranslationAccessor.load(1L, Locale.ENGLISH)).thenReturn(supplierTranslation);

        service.approveTranslation(1L);

        verify(supplierTranslationMutator).update(supplierTranslation);
        verify(productPublisher).publish(1L);
    }

    @Test
    public void saveProductTranslationDelegatesToMutator() {
        JsonProductTranslation jsonTranslation = new JsonProductTranslation();
        jsonTranslation.setCountry("England");
        jsonTranslation.setFobPort("port");
        jsonTranslation.setFobPrice("123.0");
        jsonTranslation.setKeySpecification("key specification");
        jsonTranslation.setKeySpecificationsTranslation("translated key specification");
        jsonTranslation.setKeyWords("key words");
        jsonTranslation.setKeyWordsTranslations("translated key words");
        jsonTranslation.setLeadTime("lead");
        jsonTranslation.setMetaTags("meta tags");
        jsonTranslation.setMetaTagsTranslations("translated meta tags");
        jsonTranslation.setMinimumOrder("minimum");
        jsonTranslation.setModelNumber("model number");
        jsonTranslation.setPaymentTermsChinese("chinese payment");
        jsonTranslation.setPaymentTermsEnglish("payment");
        jsonTranslation.setProductName("name");
        jsonTranslation.setProductNameTranslation("translated name");
        jsonTranslation.setProductDescription("description");
        jsonTranslation.setProductDescriptionTranslation("translated description");

        Product product = new Product(1L, new ProductInformation(2L, "name", "description", "payment", "minimum",
                "port", "123", "England", "lead", "model number", "specifications"), new ProductMetadata(
                "chinese name", "english name", "meta tags", "key words", false, false, false, new Date()), 6L,
                ApprovalStatus.PENDING_TRANSLATION, 1, 2L);
        when(latestProductAccessor.load(product.getId(), Locale.CHINESE)).thenReturn(product);

        ProductTranslation asSavedTranslation = new ProductTranslation(product.getId(), product.getSnapshotId(),
                "England", "model number", "payment", "chinese payment", "minimum", "port", "123.0", "lead", "name",
                "translated name", "description", "translated description", "key specification",
                "translated key specification", "key words", "translated key words", "meta tags",
                "translated meta tags");
        when(productTranslationMutator.update(any(ProductTranslation.class))).thenReturn(asSavedTranslation);

        JsonProductTranslation savedJsonTranslation = service.saveProductTranslation(jsonTranslation, product.getId());

        assertThat(savedJsonTranslation.getCountry(), is(asSavedTranslation.getCountry()));
        assertThat(savedJsonTranslation.getFobPort(), is(asSavedTranslation.getFobPort()));
        assertThat(savedJsonTranslation.getFobPrice(), is(asSavedTranslation.getFobPrice()));
        assertThat(savedJsonTranslation.getKeySpecification(), is(asSavedTranslation.getKeySpecification()));
        assertThat(savedJsonTranslation.getKeySpecificationsTranslation(),
                is(asSavedTranslation.getKeySpecificationTranslation()));
        assertThat(savedJsonTranslation.getKeyWords(), is(asSavedTranslation.getKeyWords()));
        assertThat(savedJsonTranslation.getKeyWordsTranslations(), is(asSavedTranslation.getKeyWordsTranslation()));
        assertThat(savedJsonTranslation.getLeadTime(), is(asSavedTranslation.getLeadTime()));
        assertThat(savedJsonTranslation.getMetaTags(), is(asSavedTranslation.getMetaTags()));
        assertThat(savedJsonTranslation.getMetaTagsTranslations(), is(asSavedTranslation.getMetaTagsTranslation()));
        assertThat(savedJsonTranslation.getMinimumOrder(), is(asSavedTranslation.getMinimumOrder()));
        assertThat(savedJsonTranslation.getModelNumber(), is(asSavedTranslation.getModelNumber()));
        assertThat(savedJsonTranslation.getProductName(), is(asSavedTranslation.getProductName()));
        assertThat(savedJsonTranslation.getProductNameTranslation(), is(asSavedTranslation.getProductNameTranslation()));
        assertThat(savedJsonTranslation.getProductDescription(), is(asSavedTranslation.getProductDescription()));
        assertThat(savedJsonTranslation.getProductDescriptionTranslation(),
                is(asSavedTranslation.getProductDescriptionTranslation()));

        verify(productTranslationMutator).update(productTranslationCaptor.capture());
        ProductTranslation toBeSavedTranslation = productTranslationCaptor.getValue();
        assertThat(toBeSavedTranslation.getId(), is(product.getId()));
        assertThat(toBeSavedTranslation.getSnapshotId(), is(product.getSnapshotId()));
        assertThat(toBeSavedTranslation.getCountry(), is(jsonTranslation.getCountry()));
        assertThat(toBeSavedTranslation.getFobPort(), is(jsonTranslation.getFobPort()));
        assertThat(toBeSavedTranslation.getFobPrice(), is(jsonTranslation.getFobPrice()));
        assertThat(toBeSavedTranslation.getKeySpecification(), is(jsonTranslation.getKeySpecification()));
        assertThat(toBeSavedTranslation.getKeySpecificationTranslation(),
                is(jsonTranslation.getKeySpecificationsTranslation()));
        assertThat(toBeSavedTranslation.getKeyWords(), is(jsonTranslation.getKeyWords()));
        assertThat(toBeSavedTranslation.getKeyWordsTranslation(), is(jsonTranslation.getKeyWordsTranslations()));
        assertThat(toBeSavedTranslation.getLeadTime(), is(jsonTranslation.getLeadTime()));
        assertThat(toBeSavedTranslation.getMetaTags(), is(jsonTranslation.getMetaTags()));
        assertThat(toBeSavedTranslation.getMetaTagsTranslation(), is(jsonTranslation.getMetaTagsTranslations()));
        assertThat(toBeSavedTranslation.getMinimumOrder(), is(jsonTranslation.getMinimumOrder()));
        assertThat(toBeSavedTranslation.getModelNumber(), is(jsonTranslation.getModelNumber()));
        assertThat(toBeSavedTranslation.getProductName(), is(jsonTranslation.getProductName()));
        assertThat(toBeSavedTranslation.getProductNameTranslation(), is(jsonTranslation.getProductNameTranslation()));
        assertThat(toBeSavedTranslation.getProductDescription(), is(jsonTranslation.getProductDescription()));
        assertThat(toBeSavedTranslation.getProductDescriptionTranslation(),
                is(jsonTranslation.getProductDescriptionTranslation()));
    }

    @Test
    public void retrieveProductTranslationDelegatesToAccessor() {
        long translationId = 1L;
        ProductTranslation translation = new ProductTranslation(null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null);
        when(productTranslationAccessor.load(translationId, Locale.ENGLISH)).thenReturn(translation);

        service.retrieveProductTranslation(translationId);

        verify(productTranslationAccessor).load(translationId, Locale.ENGLISH);
    }
}
