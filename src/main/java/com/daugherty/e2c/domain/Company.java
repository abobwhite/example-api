package com.daugherty.e2c.domain;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * An organization whose business includes import/export of Products into China.
 */
public class Company extends ValueObject {

    private static final long serialVersionUID = 1L;

    private final String englishName;
    private final String chineseName;
    private final String description;
    private List<BusinessType> businessTypes;
    private final String numberOfEmployees;
    private final String website;
    private final Integer yearEstablished;
    private final String totalAnnualSales;
    private final String totalImportAmount;
    private String logoRefId;
    private String videoRefId;

    public Company(String englishName, String chineseName, String description, List<BusinessType> businessTypes,
            String numberOfEmployees, String website, Integer yearEstablished, String totalAnnualSales,
            String totalImportAmount, String logoRefId, String videoRefId) {
        this.englishName = englishName;
        this.chineseName = chineseName;
        this.description = description;
        this.businessTypes = businessTypes;
        this.numberOfEmployees = numberOfEmployees;
        this.website = website;
        this.yearEstablished = yearEstablished;
        this.totalAnnualSales = totalAnnualSales;
        this.totalImportAmount = totalImportAmount;
        this.logoRefId = logoRefId;
        this.videoRefId = videoRefId;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public String getDescription() {
        return description;
    }

    public List<BusinessType> getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(List<BusinessType> businessTypes) {
        this.businessTypes = businessTypes;
    }

    public String getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public String getWebsite() {
        if (StringUtils.isBlank(website)) {
            return null;
        } else if (website.startsWith(UrlBuilder.HTTP_PREFIX)) {
            return website;
        } else {
            return UrlBuilder.HTTP_PREFIX + website;
        }
    }

    public Integer getYearEstablished() {
        return yearEstablished;
    }

    public String getTotalAnnualSales() {
        return totalAnnualSales;
    }

    public String getTotalImportAmount() {
        return totalImportAmount;
    }

    public String getLogoRefId() {
        return logoRefId;
    }

    public void setLogoRefId(String logoRefId) {
        this.logoRefId = logoRefId;
    }

    public String getVideoRefId() {
        if (videoRefId == null) {
            return videoRefId;
        } else if (hasYouTubeEmbedUrl()) {
            return videoRefId;
        } else {
            String[] videoUrl = videoRefId.split("=");
            if (videoUrl.length > 1) {
                return "https://www.youtube.com/embed/" + videoUrl[1] + "?feature=player_embedded";
            }
            return videoRefId;
        }
    }

    private boolean hasYouTubeEmbedUrl() {
        return videoRefId.contains("www.youtube.com/embed/");
    }

    public void setVideoRefId(String videoRefId) {
        this.videoRefId = videoRefId;
    }

    @Override
    protected void addFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        Company rhs = (Company) obj;
        builder.append(englishName, rhs.englishName).append(chineseName, rhs.chineseName)
                .append(description, rhs.description).append(businessTypes, rhs.businessTypes)
                .append(numberOfEmployees, rhs.numberOfEmployees).append(website, rhs.website)
                .append(yearEstablished, rhs.yearEstablished).append(totalAnnualSales, rhs.totalAnnualSales)
                .append(totalImportAmount, rhs.totalImportAmount).append(logoRefId, rhs.logoRefId)
                .append(videoRefId, rhs.videoRefId);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 1003;
    }

    @Override
    protected void addFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(englishName).append(chineseName).append(description).append(businessTypes)
                .append(numberOfEmployees).append(website).append(yearEstablished).append(totalAnnualSales)
                .append(totalImportAmount).append(logoRefId).append(videoRefId);
    }

    @Override
    protected void addFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("englishName", englishName).append("chineseName", chineseName)
                .append("description", description).append("businessTypes", businessTypes)
                .append("numberOfEmployees", numberOfEmployees).append("website", website)
                .append("established", yearEstablished).append("totalAnnualSales", totalAnnualSales)
                .append("totalImportAmount", totalImportAmount).append("logoRefId", logoRefId)
                .append("videoRefId", videoRefId);
    }
}
