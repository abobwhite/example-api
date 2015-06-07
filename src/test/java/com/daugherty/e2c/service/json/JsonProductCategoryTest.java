package com.daugherty.e2c.service.json;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.daugherty.e2c.domain.CategoryType;
import com.daugherty.e2c.domain.ProductCategory;

public class JsonProductCategoryTest {

    @Test
    public void constructingWithRootAndLeafObjectPopulatesSimpleFieldsOnly() {
        ProductCategory entityCategory = new ProductCategory(1L, "rootAndLeaf", 0, "link", true, CategoryType.PRODUCT);

        JsonProductCategory jsonCategory = new JsonProductCategory(entityCategory);

        assertJsonProductCategorySimpleFieldsMatchExpectedValues(jsonCategory, entityCategory);
        assertThat(jsonCategory.getChildren().size(), is(0));
    }

    @Test
    public void constructingWithLeafObjectPopulatesSimpleFieldsOnly() {
        ProductCategory entityLeafCategory = new ProductCategory(1L, "leaf", 0, null, null, null);
        ProductCategory entityRootCategory = new ProductCategory(2L, "root", 0, "link", null, CategoryType.PRODUCT);
        entityRootCategory.addChild(entityLeafCategory);

        JsonProductCategory jsonLeafCategory = new JsonProductCategory(entityLeafCategory);

        assertJsonProductCategorySimpleFieldsMatchExpectedValues(jsonLeafCategory, entityLeafCategory);
        assertThat(jsonLeafCategory.getChildren().size(), is(0));
    }

    @Test
    public void constructingWithObjectHavingChildrenPopulatesAllFields() {
        ProductCategory entityBranchCategory = new ProductCategory(1L, "branch", 0, null, null, null);
        ProductCategory entityRootCategory = new ProductCategory(2L, "root", 0, "link", null, null);
        entityRootCategory.addChild(entityBranchCategory);
        ProductCategory entityLeaf3Category = new ProductCategory(3L, "leaf3", 0, null, null, null);
        entityBranchCategory.addChild(entityLeaf3Category);
        ProductCategory entityChildCategory = new ProductCategory(4L, "child", 0, null, null, null);
        entityBranchCategory.addChild(entityChildCategory);
        ProductCategory entityLeaf5Category = new ProductCategory(5L, "leaf5", 0, null, null, null);
        entityChildCategory.addChild(entityLeaf5Category);

        JsonProductCategory jsonBranchCategory = new JsonProductCategory(entityBranchCategory);

        assertJsonProductCategorySimpleFieldsMatchExpectedValues(jsonBranchCategory, entityBranchCategory);
        assertThat(jsonBranchCategory.getChildren().size(), is(2));

        JsonProductCategory jsonLeaf3Category = jsonBranchCategory.getChildren().get(0);
        assertJsonProductCategorySimpleFieldsMatchExpectedValues(jsonLeaf3Category, entityLeaf3Category);
        assertThat(jsonLeaf3Category.getChildren().size(), is(0));

        JsonProductCategory jsonChildCategory = jsonBranchCategory.getChildren().get(1);
        assertJsonProductCategorySimpleFieldsMatchExpectedValues(jsonChildCategory, entityChildCategory);
        assertThat(jsonChildCategory.getChildren().size(), is(1));

        JsonProductCategory jsonLeaf5Category = jsonChildCategory.getChildren().get(0);
        assertJsonProductCategorySimpleFieldsMatchExpectedValues(jsonLeaf5Category, entityLeaf5Category);
        assertThat(jsonLeaf5Category.getChildren().size(), is(0));

    }

    private void assertJsonProductCategorySimpleFieldsMatchExpectedValues(JsonProductCategory jsonCategory,
            ProductCategory entityCategory) {
        assertThat(jsonCategory.getId(), is(entityCategory.getId()));
        assertThat(jsonCategory.getName(), is(entityCategory.getName()));
        assertThat(jsonCategory.getProductCount(), is(entityCategory.getProductCount()));
    }

}
