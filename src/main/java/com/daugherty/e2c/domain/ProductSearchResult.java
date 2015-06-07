package com.daugherty.e2c.domain;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * Represents a Product Search Result
 */
public class ProductSearchResult extends Entity implements MembershipLevelAware {
    private static final long serialVersionUID = 1L;

    private Long snapshotId;
    private String name;
    private String description;
    private String keywords;
    private List<String> categories = Lists.newArrayList();
    private Integer categoryCount = 0;
    private Integer membershipLevel;
    private Integer relevance = 0;

    public ProductSearchResult() {
    }

    public ProductSearchResult(Integer relevance) {
        this.relevance = relevance;
    }

    public ProductSearchResult(Long id, Long snapshotId, String name, String description, String keywords,
            Integer categoryCount, Integer membershipLevel) {
        super(id);
        this.snapshotId = snapshotId;
        this.name = name;
        this.description = description;
        this.keywords = keywords;
        this.categoryCount = categoryCount;
        this.membershipLevel = membershipLevel;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public final String getKeywords() {
        return keywords;
    }

    public final void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public final Integer getCategoryCount() {
        return categoryCount;
    }

    public final void setCategoryCount(Integer categoryCount) {
        this.categoryCount = categoryCount;
    }

    public final Integer getMembershipLevel() {
        return membershipLevel;
    }

    public final void setMembershipLevel(Integer membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public final Integer getRelevance() {
        return relevance;
    }

    public final void setRelevance(Integer relevance) {
        this.relevance = relevance;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        ProductSearchResult rhs = (ProductSearchResult) obj;
        builder.append(snapshotId, rhs.snapshotId).append(name, rhs.name).append(description, rhs.description)
                .append(keywords, rhs.keywords).append(categoryCount, rhs.categoryCount)
                .append(membershipLevel, rhs.membershipLevel).append(relevance, rhs.relevance)
                .append(categories, rhs.categories);
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(snapshotId).append(name).append(description).append(keywords).append(categoryCount)
                .append(membershipLevel).append(relevance).append(categories);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("snapshotId", snapshotId).append("name", name).append("description", description)
                .append("keywords", keywords).append("categoryCount", categoryCount)
                .append("membershipLevel", membershipLevel).append("relevance", relevance)
                .append("categories", categories);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 13;
    }

}
