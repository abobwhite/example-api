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

import com.daugherty.e2c.domain.ProfileTranslation;
import com.daugherty.e2c.persistence.data.ProfileTranslationReadDao;

@RunWith(MockitoJUnitRunner.class)
public class ProfileTranslationAccessorTest {

    @Mock
    private ProfileTranslationReadDao profileTranslationReadDao;

    @InjectMocks
    private final ProfileTranslationAccessor profileTranslationAccessor = new ProfileTranslationAccessor();

    @Test
    public void loadDelegatesToDao() {
        long id = 42L;
        ProfileTranslation profileTranslation = new ProfileTranslation(null, null, null, null, null);
        when(profileTranslationReadDao.loadByLatestSupplierId(id)).thenReturn(profileTranslation);

        assertThat(profileTranslationAccessor.load(id, Locale.ENGLISH), is(profileTranslation));

        verify(profileTranslationReadDao).loadByLatestSupplierId(id);
    }
}
