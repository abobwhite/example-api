package com.daugherty.e2c.domain;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * Represents a means by which Products can be grouped.
 */
public class ProductCategory extends Entity {

    private static final long serialVersionUID = 1L;

    public static final String NAME_SERIAL_PROPERTY = "name";
    public static final String LINK_SERIAL_PROPERTY = "link";
    public static final String TYPE_SERIAL_PROPERTY = "type";
    public static final String PRODUCT_COUNT_SERIAL_PROPERTY = "productCount";
    public static final String VISIBLE_ON_HOME_SCREEN_SERIAL_PROPERTY = "isVisibleOnHomeScreen";

    private String name;
    private String link;
    private Boolean visibleOnHomeScreen;
    private CategoryType categoryType;
    private Long productSnapshotId;
    private Integer productCount;
    private final List<ProductCategory> children = Lists.newArrayList();

    public ProductCategory(Long id) {
        super(id);
    }

    public ProductCategory(Long id, String name, Integer productCount, String link, Boolean visibleOnHomeScreen,
            CategoryType categoryType) {
        super(id);
        this.name = name;
        this.productCount = productCount;
        this.link = link;
        this.visibleOnHomeScreen = visibleOnHomeScreen;
        this.categoryType = categoryType;
    }

    public ProductCategory(Long id, String name, Long productSnapshotId, String link, Boolean visibleOnHomeScreen,
            CategoryType categoryType) {
        super(id);
        this.name = name;
        this.productSnapshotId = productSnapshotId;
        this.link = link;
        this.visibleOnHomeScreen = visibleOnHomeScreen;
        this.categoryType = categoryType;
    }

    public String getName() {
        return name;
    }

    public Long getProductSnapshotId() {
        return productSnapshotId;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public String getLink() {
        return link;
    }

    public Boolean isVisibleOnHomeScreen() {
        return visibleOnHomeScreen;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void resetProductCount() {
        productCount = 0;
    }

    public void addToProductCount(int additionalProductCount) {
        if (productCount == null) {
            productCount = additionalProductCount;
        } else {
            productCount += additionalProductCount;
        }
    }

    public List<ProductCategory> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public void addChild(ProductCategory child) {
        if (!children.contains(child)) {
            children.add(child);
        }
    }

    public void removeAllChildrenFromIndex(int index) {
        if (index >= 0 && index <= children.size()) {
            children.retainAll(children.subList(0, index));
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        ProductCategory rhs = (ProductCategory) obj;
        builder.append(name, rhs.name);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 21;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(name);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("name", name).append("productCount", productCount);
    }

}
