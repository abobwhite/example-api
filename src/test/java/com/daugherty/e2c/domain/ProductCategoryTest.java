package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProductCategoryTest {

    @Test
    public void addToProductCountWhenObjectHasProductCountAlready() throws Exception {
        ProductCategory category = new ProductCategory(1L, "category", 1, null, null, null);
        category.addToProductCount(2);
        assertThat(category.getProductCount(), is(3));
    }

    @Test
    public void addToProductCountWhenObjectDoesNotHaveProductCountAlready() throws Exception {
        ProductCategory category = new ProductCategory(1L, "category", (Integer) null, null, null, null);
        category.addToProductCount(2);
        assertThat(category.getProductCount(), is(2));
    }

    @Test
    public void childrenCollectionMustExistAfterConstruction() {
        ProductCategory category = new ProductCategory(null, null, 0, null, null, null);
        assertThat(category.getChildren(), is(notNullValue()));
    }

    @Test
    public void addChildAddsToChildListAndAddsToProductCount() throws Exception {
        ProductCategory parent = new ProductCategory(1L, "parent", 1, "parentLink", true, null);
        ProductCategory child = new ProductCategory(2L, "child", 2, null, null, null);

        parent.addChild(child);

        assertThat(parent.getChildren().size(), is(1));
        assertThat(parent.getChildren().get(0), is(child));
        assertThat(parent.getProductCount(), is(1));
        assertThat(parent.isLeaf(), is(false));

        assertThat(child.getChildren().size(), is(0));
        assertThat(child.getProductCount(), is(2));
        assertThat(child.isLeaf(), is(true));
    }

    @Test
    public void addChildAddsToChildListEvenIfProductCountIsNull() throws Exception {
        ProductCategory parent = new ProductCategory(1L);
        ProductCategory child = new ProductCategory(2L);

        parent.addChild(child);

        assertThat(parent.getChildren().size(), is(1));
        assertThat(parent.getChildren().get(0), is(child));
        assertThat(parent.getProductCount(), is(nullValue()));
        assertThat(parent.isLeaf(), is(false));

        assertThat(child.getChildren().size(), is(0));
        assertThat(child.getProductCount(), is(nullValue()));
    }

    @Test
    public void addChildSecondTimeIsIgnored() throws Exception {
        ProductCategory parent = new ProductCategory(1L, "parent", 1, "parentLink", null, null);
        ProductCategory child = new ProductCategory(2L, "child", 2, null, null, null);

        parent.addChild(child);
        parent.addChild(child);

        assertThat(parent.getChildren().size(), is(1));
        assertThat(parent.getChildren().get(0), is(child));
        assertThat(parent.getProductCount(), is(1));
        assertThat(parent.isLeaf(), is(false));

        assertThat(child.getChildren().size(), is(0));
        assertThat(child.getProductCount(), is(2));
    }

    @Test
    public void removeAllChildrenFromIndex0ClearsChildren() throws Exception {
        ProductCategory parent = new ProductCategory(1L, "parent", 1, "parentLink", null, null);
        parent.addChild(new ProductCategory(2L, "first child", 2, null, null, null));
        parent.addChild(new ProductCategory(3L, "second child", 3, null, null, null));
        parent.addChild(new ProductCategory(4L, "third child", 4, null, null, null));

        parent.removeAllChildrenFromIndex(0);

        assertThat(parent.getChildren().isEmpty(), is(true));
        assertThat(parent.getProductCount(), is(1));
        assertThat(parent.isLeaf(), is(true));
    }

    @Test
    public void removeAllChildrenFromIndexMidwayThroughChildListRemovesThatChildAndSubsequentChildren()
            throws Exception {
        ProductCategory parent = new ProductCategory(1L, "parent", 1, "parentLink", null, null);
        ProductCategory firstChild = new ProductCategory(2L, "first child", 2, null, null, null);
        parent.addChild(firstChild);
        parent.addChild(new ProductCategory(3L, "second child", 3, null, null, null));
        parent.addChild(new ProductCategory(4L, "third child", 4, null, null, null));

        parent.removeAllChildrenFromIndex(1);

        assertThat(parent.getChildren().size(), is(1));
        assertThat(parent.getChildren().get(0), is(firstChild));
        assertThat(parent.getProductCount(), is(1));
        assertThat(parent.isLeaf(), is(false));
    }

    @Test
    public void removeAllChildrenFromNegativeIndexDoesNothing() throws Exception {
        ProductCategory parent = new ProductCategory(1L, "parent", 1, "parentLink", null, null);
        parent.addChild(new ProductCategory(2L, "first child", 2, null, null, null));
        parent.addChild(new ProductCategory(3L, "second child", 3, null, null, null));
        parent.addChild(new ProductCategory(4L, "third child", 4, null, null, null));

        parent.removeAllChildrenFromIndex(-1);

        assertThat(parent.getChildren().size(), is(3));
        assertThat(parent.getProductCount(), is(1));
        assertThat(parent.isLeaf(), is(false));
    }

    @Test
    public void removeAllChildrenFromIndexEqualToListSizeDoesNothing() throws Exception {
        ProductCategory parent = new ProductCategory(1L, "parent", 1, "parentLink", null, null);
        parent.addChild(new ProductCategory(2L, "first child", 2, null, null, null));
        parent.addChild(new ProductCategory(3L, "second child", 3, null, null, null));
        parent.addChild(new ProductCategory(4L, "third child", 4, null, null, null));

        parent.removeAllChildrenFromIndex(3);

        assertThat(parent.getChildren().size(), is(3));
        assertThat(parent.getProductCount(), is(1));
        assertThat(parent.isLeaf(), is(false));
    }

    @Test
    public void removeAllChildrenFromIndexGreaterThanListSizeDoesNothing() throws Exception {
        ProductCategory parent = new ProductCategory(1L, "parent", 1, "parentLink", null, null);
        parent.addChild(new ProductCategory(2L, "first child", 2, null, null, null));
        parent.addChild(new ProductCategory(3L, "second child", 3, null, null, null));
        parent.addChild(new ProductCategory(4L, "third child", 4, null, null, null));

        parent.removeAllChildrenFromIndex(4);

        assertThat(parent.getChildren().size(), is(3));
        assertThat(parent.getProductCount(), is(1));
        assertThat(parent.isLeaf(), is(false));
    }

    @Test
    public void isHomeScreenVisible() throws Exception {
        ProductCategory parent = new ProductCategory(1L, "parent", 1, "parentLink", true, null);
        parent.addChild(new ProductCategory(2L, "first child", 2, null, false, null));
        parent.addChild(new ProductCategory(3L, "second child", 3, null, false, null));
        parent.addChild(new ProductCategory(4L, "third child", 4, null, false, null));

        parent.removeAllChildrenFromIndex(4);

        assertTrue(parent.isVisibleOnHomeScreen());
        assertThat(parent.getChildren().size(), is(3));
        assertThat(parent.getProductCount(), is(1));
        assertThat(parent.isLeaf(), is(false));
    }

}
