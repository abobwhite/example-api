package com.daugherty.e2c.service.json;

import java.util.Map;

import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductTranslation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a ProductTranslation domain object to/from JSON.
 */
@JsonRootName(value = "productTranslation")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonProductTranslation {

    @JsonProperty("id")
    private Long id;
    @JsonProperty(ProductTranslation.COUNTRY_SERIAL_PROPERTY)
    private String country;
    @JsonProperty(ProductTranslation.MODEL_NUMBER_SERIAL_PROPERTY)
    private String modelNumber;
    @JsonProperty(ProductTranslation.PAYMENT_TERMS_ENGLISH_SERIAL_PROPERTY)
    private String paymentTermsEnglish;
    @JsonProperty(ProductTranslation.PAYMENT_TERMS_CHINESE_SERIAL_PROPERTY)
    private String paymentTermsChinese;
    @JsonProperty(ProductTranslation.MINIMUM_ORDER_SERIAL_PROPERTY)
    private String minimumOrder;
    @JsonProperty(ProductTranslation.FOB_PORT_SERIAL_PROPERTY)
    private String fobPort;
    @JsonProperty(ProductTranslation.FOB_PRICE_SERIAL_PROPERTY)
    private String fobPrice;
    @JsonProperty(ProductTranslation.LEAD_TIME_SERIAL_PROPERTY)
    private String leadTime;
    @JsonProperty(ProductTranslation.PRODUCT_NAME_SERIAL_PROPERTY)
    private String productName;
    @JsonProperty(ProductTranslation.PRODUCT_NAME_TRANSLATION_SERIAL_PROPERTY)
    private String productNameTranslation;
    @JsonProperty(ProductTranslation.PRODUCT_DESCRIPTION_SERIAL_PROPERTY)
    private String productDescription;
    @JsonProperty(ProductTranslation.PRODUCT_DESCRIPTION_TRANSLATION_SERIAL_PROPERTY)
    private String productDescriptionTranslation;
    @JsonProperty(ProductTranslation.KEY_SPECIFICATION_SERIAL_PROPERTY)
    private String keySpecification;
    @JsonProperty(ProductTranslation.KEY_SPECIFICATION_TRANSLATION_SERIAL_PROPERTY)
    private String keySpecificationsTranslation;
    @JsonProperty(ProductTranslation.KEY_WORDS_SERIAL_PROPERTY)
    private String keyWords;
    @JsonProperty(ProductTranslation.KEY_WORDS_TRANSLATION_SERIAL_PROPERTY)
    private String keyWordsTranslations;
    @JsonProperty(ProductTranslation.META_TAGS_SERIAL_PROPERTY)
    private String metaTags;
    @JsonProperty(ProductTranslation.META_TAGS_TRANSLATION_SERIAL_PROPERTY)
    private String metaTagsTranslations;
    @JsonProperty("links")
    private Map<String, String> links;
    @JsonProperty("translationType")
    public static final String type = "Product";

    public JsonProductTranslation() {
    }

    public JsonProductTranslation(ProductTranslation translation, Map<String, String> links) {
        id = translation.getId();
        country = translation.getCountry();
        modelNumber = translation.getModelNumber();
        paymentTermsEnglish = translation.getPaymentTerms();
        paymentTermsChinese = translation.getPaymentTermsTranslation();
        minimumOrder = translation.getMinimumOrder();
        fobPort = translation.getFobPort();
        fobPrice = translation.getFobPrice();
        leadTime = translation.getLeadTime();
        productName = translation.getProductName();
        productNameTranslation = translation.getProductNameTranslation();
        productDescription = translation.getProductDescription();
        productDescriptionTranslation = translation.getProductDescriptionTranslation();
        keySpecification = translation.getKeySpecification();
        keySpecificationsTranslation = translation.getKeySpecificationTranslation();
        keyWords = translation.getKeyWords();
        keyWordsTranslations = translation.getKeyWordsTranslation();
        metaTags = translation.getMetaTags();
        metaTagsTranslations = translation.getMetaTagsTranslation();
        this.links = links;
    }

    public Long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getPaymentTermsEnglish() {
        return paymentTermsEnglish;
    }

    public void setPaymentTermsEnglish(String paymentTermsEnglish) {
        this.paymentTermsEnglish = paymentTermsEnglish;
    }

    public String getPaymentTermsChinese() {
        return paymentTermsChinese;
    }

    public void setPaymentTermsChinese(String paymentTermsChinese) {
        this.paymentTermsChinese = paymentTermsChinese;
    }

    public String getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(String minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    public String getFobPort() {
        return fobPort;
    }

    public void setFobPort(String fobPort) {
        this.fobPort = fobPort;
    }

    public String getFobPrice() {
        return fobPrice;
    }

    public void setFobPrice(String fobPrice) {
        this.fobPrice = fobPrice;
    }

    public String getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(String leadTime) {
        this.leadTime = leadTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameTranslation() {
        return productNameTranslation;
    }

    public void setProductNameTranslation(String productNameTranslation) {
        this.productNameTranslation = productNameTranslation;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductDescriptionTranslation() {
        return productDescriptionTranslation;
    }

    public void setProductDescriptionTranslation(String productDescriptionTranslation) {
        this.productDescriptionTranslation = productDescriptionTranslation;
    }

    public String getKeySpecification() {
        return keySpecification;
    }

    public void setKeySpecification(String keySpecification) {
        this.keySpecification = keySpecification;
    }

    public String getKeySpecificationsTranslation() {
        return keySpecificationsTranslation;
    }

    public void setKeySpecificationsTranslation(String keySpecificationsTranslation) {
        this.keySpecificationsTranslation = keySpecificationsTranslation;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getKeyWordsTranslations() {
        return keyWordsTranslations;
    }

    public void setKeyWordsTranslations(String keyWordsTranslations) {
        this.keyWordsTranslations = keyWordsTranslations;
    }

    public String getMetaTags() {
        return metaTags;
    }

    public void setMetaTags(String metaTags) {
        this.metaTags = metaTags;
    }

    public String getMetaTagsTranslations() {
        return metaTagsTranslations;
    }

    public void setMetaTagsTranslations(String metaTagsTranslation) {
        metaTagsTranslations = metaTagsTranslation;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public static String getType() {
        return type;
    }

    public ProductTranslation toDomainObject(Product product) {
        return new ProductTranslation(product.getId(), product.getSnapshotId(), country, modelNumber,
                paymentTermsEnglish, paymentTermsChinese, minimumOrder, fobPort, fobPrice, leadTime, productName,
                productNameTranslation, productDescription, productDescriptionTranslation, keySpecification,
                keySpecificationsTranslation, keyWords, keyWordsTranslations, metaTags, metaTagsTranslations);
    }
}
