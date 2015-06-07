package com.daugherty.e2c.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * Represents search and process metadata about a Product.
 */
public class ProductMetadata extends ValueObject {

    private static final long serialVersionUID = 1L;

    private List<ProductCategory> categories = Lists.newArrayList();
    private String chineseName;
    private String englishName;
    private String translatedName;
    private String metaTags;
    private String keywords;
    private Boolean published = Boolean.FALSE;
    private Boolean hot = Boolean.FALSE;
    private Boolean hotProductOverride = Boolean.FALSE;
    private Date publishedDate;

    /**
     * Constructor for new Product instances.
     */
    public ProductMetadata(List<ProductCategory> categories) {
        this.categories = categories;
    }

    /**
     * Constructor for existing Product instances.
     */
    public ProductMetadata(String chineseName, String englishName, String metaTags, String keywords, Boolean published,
            Boolean hot, Boolean hotProductOverride, Date publishedDate) {
        this.chineseName = chineseName;
        this.englishName = englishName;
        this.metaTags = metaTags;
        this.keywords = keywords;
        this.published = published;
        this.hot = hot;
        this.hotProductOverride = hotProductOverride;
        this.publishedDate = publishedDate;
    }

    public List<ProductCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ProductCategory> categories) {
        this.categories = categories;
    }

    public String getChineseName() {
        return chineseName;
    }

    public String getTranslatedName() {
        return translatedName;
    }

    public void setTranslatedName(String translatedName) {
        this.translatedName = translatedName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getMetaTags() {
        return metaTags;
    }

    public String getKeywords() {
        return keywords;
    }

    public Boolean isPublished() {
        return published;
    }

    public Boolean isHot() {
        return hot;
    }

    public Boolean isHotProductOverride() {
        return hotProductOverride;
    }

    public final Date getPublishedDate() {
        return publishedDate;
    }

    public void translate(ProductTranslation translation) {
        if (translation != null) {
            if (StringUtils.isNotBlank(translation.getKeyWordsTranslation())) {
                keywords = translation.getKeyWordsTranslation();
            }
            if (StringUtils.isNotBlank(translation.getMetaTagsTranslation())) {
                metaTags = translation.getMetaTagsTranslation();
            }
        }
    }

    @Override
    protected void addFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        ProductMetadata rhs = (ProductMetadata) obj;
        builder.append(categories, rhs.categories).append(chineseName, rhs.chineseName)
                .append(translatedName, rhs.translatedName).append(metaTags, rhs.metaTags)
                .append(keywords, rhs.keywords).append(published, rhs.published).append(hot, rhs.hot);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 1005;
    }

    @Override
    protected void addFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(categories).append(chineseName).append(translatedName).append(metaTags).append(keywords)
                .append(published).append(hot);
    }

    @Override
    protected void addFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("categories", categories).append("chineseName", chineseName)
                .append("translatedName", translatedName).append("metaTags", metaTags).append("keywords", keywords)
                .append("published", published).append("hot", hot);
    }

}
