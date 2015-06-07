package com.daugherty.e2c.service.json;

import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a ProductImage domain object to/from JSON.
 */
@JsonRootName("productImage")
public class JsonProductImage {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("imageUrl")
    private String imageUrl;
    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;
    @JsonProperty("watermarkUrl")
    private String watermarkUrl;
    @JsonProperty("huijinkouThumbnailUrl")
    private String huijinkouThumbnailUrl;
    @JsonProperty("huijinkouWatermarkUrl")
    private String huijinkouWatermarkUrl;
    @JsonProperty("primary")
    private boolean primary;

    public JsonProductImage() {
    }

    public JsonProductImage(ProductImage productImage, DocumentUrlFactory urlFactory, Locale locale) {
        id = productImage.getId();

        String imageLink = productImage.getProductImageLink();
        imageUrl = urlFactory.createDocumentUrl(imageLink, locale);
        thumbnailUrl = urlFactory.createE2CDocumentThumbnailUrl(imageLink, locale);
        watermarkUrl = urlFactory.createE2CDocumentWatermarkUrl(imageLink, locale);
        huijinkouThumbnailUrl = urlFactory.createHuijinkouDocumentThumbnailUrl(imageLink, locale);
        huijinkouWatermarkUrl = urlFactory.createHuijinkouDocumentWatermarkUrl(imageLink, locale);

        primary = productImage.isPrimary();
    }

    public final Long getId() {
        return id;
    }

    public final void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getWatermarkUrl() {
        return watermarkUrl;
    }

    public void setWatermarkUrl(String watermarkUrl) {
        this.watermarkUrl = watermarkUrl;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
