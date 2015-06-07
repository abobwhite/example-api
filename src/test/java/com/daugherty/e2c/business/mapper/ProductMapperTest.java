package com.daugherty.e2c.business.mapper;

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

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonProduct;
import com.daugherty.e2c.service.json.JsonProductImage;
import com.daugherty.e2c.service.json.JsonSupplier;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ProductMapperTest {
    @Mock
    private Hashids hashids;
    @Mock
    private ProductImageMapper productImageMapper;
    
    @InjectMocks
    private ProductMapper productMapper = new ProductMapper();
    
    @Test
    public void transformNewTransferObjectPopulatesSubsetOfFields() throws Exception {
        JsonSupplier jsonSupplier = new JsonSupplier();
        jsonSupplier.setId("jKNzKB04");

        JsonProduct jsonProduct = new JsonProduct();
        jsonProduct.setSupplier(jsonSupplier);
        jsonProduct.setName("name");
        jsonProduct.setDescription("description");
        jsonProduct.setPaymentTerms("paymentTerms");
        jsonProduct.setMinimumOrder("mimimumOrder");
        jsonProduct.setFreightOnBoardPort("freightOnBoardPort");
        jsonProduct.setFreightOnBoardPrice("1.23");
        jsonProduct.setCountry("country");
        jsonProduct.setLeadTime("leadTime");
        jsonProduct.setModel("model");
        jsonProduct.setSpecifications("specifications");
        jsonProduct.setCategories(Lists.newArrayList(1L, 2L));
        
        when(hashids.decode(jsonSupplier.getId())).thenReturn(new long[]{586L});

        Product entityProduct = productMapper.toNewDomainObject(jsonProduct);

        assertThat(entityProduct.getInformation().getSupplier().getPublicId(), is(jsonProduct.getSupplier().getId()));
        assertThat(entityProduct.getInformation().getName(), is(jsonProduct.getName()));
        assertThat(entityProduct.getInformation().getDescription(), is(jsonProduct.getDescription()));
        assertThat(entityProduct.getInformation().getPaymentTerms(), is(jsonProduct.getPaymentTerms()));
        assertThat(entityProduct.getInformation().getMinimumOrder(), is(jsonProduct.getMinimumOrder()));
        assertThat(entityProduct.getInformation().getFreightOnBoardPort(), is(jsonProduct.getFreightOnBoardPort()));
        assertThat(entityProduct.getInformation().getFreightOnBoardPrice(), is(jsonProduct.getFreightOnBoardPrice()));
        assertThat(entityProduct.getInformation().getCountry(), is(jsonProduct.getCountry()));
        assertThat(entityProduct.getInformation().getLeadTime(), is(jsonProduct.getLeadTime()));
        assertThat(entityProduct.getInformation().getModelNumber(), is(jsonProduct.getModel()));
        assertThat(entityProduct.getInformation().getSpecifications(), is(jsonProduct.getSpecifications()));

        assertThat(entityProduct.getMetadata().getCategories().size(), is(2));
        assertThat(entityProduct.getMetadata().getCategories().get(0).getId(), is(1L));
        assertThat(entityProduct.getMetadata().getCategories().get(1).getId(), is(2L));

        assertThat(entityProduct.getImages().size(), is(0));
    }

    @Test
    public void transformExistingTransferObjectPopulatesAllFields() throws Exception {
        JsonSupplier jsonSupplier = new JsonSupplier();
        jsonSupplier.setId("jKNzKB04");

        JsonProduct jsonProduct = new JsonProduct();
        jsonProduct.setId(42L);
        jsonProduct.setSupplier(jsonSupplier);
        jsonProduct.setName("name");
        jsonProduct.setChineseName("chineseName");
        jsonProduct.setEnglishName("englishName");
        jsonProduct.setDescription("description");
        jsonProduct.setPaymentTerms("paymentTerms");
        jsonProduct.setMinimumOrder("mimimumOrder");
        jsonProduct.setFreightOnBoardPort("freightOnBoardPort");
        jsonProduct.setFreightOnBoardPrice("1.23");
        jsonProduct.setCountry("country");
        jsonProduct.setLeadTime("leadTime");
        jsonProduct.setModel("model");
        jsonProduct.setSpecifications("specifications");
        jsonProduct.setMetaTags("metaTags");
        jsonProduct.setKeywords("keywords");
        jsonProduct.setPublished(true);
        jsonProduct.setHotProduct(true);
        jsonProduct.setApprovalStatus(ApprovalStatus.APPROVED.getName());
        jsonProduct.setVersion(13);
        jsonProduct.setCategories(Lists.newArrayList(1L, 2L));
        JsonProductImage jsonProductImage = new JsonProductImage();
        jsonProductImage.setImageUrl("productImageUrl");
        jsonProductImage.setPrimary(true);
        jsonProduct.setImages(Lists.newArrayList(jsonProductImage));
        jsonProduct.setPublicationDate(new Date());
        
        ProductImage productImage = new ProductImage("productImageLink", true);

        when(productImageMapper.toDomainObject(jsonProductImage, Locale.ENGLISH)).thenReturn(productImage);
        when(hashids.decode(jsonSupplier.getId())).thenReturn(new long[]{586L});
        
        Product entityProduct = productMapper.toExistingDomainObject(42L, jsonProduct, Locale.ENGLISH);

        assertThat(entityProduct.getId(), is(jsonProduct.getId()));
        assertThat(entityProduct.getInformation().getSupplier().getPublicId(), is(jsonProduct.getSupplier().getId()));
        assertThat(entityProduct.getInformation().getName(), is(jsonProduct.getName()));
        assertThat(entityProduct.getInformation().getDescription(), is(jsonProduct.getDescription()));
        assertThat(entityProduct.getInformation().getPaymentTerms(), is(jsonProduct.getPaymentTerms()));
        assertThat(entityProduct.getInformation().getMinimumOrder(), is(jsonProduct.getMinimumOrder()));
        assertThat(entityProduct.getInformation().getFreightOnBoardPort(), is(jsonProduct.getFreightOnBoardPort()));
        assertThat(entityProduct.getInformation().getFreightOnBoardPrice(), is(jsonProduct.getFreightOnBoardPrice()));
        assertThat(entityProduct.getInformation().getCountry(), is(jsonProduct.getCountry()));
        assertThat(entityProduct.getInformation().getLeadTime(), is(jsonProduct.getLeadTime()));
        assertThat(entityProduct.getInformation().getModelNumber(), is(jsonProduct.getModel()));
        assertThat(entityProduct.getInformation().getSpecifications(), is(jsonProduct.getSpecifications()));
        assertThat(entityProduct.getMetadata().getChineseName(), is(jsonProduct.getChineseName()));
        assertThat(entityProduct.getMetadata().getEnglishName(), is(jsonProduct.getEnglishName()));
        assertThat(entityProduct.getMetadata().getMetaTags(), is(jsonProduct.getMetaTags()));
        assertThat(entityProduct.getMetadata().getKeywords(), is(jsonProduct.getKeywords()));
        assertThat(entityProduct.getMetadata().isPublished(), is(jsonProduct.isPublished()));
        assertThat(entityProduct.getMetadata().isHot(), is(jsonProduct.isHotProduct()));
        assertThat(entityProduct.getApprovalStatus().getName(), is(jsonProduct.getApprovalStatus()));
        assertThat(entityProduct.getVersion(), is(jsonProduct.getVersion()));

        assertThat(entityProduct.getMetadata().getCategories().size(), is(2));
        assertThat(entityProduct.getMetadata().getCategories().get(0).getId(), is(1L));
        assertThat(entityProduct.getMetadata().getCategories().get(1).getId(), is(2L));

        assertThat(entityProduct.getImages().size(), is(1));
        assertThat(entityProduct.getImages().get(0).getProductImageLink(), is("productImageLink"));
        assertThat(entityProduct.getImages().get(0).isPrimary(), is(jsonProductImage.isPrimary()));
    }
}
