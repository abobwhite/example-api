package com.daugherty.e2c.business.mutator;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ProfileTranslation;
import com.daugherty.e2c.persistence.data.ProfileTranslationReadDao;
import com.daugherty.e2c.persistence.data.ProfileTranslationWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class ProfileTranslationMutatorTest {

    @Mock
    private ProfileTranslationReadDao profileTranslationReadDao;
    @Mock
    private ProfileTranslationWriteDao profileTranslationWriteDao;

    @InjectMocks
    private final ProfileTranslationMutator profileTranslationMutator = new ProfileTranslationMutator();

    @Test
    public void updateWhenTranslationDoesNotExistForLatestSupplierInvokesDaoInsert() {
        ProfileTranslation translation = new ProfileTranslation(42L, "jKNz4P4q", 586L, "companyDescription",
                "companyDescriptionTranslation");
        when(profileTranslationReadDao.getUpdateIdForLatestSupplier(translation.getId())).thenReturn(null);

        profileTranslationMutator.update(translation);

        verify(profileTranslationWriteDao).insert(translation);
    }

    @Test
    public void updateWhenTranslationExistsForLatestSupplierInvokesDaoUpdate() {
        ProfileTranslation translation = new ProfileTranslation(42L, "jKNz4P4q", 586L, "companyDescription",
                "companyDescriptionTranslation");
        when(profileTranslationReadDao.getUpdateIdForLatestSupplier(translation.getId())).thenReturn(666L);

        profileTranslationMutator.update(translation);

        verify(profileTranslationWriteDao).update(666L, translation);
    }
}
