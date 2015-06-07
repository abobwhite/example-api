package com.daugherty.e2c.service.json;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.ProductInformation;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class JsonProductsTest {
    @Mock
    private DocumentUrlFactory documentUrlFactory;

    @Test
    public void constructorAddsProducts() {
        List<ProductCategory> categories = Lists.newArrayList(new ProductCategory(1L));
        ProductImage productImage = new ProductImage("link", true);
        Product product = new Product(new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                categories);

        when(documentUrlFactory.createDocumentUrl("link", Locale.ENGLISH)).thenReturn("documentLink");

        product.getImages().add(productImage);

        JsonProducts jsonProducts = new JsonProducts(Lists.newArrayList(product), documentUrlFactory, Locale.ENGLISH,
                null);

        assertThat(jsonProducts.size(), is(1));
        assertThat(jsonProducts.get(0).getApprovalStatus(), is(ApprovalStatus.DRAFT.getName()));
        assertThat(jsonProducts.get(0).getId(), is(product.getId()));
        assertThat(jsonProducts.get(0).getName(), is(product.getInformation().getName()));
        assertThat(jsonProducts.get(0).getPaymentTerms(), is(product.getInformation().getPaymentTerms()));
        assertThat(jsonProducts.get(0).getMinimumOrder(), is(product.getInformation().getMinimumOrder()));
        assertThat(jsonProducts.get(0).getFreightOnBoardPort(), is(product.getInformation().getFreightOnBoardPort()));
        assertThat(jsonProducts.get(0).getFreightOnBoardPrice(), is(product.getInformation().getFreightOnBoardPrice()));
        assertThat(jsonProducts.get(0).getCountry(), is(product.getInformation().getCountry()));
        assertThat(jsonProducts.get(0).getLeadTime(), is(product.getInformation().getLeadTime()));
        assertThat(jsonProducts.get(0).getModel(), is(product.getInformation().getModelNumber()));
        assertThat(jsonProducts.get(0).getSpecifications(), is(product.getInformation().getSpecifications()));
        assertThat(jsonProducts.get(0).getCategories().size(), is(1));
        assertThat(jsonProducts.get(0).getCategories().get(0), is(1L));
        assertThat(jsonProducts.get(0).getImages().size(), is(1));
        assertThat(jsonProducts.get(0).getImages().get(0).getImageUrl(), is("documentLink"));
        assertThat(jsonProducts.get(0).getImages().get(0).isPrimary(), is(true));
    }
}
