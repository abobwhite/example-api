package com.daugherty.e2c.service.json;

import java.util.Map;

import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Maps;

@JsonRootName(value = "search")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonSearchParameters {

    @JsonProperty("id")
    private Long id = 1L;
    @JsonProperty("nameAttributeRelevanceWeight")
    private Integer nameAttributeRelevanceWeight;
    @JsonProperty("keywordAttributeRelevanceWeight")
    private Integer keywordAttributeRelevanceWeight;
    @JsonProperty("categoryAttributeRelevanceWeight")
    private Integer categoryAttributeRelevanceWeight;
    @JsonProperty("descriptionAttributeRelevanceWeight")
    private Integer descriptionAttributeRelevanceWeight;

    @JsonProperty("exactSearchTermRelevanceDegree")
    private Double exactSearchTermRelevanceDegree;
    @JsonProperty("exactWordSearchTermRelevanceDegree")
    private Double exactWordSearchTermRelevanceDegree;
    @JsonProperty("partialWordSearchTermRelevanceDegree")
    private Double partialWordSearchTermRelevanceDegree;

    @JsonProperty("relevanceBuckets")
    private Integer relevanceBuckets;
    @JsonProperty("relevanceFloor")
    private Integer relevanceFloor;

    @JsonProperty("chineseNameAttributeRelevanceWeight")
    private Integer chineseNameAttributeRelevanceWeight;
    @JsonProperty("chineseKeywordAttributeRelevanceWeight")
    private Integer chineseKeywordAttributeRelevanceWeight;
    @JsonProperty("chineseCategoryAttributeRelevanceWeight")
    private Integer chineseCategoryAttributeRelevanceWeight;
    @JsonProperty("chineseDescriptionAttributeRelevanceWeight")
    private Integer chineseDescriptionAttributeRelevanceWeight;

    @JsonProperty("chineseExactSearchTermRelevanceDegree")
    private Double chineseExactSearchTermRelevanceDegree;
    @JsonProperty("chineseExactWordSearchTermRelevanceDegree")
    private Double chineseExactWordSearchTermRelevanceDegree;
    @JsonProperty("chinesePartialWordSearchTermRelevanceDegree")
    private Double chinesePartialWordSearchTermRelevanceDegree;

    @JsonProperty("chineseRelevanceBuckets")
    private Integer chineseRelevanceBuckets;
    @JsonProperty("chineseRelevanceFloor")
    private Integer chineseRelevanceFloor;

    public JsonSearchParameters() {
    }

    public JsonSearchParameters(Map<String, String> applicationParameters) {

        this.nameAttributeRelevanceWeight = Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.NAME_ATTRIBUTE_RELEVANCE_WEIGHT));
        this.keywordAttributeRelevanceWeight = Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT));
        this.categoryAttributeRelevanceWeight = Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT));
        this.descriptionAttributeRelevanceWeight = Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT));
        this.exactSearchTermRelevanceDegree = Double.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.EXACT_TERM_MATCH_RELEVANCE_DEGREE));
        this.exactWordSearchTermRelevanceDegree = Double.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.EXACT_WORD_MATCH_RELEVANCE_DEGREE));
        this.partialWordSearchTermRelevanceDegree = Double.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.PARTIAL_WORD_MATCH_RELEVANCE_DEGREE));
        this.relevanceBuckets = Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.RELEVANCE_BUCKETS));
        this.relevanceFloor = Integer.valueOf(applicationParameters.get(ApplicationParameterReadDao.RELEVANCE_FLOOR));

        this.chineseNameAttributeRelevanceWeight = Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_NAME_ATTRIBUTE_RELEVANCE_WEIGHT));
        this.chineseKeywordAttributeRelevanceWeight = Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT));
        this.chineseCategoryAttributeRelevanceWeight = Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT));
        this.chineseDescriptionAttributeRelevanceWeight = Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT));
        this.chineseExactSearchTermRelevanceDegree = Double.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_EXACT_TERM_MATCH_RELEVANCE_DEGREE));
        this.chineseExactWordSearchTermRelevanceDegree = Double.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_EXACT_WORD_MATCH_RELEVANCE_DEGREE));
        this.chinesePartialWordSearchTermRelevanceDegree = Double.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_PARTIAL_WORD_MATCH_RELEVANCE_DEGREE));
        this.chineseRelevanceBuckets = Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_RELEVANCE_BUCKETS));
        this.chineseRelevanceFloor = Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_RELEVANCE_FLOOR));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNameAttributeRelevanceWeight() {
        return nameAttributeRelevanceWeight;
    }

    public void setNameAttributeRelevanceWeight(Integer nameAttributeRelevanceWeight) {
        this.nameAttributeRelevanceWeight = nameAttributeRelevanceWeight;
    }

    public Integer getKeywordAttributeRelevanceWeight() {
        return keywordAttributeRelevanceWeight;
    }

    public void setKeywordAttributeRelevanceWeight(Integer keywordAttributeRelevanceWeight) {
        this.keywordAttributeRelevanceWeight = keywordAttributeRelevanceWeight;
    }

    public Integer getCategoryAttributeRelevanceWeight() {
        return categoryAttributeRelevanceWeight;
    }

    public void setCategoryAttributeRelevanceWeight(Integer categoryAttributeRelevanceWeight) {
        this.categoryAttributeRelevanceWeight = categoryAttributeRelevanceWeight;
    }

    public Integer getDescriptionAttributeRelevanceWeight() {
        return descriptionAttributeRelevanceWeight;
    }

    public void setDescriptionAttributeRelevanceWeight(Integer descriptionAttributeRelevanceWeight) {
        this.descriptionAttributeRelevanceWeight = descriptionAttributeRelevanceWeight;
    }

    public Double getExactSearchTermRelevanceDegree() {
        return exactSearchTermRelevanceDegree;
    }

    public void setExactSearchTermRelevanceDegree(Double exactSearchTermRelevanceDegree) {
        this.exactSearchTermRelevanceDegree = exactSearchTermRelevanceDegree;
    }

    public Double getExactWordSearchTermRelevanceDegree() {
        return exactWordSearchTermRelevanceDegree;
    }

    public void setExactWordSearchTermRelevanceDegree(Double exactWordSearchTermRelevanceDegree) {
        this.exactWordSearchTermRelevanceDegree = exactWordSearchTermRelevanceDegree;
    }

    public Double getPartialWordSearchTermRelevanceDegree() {
        return partialWordSearchTermRelevanceDegree;
    }

    public void setPartialWordSearchTermRelevanceDegree(Double partialWordSearchTermRelevanceDegree) {
        this.partialWordSearchTermRelevanceDegree = partialWordSearchTermRelevanceDegree;
    }

    public Integer getRelevanceBuckets() {
        return relevanceBuckets;
    }

    public void setRelevanceBuckets(Integer relevanceBuckets) {
        this.relevanceBuckets = relevanceBuckets;
    }

    public Integer getRelevanceFloor() {
        return relevanceFloor;
    }

    public void setRelevanceFloor(Integer relevanceFloor) {
        this.relevanceFloor = relevanceFloor;
    }

    public Integer getChineseNameAttributeRelevanceWeight() {
        return chineseNameAttributeRelevanceWeight;
    }

    public void setChineseNameAttributeRelevanceWeight(Integer chineseNameAttributeRelevanceWeight) {
        this.chineseNameAttributeRelevanceWeight = chineseNameAttributeRelevanceWeight;
    }

    public Integer getChineseKeywordAttributeRelevanceWeight() {
        return chineseKeywordAttributeRelevanceWeight;
    }

    public void setChineseKeywordAttributeRelevanceWeight(Integer chineseKeywordAttributeRelevanceWeight) {
        this.chineseKeywordAttributeRelevanceWeight = chineseKeywordAttributeRelevanceWeight;
    }

    public Integer getChineseCategoryAttributeRelevanceWeight() {
        return chineseCategoryAttributeRelevanceWeight;
    }

    public void setChineseCategoryAttributeRelevanceWeight(Integer chineseCategoryAttributeRelevanceWeight) {
        this.chineseCategoryAttributeRelevanceWeight = chineseCategoryAttributeRelevanceWeight;
    }

    public Integer getChineseDescriptionAttributeRelevanceWeight() {
        return chineseDescriptionAttributeRelevanceWeight;
    }

    public void setChineseDescriptionAttributeRelevanceWeight(Integer chineseDescriptionAttributeRelevanceWeight) {
        this.chineseDescriptionAttributeRelevanceWeight = chineseDescriptionAttributeRelevanceWeight;
    }

    public Double getChineseExactSearchTermRelevanceDegree() {
        return chineseExactSearchTermRelevanceDegree;
    }

    public void setChineseExactSearchTermRelevanceDegree(Double chineseExactSearchTermRelevanceDegree) {
        this.chineseExactSearchTermRelevanceDegree = chineseExactSearchTermRelevanceDegree;
    }

    public Double getChineseExactWordSearchTermRelevanceDegree() {
        return chineseExactWordSearchTermRelevanceDegree;
    }

    public void setChineseExactWordSearchTermRelevanceDegree(Double chineseExactWordSearchTermRelevanceDegree) {
        this.chineseExactWordSearchTermRelevanceDegree = chineseExactWordSearchTermRelevanceDegree;
    }

    public Double getChinesePartialWordSearchTermRelevanceDegree() {
        return chinesePartialWordSearchTermRelevanceDegree;
    }

    public void setChinesePartialWordSearchTermRelevanceDegree(Double chinesePartialWordSearchTermRelevanceDegree) {
        this.chinesePartialWordSearchTermRelevanceDegree = chinesePartialWordSearchTermRelevanceDegree;
    }

    public Integer getChineseRelevanceBuckets() {
        return chineseRelevanceBuckets;
    }

    public void setChineseRelevanceBuckets(Integer chineseRelevanceBuckets) {
        this.chineseRelevanceBuckets = chineseRelevanceBuckets;
    }

    public Integer getChineseRelevanceFloor() {
        return chineseRelevanceFloor;
    }

    public void setChineseRelevanceFloor(Integer chineseRelevanceFloor) {
        this.chineseRelevanceFloor = chineseRelevanceFloor;
    }

    public Map<String, String> toMap() {
        Map<String, String> applicationParameterMap = Maps.newHashMap();

        applicationParameterMap.put(ApplicationParameterReadDao.NAME_ATTRIBUTE_RELEVANCE_WEIGHT,
                String.valueOf(nameAttributeRelevanceWeight));
        applicationParameterMap.put(ApplicationParameterReadDao.KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT,
                String.valueOf(keywordAttributeRelevanceWeight));
        applicationParameterMap.put(ApplicationParameterReadDao.CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT,
                String.valueOf(categoryAttributeRelevanceWeight));
        applicationParameterMap.put(ApplicationParameterReadDao.DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT,
                String.valueOf(descriptionAttributeRelevanceWeight));
        applicationParameterMap.put(ApplicationParameterReadDao.EXACT_TERM_MATCH_RELEVANCE_DEGREE,
                String.valueOf(exactSearchTermRelevanceDegree));
        applicationParameterMap.put(ApplicationParameterReadDao.EXACT_WORD_MATCH_RELEVANCE_DEGREE,
                String.valueOf(exactWordSearchTermRelevanceDegree));
        applicationParameterMap.put(ApplicationParameterReadDao.PARTIAL_WORD_MATCH_RELEVANCE_DEGREE,
                String.valueOf(partialWordSearchTermRelevanceDegree));
        applicationParameterMap.put(ApplicationParameterReadDao.RELEVANCE_BUCKETS, String.valueOf(relevanceBuckets));
        applicationParameterMap.put(ApplicationParameterReadDao.RELEVANCE_FLOOR, String.valueOf(relevanceFloor));

        applicationParameterMap.put(ApplicationParameterReadDao.CHINESE_NAME_ATTRIBUTE_RELEVANCE_WEIGHT,
                String.valueOf(chineseNameAttributeRelevanceWeight));
        applicationParameterMap.put(ApplicationParameterReadDao.CHINESE_KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT,
                String.valueOf(chineseKeywordAttributeRelevanceWeight));
        applicationParameterMap.put(ApplicationParameterReadDao.CHINESE_CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT,
                String.valueOf(chineseCategoryAttributeRelevanceWeight));
        applicationParameterMap.put(ApplicationParameterReadDao.CHINESE_DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT,
                String.valueOf(chineseDescriptionAttributeRelevanceWeight));
        applicationParameterMap.put(ApplicationParameterReadDao.CHINESE_EXACT_TERM_MATCH_RELEVANCE_DEGREE,
                String.valueOf(chineseExactSearchTermRelevanceDegree));
        applicationParameterMap.put(ApplicationParameterReadDao.CHINESE_EXACT_WORD_MATCH_RELEVANCE_DEGREE,
                String.valueOf(chineseExactWordSearchTermRelevanceDegree));
        applicationParameterMap.put(ApplicationParameterReadDao.CHINESE_PARTIAL_WORD_MATCH_RELEVANCE_DEGREE,
                String.valueOf(chinesePartialWordSearchTermRelevanceDegree));
        applicationParameterMap.put(ApplicationParameterReadDao.CHINESE_RELEVANCE_BUCKETS,
                String.valueOf(chineseRelevanceBuckets));
        applicationParameterMap.put(ApplicationParameterReadDao.CHINESE_RELEVANCE_FLOOR,
                String.valueOf(chineseRelevanceFloor));

        return applicationParameterMap;

    }

}
