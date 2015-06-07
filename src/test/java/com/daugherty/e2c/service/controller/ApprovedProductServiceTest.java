package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.service.json.JsonApprovedProduct;

@RunWith(MockitoJUnitRunner.class)
public class ApprovedProductServiceTest {
    @Mock
    private Accessor<Product> approvedProductAccessor;
    @Mock
    private DocumentUrlFactory documentUrlFactory;

    @InjectMocks
    private final ApprovedProductService service = new ApprovedProductService();

    @Test
    public void retrievePublishedDelegatesToPublishedProductAccessor() {
        Product product = new Product(1L, new ProductInformation(42L, "Name", "Description", "payPal", "100", "port",
                "10", "country", "10 days", "Model ZX 234", "specs"), new ProductMetadata(null, null, "metaTage",
                "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, 32L);
        when(approvedProductAccessor.load(1L, Locale.ENGLISH)).thenReturn(product);

        JsonApprovedProduct jsonProduct = service.retrieveProduct(1L, Locale.ENGLISH, null);

        verify(approvedProductAccessor).load(1L, Locale.ENGLISH);
        assertThat(jsonProduct.getId(), is(product.getId()));
    }
}
