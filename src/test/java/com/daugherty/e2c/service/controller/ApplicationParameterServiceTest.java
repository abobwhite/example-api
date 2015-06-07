package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;
import com.daugherty.e2c.service.json.JsonSearchParameters;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationParameterServiceTest {
    @Mock
    private ApplicationParameterReadDao applicationParameterReadDao;
    @Mock
    private Map<String, String> applicationParameters;

    @InjectMocks
    private ApplicationParameterService applicationParameterService = new ApplicationParameterService();

    @Before
    public void setup() {
        when(applicationParameterReadDao.loadAllValues()).thenReturn(applicationParameters);
        when(applicationParameters.get(ApplicationParameterReadDao.NAME_ATTRIBUTE_RELEVANCE_WEIGHT)).thenReturn("5");
        when(applicationParameters.get(ApplicationParameterReadDao.KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT)).thenReturn("5");
        when(applicationParameters.get(ApplicationParameterReadDao.CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("7");
        when(applicationParameters.get(ApplicationParameterReadDao.DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT)).thenReturn(
                "4");
        when(applicationParameters.get(ApplicationParameterReadDao.EXACT_TERM_MATCH_RELEVANCE_DEGREE))
                .thenReturn("1.5");
        when(applicationParameters.get(ApplicationParameterReadDao.EXACT_WORD_MATCH_RELEVANCE_DEGREE)).thenReturn("2");
        when(applicationParameters.get(ApplicationParameterReadDao.PARTIAL_WORD_MATCH_RELEVANCE_DEGREE))
                .thenReturn("1");
        when(applicationParameters.get(ApplicationParameterReadDao.RELEVANCE_FLOOR)).thenReturn("6");
        when(applicationParameters.get(ApplicationParameterReadDao.RELEVANCE_BUCKETS)).thenReturn("4");

        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_NAME_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("6");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("7");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("8");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("9");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_EXACT_TERM_MATCH_RELEVANCE_DEGREE))
                .thenReturn("10.5");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_EXACT_WORD_MATCH_RELEVANCE_DEGREE))
                .thenReturn("3");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_PARTIAL_WORD_MATCH_RELEVANCE_DEGREE))
                .thenReturn("2");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_RELEVANCE_FLOOR)).thenReturn("3");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_RELEVANCE_BUCKETS)).thenReturn("5");
    }

    @Test
    public void getSearchParameters() {
        JsonSearchParameters jsonSearchParameters = applicationParameterService.retrieveSearchParameters(1L);

        assertThat(jsonSearchParameters.getNameAttributeRelevanceWeight(), is(5));
        assertThat(jsonSearchParameters.getKeywordAttributeRelevanceWeight(), is(5));
        assertThat(jsonSearchParameters.getCategoryAttributeRelevanceWeight(), is(7));
        assertThat(jsonSearchParameters.getDescriptionAttributeRelevanceWeight(), is(4));
        assertThat(jsonSearchParameters.getExactSearchTermRelevanceDegree(), is(1.5));
        assertThat(jsonSearchParameters.getExactWordSearchTermRelevanceDegree(), is(2.0));
        assertThat(jsonSearchParameters.getPartialWordSearchTermRelevanceDegree(), is(1.0));

        assertThat(jsonSearchParameters.getChineseNameAttributeRelevanceWeight(), is(6));
        assertThat(jsonSearchParameters.getChineseKeywordAttributeRelevanceWeight(), is(7));
        assertThat(jsonSearchParameters.getChineseCategoryAttributeRelevanceWeight(), is(8));
        assertThat(jsonSearchParameters.getChineseDescriptionAttributeRelevanceWeight(), is(9));
        assertThat(jsonSearchParameters.getChineseExactSearchTermRelevanceDegree(), is(10.5));
        assertThat(jsonSearchParameters.getChineseExactWordSearchTermRelevanceDegree(), is(3.0));
        assertThat(jsonSearchParameters.getChinesePartialWordSearchTermRelevanceDegree(), is(2.0));
    }

}
