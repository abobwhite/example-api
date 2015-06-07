package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ProductTranslation;
import com.daugherty.e2c.persistence.data.ProductTranslationReadDao;

@RunWith(MockitoJUnitRunner.class)
public class ProductTranslationAccessorTest {

    @Mock
    private ProductTranslationReadDao productTranslationReadDao;

    @InjectMocks
    private final ProductTranslationAccessor productTranslationAccessor = new ProductTranslationAccessor();

    @Test
    public void loadDelegatesToDao() {
        long id = 42L;
        ProductTranslation translation =
                new ProductTranslation(null, null, null, null, null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null);
        when(productTranslationReadDao.load(id)).thenReturn(translation);

        assertThat(productTranslationAccessor.load(id, Locale.ENGLISH), is(translation));

        verify(productTranslationReadDao).load(id);
    }
}
