package com.daugherty.e2c.business;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.ProductTranslation;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.TranslationUtils;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.e2c.persistence.data.ProductCategoryReadDao;
import com.daugherty.e2c.persistence.data.ProductImageReadDao;
import com.daugherty.e2c.persistence.data.ProductTranslationReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

/**
 * Set suppliers, images and categories on products.
 */
@Component("productCollectionHydrator")
public class ProductCollectionHydrator {

    static final Function<Product, Long> PRODUCT_SNAPSHOT_ID_FUNCTION = new Function<Product, Long>() {
        @Override
        public Long apply(Product product) {
            return product.getSnapshotId();
        }
    };
    static final Function<Product, Long> PRODUCT_SUPPLIER_ID_FUNCTION = new Function<Product, Long>() {
        @Override
        public Long apply(Product product) {
            return product.getInformation().getSupplier().getId();
        }
    };
    static final Function<Supplier, Long> SUPPLIER_SNAPSHOT_ID_FUNCTION = new Function<Supplier, Long>() {
        @Override
        public Long apply(Supplier supplier) {
            return supplier.getSnapshotId();
        }
    };

    @Inject
    private ProductImageReadDao productImageReadDao;
    @Inject
    private ProductCategoryReadDao productCategoryReadDao;
    @Inject
    private SupplierReadDao supplierReadDao;
    @Inject
    private PartyBusinessTypeReadDao businessTypeReadDao;
    @Inject
    private ProductTranslationReadDao productTranslationReadDao;

    public Map<Long, Product> hydrateApprovedMap(List<Product> products, Locale locale) {
        Map<Long, Product> productToIdMap = Maps.newHashMap();

        List<Product> hydratedProducts = hydrateAll(products, true, locale);

        for (Product product : hydratedProducts) {
            productToIdMap.put(product.getId(), product);
        }
        return productToIdMap;
    }

    public List<Product> hydrateApproved(List<Product> products, Locale locale) {
        return hydrateAll(products, true, locale);
    }

    public Product hydrateApproved(Product product, Locale locale) {
        hydrateAll(Lists.newArrayList(product), true, locale);
        return product;
    }

    public List<Product> hydrateLatest(List<Product> products, Locale locale) {
        return hydrateAll(products, false, locale);
    }

    public Product hydrateLatest(Product product, Locale locale) {
        hydrateAll(Lists.newArrayList(product), false, locale);
        return product;
    }

    private List<Product> hydrateAll(List<Product> products, Boolean approvedSupplier, Locale locale) {
        List<Long> supplierIds = Lists.transform(products, PRODUCT_SUPPLIER_ID_FUNCTION);
        List<Long> snapshotIds = Lists.transform(products, PRODUCT_SNAPSHOT_ID_FUNCTION);

        Map<Long, Supplier> supplierById = getSupplierMap(supplierIds, approvedSupplier, locale);
        Multimap<Long, ProductImage> imagesBySnapshot = getProductImagesMap(snapshotIds);
        Multimap<Long, ProductCategory> categoriesBySnapshot = getProductCategoriesMap(snapshotIds);
        Map<Long, ProductTranslation> translationsByProduct = productTranslationReadDao.findBySnapshotIds(snapshotIds,
                locale);
        for (Product product : products) {
            product.setImages(Lists.newArrayList(imagesBySnapshot.get(product.getSnapshotId())));
            product.getMetadata().setCategories(Lists.newArrayList(categoriesBySnapshot.get(product.getSnapshotId())));
            product.getInformation().setSupplier(supplierById.get(product.getInformation().getSupplier().getId()));
            product.translate(translationsByProduct.get(product.getSnapshotId()));
            product.getMetadata().setTranslatedName(
                    TranslationUtils.toPinyinIfChinese(product.getInformation().getName(), locale));
        }

        return products;
    }

    private Multimap<Long, ProductImage> getProductImagesMap(List<Long> snapshotIds) {
        List<ProductImage> productImages = productImageReadDao.loadBySnapshotIds(snapshotIds);

        Multimap<Long, ProductImage> productSnapshotToProductImages = ArrayListMultimap.create();

        for (ProductImage productImage : productImages) {
            productSnapshotToProductImages.put(productImage.getProductSnapshotId(), productImage);
        }
        return productSnapshotToProductImages;
    }

    private Multimap<Long, ProductCategory> getProductCategoriesMap(List<Long> snapshotIds) {
        List<ProductCategory> productCategories = productCategoryReadDao.findCategoriesBySnapshotIds(snapshotIds);

        Multimap<Long, ProductCategory> productSnapshotToProductCategories = ArrayListMultimap.create();

        for (ProductCategory productCategory : productCategories) {
            productSnapshotToProductCategories.put(productCategory.getProductSnapshotId(), productCategory);
        }
        return productSnapshotToProductCategories;
    }

    private Map<Long, Supplier> getSupplierMap(List<Long> supplierIds, Boolean approvedSupplier, Locale locale) {
        List<Supplier> suppliers = approvedSupplier ? supplierReadDao.loadApprovedBySupplierIds(supplierIds, locale)
                : supplierReadDao.loadLatestBySupplierIds(supplierIds, locale);
        ArrayListMultimap<Long, BusinessType> businessTypeToSnapshot = businessTypeReadDao.findBySnapshotIds(Lists
                .transform(suppliers, SUPPLIER_SNAPSHOT_ID_FUNCTION));

        Map<Long, Supplier> supplierIdToSupplier = Maps.newHashMap();

        for (Supplier supplier : suppliers) {
            supplier.getCompany().setBusinessTypes(businessTypeToSnapshot.get(supplier.getSnapshotId()));
            supplierIdToSupplier.put(supplier.getId(), supplier);
        }
        return supplierIdToSupplier;
    }
}
