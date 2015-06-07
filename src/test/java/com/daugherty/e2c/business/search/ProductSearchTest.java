package com.daugherty.e2c.business.search;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.search.relevance.ProductSearchResultRelevanceCalculator;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.domain.ProductSearchResult;
import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;
import com.daugherty.e2c.persistence.data.ProductCategoryReadDao;
import com.daugherty.e2c.persistence.data.ProductSearchReadDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class ProductSearchTest {
    private final ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Olive Oil", "", "", 0, 1);
    private final Product product = new Product(1L, new ProductInformation(586L, "name", "description", "paymentTerms",
            "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
            new ProductMetadata(null, null, "metaTags", "keywords", true, true, false, new Date()), 666L,
            ApprovalStatus.APPROVED, 13, 421L);
    private final ProductCategory productCategory = new ProductCategory(1L);
    private final ProductImage productImage = new ProductImage("productImageLink", true);
    private final Map<Long, Product> productIdMap = Maps.newHashMap();

    @Mock
    private ProductSearchReadDao productSearchReadDao;
    @Mock
    private ApplicationParameterReadDao applicationParameterReadDao;
    @Mock
    private ProductCategoryReadDao productCategoryReadDao;
    @Mock
    private ProductSearchResultRelevanceCalculator productSearchResultRelevanceCalculator;
    @Mock
    private MembershipLevelPrioritizer membershipLevelPrioritizer;
    @Mock
    private Map<String, String> applicationParameters;

    @InjectMocks
    private final ProductSearch productSearch = new ProductSearch();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        product.getMetadata().setCategories(Lists.newArrayList(productCategory));
        product.setImages(Lists.newArrayList(productImage));
        productIdMap.put(1L, product);

        when(applicationParameterReadDao.loadAllValues()).thenReturn(applicationParameters);
        when(applicationParameters.get(ApplicationParameterReadDao.NAME_ATTRIBUTE_RELEVANCE_WEIGHT)).thenReturn("5");
        when(applicationParameters.get(ApplicationParameterReadDao.KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT)).thenReturn("5");
        when(applicationParameters.get(ApplicationParameterReadDao.CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("7");
        when(applicationParameters.get(ApplicationParameterReadDao.DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT)).thenReturn(
                "4");
        when(applicationParameters.get(ApplicationParameterReadDao.RELEVANCE_FLOOR)).thenReturn("6");
        when(applicationParameters.get(ApplicationParameterReadDao.RELEVANCE_BUCKETS)).thenReturn("4");

        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_NAME_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("5");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("5");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("7");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("4");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_RELEVANCE_FLOOR)).thenReturn("2");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_RELEVANCE_BUCKETS)).thenReturn("4");
    }

    @Test
    public void englishSearchDelegatesToProductSearchResultsStepsSplittingOnWords() {
        List<ProductSearchResult> productSearchResults = Lists.newArrayList(productSearchResult);

        when(productSearchReadDao.find(new String[] { "olive", "oil" }, "United States", null, Locale.ENGLISH))
                .thenReturn(productSearchResults);
        when(
                productSearchResultRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                        applicationParameters)).thenReturn(100);
        when(membershipLevelPrioritizer.prioritize(productSearchResults, 4, 6)).thenReturn(productSearchResults);

        List<ProductSearchResult> results = productSearch.search("Olive Oil", "United States", null, Locale.ENGLISH);

        verify(productSearchReadDao).find(new String[] { "olive", "oil" }, "United States", null, Locale.ENGLISH);
        assertThat(results.get(0).getRelevance(), is(100));
    }

    @Test
    public void chineseSearchDelegatesToProductSearchResultsStepsSplittingOnUniqueCharacters() {
        List<ProductSearchResult> productSearchResults = Lists.newArrayList(productSearchResult);

        when(productSearchReadDao.find(new String[] { "o", "l", "i", "v", "e" }, "United States", null, Locale.CHINESE))
                .thenReturn(productSearchResults);
        when(
                productSearchResultRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.CHINESE,
                        applicationParameters)).thenReturn(100);
        when(membershipLevelPrioritizer.prioritize(productSearchResults, 4, 2)).thenReturn(productSearchResults);

        List<ProductSearchResult> results = productSearch.search("Olive Oil", "United States", null, Locale.CHINESE);

        verify(productSearchReadDao).find(new String[] { "o", "l", "i", "v", "e", }, "United States", null,
                Locale.CHINESE);
        assertThat(results.get(0).getRelevance(), is(100));
    }

    @Test
    public void searchThrowsValidationErrorWhenEnglishAndLengthOne() {
        expectedException.expect(ValidationException.class);

        productSearch.search("O", "United States", null, Locale.ENGLISH);
    }
}
