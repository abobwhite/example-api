package com.daugherty.e2c.service.controller;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.ProductCategoryFilter;
import com.daugherty.e2c.business.accessor.filter.ProductFilter;
import com.daugherty.e2c.business.search.MembershipLevelPrioritizer;
import com.daugherty.e2c.business.search.ProductSearch;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.MembershipLevelAware;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonProduct;
import com.daugherty.e2c.service.json.JsonProductCategories;
import com.daugherty.e2c.service.json.JsonProductCategory;
import com.daugherty.e2c.service.json.JsonProductIds;
import com.daugherty.e2c.service.json.JsonProducts;
import com.daugherty.e2c.service.json.JsonSupplierProducts;
import com.google.common.collect.Lists;

/**
 * REST resource for Products.
 */
@Controller
public class ProductService {
    @Inject
    private Accessor<ProductCategory> productCategoryAccessor;
    @Inject
    private Accessor<Product> publishedProductAccessor;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    @Inject
    private ProductSearch productSearch;
    @Inject
    private MembershipLevelPrioritizer membershipLevelPrioritizer;

    @RequestMapping(value = "/productCategories", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonProductCategories retrieveProductCategories(@RequestParam(value = ProductCategoryFilter.LEVEL2_COUNT,
            required = false) Integer level2Count, Locale locale) {
        Filter<ProductCategory> filter = new ProductCategoryFilter(level2Count, null, locale);
        List<ProductCategory> categories = productCategoryAccessor.find(filter);

        JsonProductCategories jsonCategories = new JsonProductCategories();
        for (ProductCategory category : categories) {
            jsonCategories.add(new JsonProductCategory(category));
        }
        return jsonCategories;
    }

    @RequestMapping(value = "/suppliers/{id}/products", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonSupplierProducts retrieveSupplierProducts(@PathVariable String id, @RequestParam(
            value = ProductFilter.PRODUCT_NAME, required = false) String productName, @RequestParam(
            value = ProductFilter.STATUS, required = false) String approvalStatus, @RequestParam(
            value = ProductFilter.HOT, required = false) Boolean hotProdcuts, @RequestParam(
            value = ProductFilter.PUBLISHED, required = false) Boolean published, @RequestParam(
            value = BaseFilter.SORT_BY, required = false) String sortBy, @RequestParam(value = BaseFilter.SORT_DESC,
            defaultValue = "false") Boolean sortDesc,
            @RequestParam(value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count, Locale locale, Principal principal) {

        ProductFilter filter = new ProductFilter(null, null, hotProdcuts, null, null, null, id, productName,
                verifyApprovalStatusParameter(approvalStatus), published, sortBy, sortDesc, startItem, count, locale);

        List<Product> products = publishedProductAccessor.find(filter);

        JsonSupplierProducts jsonSupplierProducts = new JsonSupplierProducts();
        for (Product product : products) {
            jsonSupplierProducts.add(new JsonProduct(product, documentUrlFactory, locale, principal));
        }
        return jsonSupplierProducts;
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonProduct retrieveProduct(@PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") Locale locale, Principal principal) {
        Product product = publishedProductAccessor.load(id, locale);
        return new JsonProduct(product, documentUrlFactory, locale, principal);
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonProducts retrieveProducts(Locale locale, @RequestParam(value = ProductSearch.SEARCH_TERMS_PROPERTY,
            required = false) String searchTerms,
            @RequestParam(value = ProductFilter.COUNTRY, required = false) String country, @RequestParam(
                    value = ProductFilter.CATEGORY, required = false) Long categoryId, @RequestParam(
                    value = ProductFilter.HOT, required = false) Boolean hot, @RequestParam(
                    value = ProductFilter.BUSINESS_TYPE, required = false) Long businessTypeId, @RequestParam(
                    value = ProductFilter.MEMBERSHIP_LEVEL, required = false) Integer membershipLevel, @RequestParam(
                    value = ProductFilter.PRODUCT_IDS, required = false) String productsIds, @RequestParam(
                    value = BaseFilter.SORT_BY, required = false) String sortBy, @RequestParam(
                    value = BaseFilter.SORT_DESC, defaultValue = "false") Boolean sortDescending, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count, Principal principal) {
        List<Product> products = Lists.newArrayList();
        Filter<Product> filter = buildProductFilter(country, categoryId, hot, businessTypeId, membershipLevel,
                productsIds, sortBy, sortDescending, startItem, count, locale);

        if (StringUtils.isBlank(productsIds)) {
            products = membershipLevelPrioritizer.prioritize(publishedProductAccessor.find(filter),
                    MembershipLevelAware.DEFAULT_RELEVANCE_BUCKETS, MembershipLevelAware.DEFAULT_RELEVANCE_FLOOR);
        } else {
            products = publishedProductAccessor.find(filter);
        }

        return new JsonProducts(products, documentUrlFactory, locale, principal);
    }

    @RequestMapping(value = "/productIds", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonProductIds retrieveProductIds(Locale locale, @RequestParam(value = ProductSearch.SEARCH_TERMS_PROPERTY,
            required = false) String searchTerms,
            @RequestParam(value = ProductFilter.COUNTRY, required = false) String country, @RequestParam(
                    value = ProductFilter.CATEGORY, required = false) Long categoryId, @RequestParam(
                    value = ProductFilter.HOT, required = false) Boolean hot, @RequestParam(
                    value = ProductFilter.BUSINESS_TYPE, required = false) Long businessTypeId, @RequestParam(
                    value = ProductFilter.MEMBERSHIP_LEVEL, required = false) Integer membershipLevel, @RequestParam(
                    value = ProductFilter.PRODUCT_IDS, required = false) String productsIds, @RequestParam(
                    value = BaseFilter.SORT_BY, required = false) String sortBy, @RequestParam(
                    value = BaseFilter.SORT_DESC, defaultValue = "false") Boolean sortDescending, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "1000") Integer count, Principal principal) {
        Filter<Product> filter = buildProductFilter(country, categoryId, hot, businessTypeId, membershipLevel,
                productsIds, sortBy, sortDescending, startItem, count, locale);

        if (StringUtils.isNotBlank(searchTerms)) {
            return new JsonProductIds(productSearch.search(searchTerms, country,
                    ((ProductFilter) filter).getProductCategoryCriterion(ProductFilter.CATEGORY), locale));
        } else {
            return new JsonProductIds(membershipLevelPrioritizer.prioritize(publishedProductAccessor.find(filter),
                    MembershipLevelAware.DEFAULT_RELEVANCE_BUCKETS, MembershipLevelAware.DEFAULT_RELEVANCE_FLOOR));
        }
    }

    private Filter<Product> buildProductFilter(String country, final Long categoryId, Boolean hot, Long businessTypeId,
            Integer membershipLevel, String productIds, String sortBy, Boolean sortDescending, Integer startItem,
            Integer count, Locale locale) {
        if (categoryId != null) {
            ProductCategory category = findCategoryWithChildren(categoryId);
            return new ProductFilter(country, category, hot, businessTypeId, membershipLevel, productIds, null, null,
                    null, null, sortBy, sortDescending, startItem, count, locale);
        } else {
            return new ProductFilter(country, null, hot, businessTypeId, membershipLevel, productIds, null, null, null,
                    null, sortBy, sortDescending, startItem, count, locale);
        }
    }

    private ProductCategory findCategoryWithChildren(Long categoryId) {
        ProductCategoryFilter filter = new ProductCategoryFilter(null, categoryId, null);
        List<ProductCategory> categories = productCategoryAccessor.find(filter);
        return categories.isEmpty() ? null : categories.get(0);
    }

    private ApprovalStatus verifyApprovalStatusParameter(String approvalStatus) {
        return StringUtils.isBlank(approvalStatus) ? null : ApprovalStatus.findByName(approvalStatus);
    }
}
