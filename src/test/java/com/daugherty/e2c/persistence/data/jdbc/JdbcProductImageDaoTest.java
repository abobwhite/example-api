package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ProductImage;
import com.google.common.collect.Lists;

public class JdbcProductImageDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcProductImageDao productImageDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("supplier-approval.sql", "product-image.sql");
    }

    @Test
    public void loadBySnapshotIdReturnsProductImages() throws Exception {
        List<ProductImage> productImages = productImageDao.loadBySnapshotIds(Lists.newArrayList(52L));

        assertThat(productImages, is(notNullValue()));
        assertThat(productImages.size(), is(3));
        assertThat(productImages.get(0).getProductImageLink(), is("Product Image Link 1"));
        assertThat(productImages.get(1).getProductImageLink(), is("Product Image Link 2"));
        assertThat(productImages.get(2).getProductImageLink(), is("Product Image Link 3"));

        assertTrue(productImages.get(0).isPrimary());
        assertFalse(productImages.get(1).isPrimary());
        assertFalse(productImages.get(2).isPrimary());
    }

    @Test
    public void updateImagesInsertsIntoAssociationTable() {
        productImageDao.updateImages(123456789L, Lists.newArrayList(new ProductImage("productImageLink", true)));

        Map<String, Object> productAuditImageLinkRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_audit_image_link WHERE product_audit_id = ?", 123456789L);
        assertThat((Long) productAuditImageLinkRowMap.get("product_audit_id"), is(123456789L));
        assertThat(productAuditImageLinkRowMap.get("product_image_link").toString(), is("productImageLink"));
        assertThat((Boolean) productAuditImageLinkRowMap.get("is_primary"), is(true));
        assertThat((Date) productAuditImageLinkRowMap.get("last_modified_date"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }

}
