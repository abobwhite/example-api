package com.daugherty.e2c.business.mutator;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ProductTranslation;
import com.daugherty.e2c.persistence.data.ProductTranslationReadDao;
import com.daugherty.e2c.persistence.data.ProductTranslationWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class ProductTranslationMutatorTest {

    @Mock
    private ProductTranslationReadDao productTranslationReadDao;
    @Mock
    private ProductTranslationWriteDao productTranslationWriteDao;

    @InjectMocks
    private final ProductTranslationMutator productTranslationMutator = new ProductTranslationMutator();

    @Test
    public void updateWhenTranslationDoesNotExistForLatestProductInvokesDaoInsert() {
        ProductTranslation translation = new ProductTranslation(42L, 586L, "paymentTermsTranslation",
                "productNameTranslation", "productDescriptionTranslation", "keySpecificationTranslation",
                "keyWordsTranslation", "metaTagsTranslation");
        when(productTranslationReadDao.getUpdateIdForLatestProduct(translation.getId())).thenReturn(null);

        productTranslationMutator.update(translation);

        verify(productTranslationWriteDao).insert(translation);
    }

    @Test
    public void updateWhenTranslationExistsForLatestProductInvokesDaoUpdate() {
        ProductTranslation translation = new ProductTranslation(42L, 586L, "paymentTermsTranslation",
                "productNameTranslation", "productDescriptionTranslation", "keySpecificationTranslation",
                "keyWordsTranslation", "metaTagsTranslation");
        when(productTranslationReadDao.getUpdateIdForLatestProduct(translation.getId())).thenReturn(666L);

        productTranslationMutator.update(translation);

        verify(productTranslationWriteDao).update(666L, translation);
    }

}
