package com.daugherty.e2c.service.json;

import java.util.List;

import com.daugherty.e2c.domain.ProductCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Lists;

/**
 * Marshalls/unmarshalls a ProductCategory domain object to/from JSON.
 */
@JsonRootName("productcategory")
public class JsonProductCategory {

    @JsonProperty("id")
    private Long id;
    @JsonProperty(ProductCategory.NAME_SERIAL_PROPERTY)
    private String name;
    @JsonProperty(ProductCategory.LINK_SERIAL_PROPERTY)
    private String link;
    @JsonProperty(ProductCategory.VISIBLE_ON_HOME_SCREEN_SERIAL_PROPERTY)
    private Boolean isVisibleOnHomeScreen;
    @JsonProperty(ProductCategory.TYPE_SERIAL_PROPERTY)
    private String type;
    @JsonProperty(ProductCategory.PRODUCT_COUNT_SERIAL_PROPERTY)
    private Integer productCount;
    @JsonProperty("children")
    private List<JsonProductCategory> children;

    public JsonProductCategory(ProductCategory category) {
        id = category.getId();
        name = category.getName();
        link = category.getLink();
        isVisibleOnHomeScreen = category.isVisibleOnHomeScreen();
        productCount = category.getProductCount();

        if (category.getCategoryType() != null) {
            type = category.getCategoryType().toString();
        }

        children = Lists.newArrayList();
        for (ProductCategory child : category.getChildren()) {
            children.add(new JsonProductCategory(child));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public List<JsonProductCategory> getChildren() {
        return children;
    }

    public void setChildren(List<JsonProductCategory> children) {
        // TODO Not sure we really want to do this. Revisit in Phase 2 when we are creating/updating categories.
        this.children = children;
    }

    public Boolean getIsVisibleOnHomeScreen() {
        return isVisibleOnHomeScreen;
    }

    public void setIsVisibleOnHomeScreen(Boolean isVisibleOnHomeScreen) {
        this.isVisibleOnHomeScreen = isVisibleOnHomeScreen;
    }
}
