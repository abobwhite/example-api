package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.ProductCollectionHydrator;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.persistence.data.ProductReadDao;

@RunWith(MockitoJUnitRunner.class)
public class ApprovedProductAccessorTest {

    private static final long SNAPSHOT_ID = 32L;
    private final Product product = new Product(1L, new ProductInformation(42L, "Name", "Description", "payPal", "100",
            "port", "10", "country", "10 days", "Model ZX 234", "specs"), new ProductMetadata(null, null, "metaTage",
            "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, SNAPSHOT_ID);

    @Mock
    private ProductReadDao productReadDao;
    @Mock
    private ProductCollectionHydrator productCollectionHydrator;

    @InjectMocks
    private final ApprovedProductAccessor accessor = new ApprovedProductAccessor();

    @Test
    public void loadDaoWithProductIdMethod() throws Exception {
        when(productReadDao.loadApproved(1L, Locale.ENGLISH)).thenReturn(product);
        when(productCollectionHydrator.hydrateApproved(product, Locale.ENGLISH)).thenReturn(product);

        Product persistedProduct = accessor.load(1L, Locale.ENGLISH);

        assertThat(persistedProduct, is(product));
    }
}
