package com.daugherty.e2c.business.search;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductSearchResult;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;
import com.daugherty.e2c.persistence.data.ProductCategoryReadDao;
import com.daugherty.e2c.persistence.data.ProductSearchReadDao;
import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

/**
 * Search randomly assigns relevance to products based on supplier membership level and search terms
 */
@Service("productSearch")
public class ProductSearch {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public static final String SEARCH_TERMS_PROPERTY = "searchTerms";

    static final Function<ProductSearchResult, Long> PRODUCT_SNAPSHOT_ID_FUNCTION = new Function<ProductSearchResult, Long>() {
        @Override
        public Long apply(ProductSearchResult result) {
            return result.getSnapshotId();
        }
    };

    @Inject
    private ProductSearchReadDao productSearchReadDao;
    @Inject
    private ProductCategoryReadDao productCategoryReadDao;
    @Inject
    private ApplicationParameterReadDao applicationParameterReadDao;
    @Inject
    private RelevanceCalculator productSearchResultRelevanceCalculator;
    @Inject
    private MembershipLevelPrioritizer membershipLevelPrioritizer;

    public List<ProductSearchResult> search(String terms, String country, ProductCategory productCategory, Locale locale) {
        Map<String, String> applicationParameters = applicationParameterReadDao.loadAllValues();

        String searchTerms = terms.replaceAll(SearchTokenizer.SPLIT_REGEX, " ").trim();

        validateSearchTerms(searchTerms, locale);

        LOG.debug("Start Product Search with the following search terms " + searchTerms);
        List<ProductSearchResult> productSearchResults = getProductSearchResults(searchTerms, country,
                buildListOfCategoryIdsIncludingAllDescendants(productCategory), locale, applicationParameters);
        LOG.debug("Finished Product Search with the following search terms " + searchTerms);

        return productSearchResults;
    }

    private void validateSearchTerms(String searchTerms, Locale locale) {
        if (!Locale.CHINESE.equals(locale)) {
            if (searchTerms.length() <= 1) {
                ValidationError validationError = new ValidationError();
                validationError.add(SEARCH_TERMS_PROPERTY, Validatable.PRODUCT_SEARCH_TERM_LENGTH);
                throw new ValidationException(validationError);
            }
        }
    }

    private List<ProductSearchResult> getProductSearchResults(String searchTerms, String country,
            List<Long> categoryIds, Locale locale, Map<String, String> applicationParameters) {
        String[] searchTokens = SearchTokenizer.tokenizeAndSanitizeSearchTerms(searchTerms, locale).toArray(
                new String[0]);

        List<ProductSearchResult> results = Lists.newArrayList();

        if (searchTokens.length > 0) {
            results = productSearchReadDao.find(searchTokens, country, categoryIds, locale);
        }

        List<Long> snapshotIds = Lists.transform(results, PRODUCT_SNAPSHOT_ID_FUNCTION);
        ArrayListMultimap<Long, ProductCategory> categoriesBySnapshot = mapProductCategoriesBySnapshotId(snapshotIds);

        for (ProductSearchResult result : results) {
            List<ProductCategory> categories = categoriesBySnapshot.get(result.getSnapshotId());
            for (ProductCategory category : categories) {
                result.getCategories().add(category.getName());
            }

            result.setRelevance(productSearchResultRelevanceCalculator.calculate(result, searchTerms, locale,
                    applicationParameters));
        }

        int relevanceFloor = Locale.CHINESE.equals(locale) ? Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_RELEVANCE_FLOOR)) : Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.RELEVANCE_FLOOR));

        int relevanceBuckets = Locale.CHINESE.equals(locale) ? Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_RELEVANCE_BUCKETS)) : Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.RELEVANCE_BUCKETS));

        return membershipLevelPrioritizer.prioritize(results, relevanceBuckets, relevanceFloor);
    }

    private ArrayListMultimap<Long, ProductCategory> mapProductCategoriesBySnapshotId(List<Long> snapshotIds) {
        List<ProductCategory> productCategories = productCategoryReadDao.findCategoriesBySnapshotIds(snapshotIds);
        ArrayListMultimap<Long, ProductCategory> productSnapshotToProductCategories = ArrayListMultimap.create();
        for (ProductCategory productCategory : productCategories) {
            productSnapshotToProductCategories.put(productCategory.getProductSnapshotId(), productCategory);
        }
        return productSnapshotToProductCategories;
    }

    private List<Long> buildListOfCategoryIdsIncludingAllDescendants(ProductCategory category) {
        if (category == null) {
            return null;
        }
        List<Long> categoryIds = Lists.newArrayList();
        addCategoryIdToList(category, categoryIds);
        return categoryIds;
    }

    private void addCategoryIdToList(ProductCategory category, List<Long> categoryIds) {
        categoryIds.add(category.getId());
        for (ProductCategory child : category.getChildren()) {
            addCategoryIdToList(child, categoryIds);
        }
    }
}
