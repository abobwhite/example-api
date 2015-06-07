package com.daugherty.e2c.business.mutator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.persistence.data.SupplierTranslationWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class SupplierTranslationMutatorTest {

    @Mock
    private SupplierTranslationWriteDao supplierTranslationWriteDao;

    @InjectMocks
    private final SupplierTranslationMutator supplierTranslationMutator = new SupplierTranslationMutator();

    @Test
    public void updateDelegatesToDao() {
        SupplierTranslation translation = mock(SupplierTranslation.class);
        supplierTranslationMutator.update(translation);
        verify(supplierTranslationWriteDao).update(translation);
    }
}
