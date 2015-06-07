package com.daugherty.e2c.integration.business.search.relevance;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.daugherty.e2c.business.search.relevance.ProductSearchResultRelevanceCalculator;
import com.daugherty.e2c.domain.ProductSearchResult;
import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
        "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/email-context.xml", "file:src/main/webapp/WEB-INF/spring/security.xml" })
@ActiveProfiles("local")
public class ProductSearchResultRelevanceCalculatorTest {
    private Map<String, String> applicationParameters = Maps.newHashMap();
    @Inject
    private ProductSearchResultRelevanceCalculator productSearchResultRelevanceCalculator;

    @Before
    public void setUp() {
        applicationParameters.put(ApplicationParameterReadDao.NAME_ATTRIBUTE_RELEVANCE_WEIGHT, "5");
        applicationParameters.put(ApplicationParameterReadDao.KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT, "5");
        applicationParameters.put(ApplicationParameterReadDao.DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT, "4");
        applicationParameters.put(ApplicationParameterReadDao.CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT, "7");
        applicationParameters.put(ApplicationParameterReadDao.EXACT_TERM_MATCH_RELEVANCE_DEGREE, "1.5");
        applicationParameters.put(ApplicationParameterReadDao.EXACT_WORD_MATCH_RELEVANCE_DEGREE, "2");
        applicationParameters.put(ApplicationParameterReadDao.PARTIAL_WORD_MATCH_RELEVANCE_DEGREE, "1");
    }

    @Test
    public void searchTermTest() {
        List<String> categories = Lists.newArrayList("Automotive Chemicals");

        ProductSearchResult productSearchResult = new ProductSearchResult();
        productSearchResult.setName("Car Polish");
        productSearchResult
                .setDescription("Advanced Car Polish. Silverhook premium Car Polish is a highly advanced formula containing natural Carnauba Wax and a complex blend of mineral oils, cleaning agents and silicones to aid the beading of water. Carnauba Wax helps to disguise any swirl marks or micro scratches in the paint. It has a similar visual effect of wet paint, so on a well maintained painted surface the effect can be stunning, leaving an incredibly durable and protective glossy shin");
        productSearchResult.setKeywords("");
        productSearchResult.setCategories(categories);
        productSearchResult.setMembershipLevel(1);

        int score = productSearchResultRelevanceCalculator.calculate(productSearchResult, "Car", Locale.ENGLISH,
                applicationParameters);

        System.out.println(score);
    }
}
