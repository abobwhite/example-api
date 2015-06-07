package com.daugherty.e2c.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a translation of Product information.
 */
public class ProductTranslation extends Entity {

    private static final long serialVersionUID = 1L;

    public static final String COUNTRY_SERIAL_PROPERTY = "country";
    public static final String MODEL_NUMBER_SERIAL_PROPERTY = "modelNumber";
    public static final String PAYMENT_TERMS_ENGLISH_SERIAL_PROPERTY = "paymentTermsEnglish";
    public static final String PAYMENT_TERMS_CHINESE_SERIAL_PROPERTY = "paymentTermsChinese";
    public static final String MINIMUM_ORDER_SERIAL_PROPERTY = "minimumOrder";
    public static final String FOB_PORT_SERIAL_PROPERTY = "fobPort";
    public static final String FOB_PRICE_SERIAL_PROPERTY = "fobPrice";
    public static final String LEAD_TIME_SERIAL_PROPERTY = "leadTime";
    public static final String PRODUCT_NAME_SERIAL_PROPERTY = "productName";
    public static final String PRODUCT_NAME_TRANSLATION_SERIAL_PROPERTY = "productNameTranslation";
    public static final String PRODUCT_DESCRIPTION_SERIAL_PROPERTY = "productDescription";
    public static final String PRODUCT_DESCRIPTION_TRANSLATION_SERIAL_PROPERTY = "productDescriptionTranslation";
    public static final String KEY_SPECIFICATION_SERIAL_PROPERTY = "keySpecification";
    public static final String KEY_SPECIFICATION_TRANSLATION_SERIAL_PROPERTY = "keySpecificationTranslation";
    public static final String KEY_WORDS_SERIAL_PROPERTY = "keyWords";
    public static final String KEY_WORDS_TRANSLATION_SERIAL_PROPERTY = "keyWordsTranslation";
    public static final String META_TAGS_SERIAL_PROPERTY = "metaTags";
    public static final String META_TAGS_TRANSLATION_SERIAL_PROPERTY = "metaTagsTranslation";

    private final Long snapshotId;
    private String country;
    private String modelNumber;
    private String paymentTerms;
    private final String paymentTermsTranslation;
    private String minimumOrder;
    private String fobPort;
    private String fobPrice;
    private String leadTime;

    private String productName;
    private final String productNameTranslation;
    private String productDescription;
    private final String productDescriptionTranslation;
    private String keySpecification;
    private final String keySpecificationTranslation;
    private String keyWords;
    private final String keyWordsTranslation;
    private String metaTags;
    private final String metaTagsTranslation;

    public ProductTranslation(Long id, Long snapshotId, String country, String modelNumber, String paymentTerms,
            String paymentTermsTranslation, String minimumOrder, String fobPort, String fobPrice, String leadTime,
            String productName, String productNameTranslation, String productDescription,
            String productDescriptionTranslation, String keySpecification, String keySpecificationTranslation,
            String keyWords, String keyWordsTranslation, String metaTags, String metaTagsTranslation) {
        super(id);
        this.snapshotId = snapshotId;
        this.country = country;
        this.modelNumber = modelNumber;
        this.paymentTerms = paymentTerms;
        this.paymentTermsTranslation = paymentTermsTranslation;
        this.minimumOrder = minimumOrder;
        this.fobPort = fobPort;
        this.fobPrice = fobPrice;
        this.leadTime = leadTime;
        this.productName = productName;
        this.productNameTranslation = productNameTranslation;
        this.productDescription = productDescription;
        this.productDescriptionTranslation = productDescriptionTranslation;
        this.keySpecification = keySpecification;
        this.keySpecificationTranslation = keySpecificationTranslation;
        this.keyWords = keyWords;
        this.keyWordsTranslation = keyWordsTranslation;
        this.metaTags = metaTags;
        this.metaTagsTranslation = metaTagsTranslation;
    }

    public ProductTranslation(Long id, Long snapshotId, String paymentTermsTranslation, String productNameTranslation,
            String productDescriptionTranslation, String keySpecificationTranslation, String keyWordsTranslation,
            String metaTagsTranslation) {
        super(id);
        this.snapshotId = snapshotId;
        this.paymentTermsTranslation = paymentTermsTranslation;
        this.productNameTranslation = productNameTranslation;
        this.productDescriptionTranslation = productDescriptionTranslation;
        this.keySpecificationTranslation = keySpecificationTranslation;
        this.keyWordsTranslation = keyWordsTranslation;
        this.metaTagsTranslation = metaTagsTranslation;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public String getCountry() {
        return country;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public String getPaymentTermsTranslation() {
        return paymentTermsTranslation;
    }

    public String getMinimumOrder() {
        return minimumOrder;
    }

    public String getFobPort() {
        return fobPort;
    }

    public String getFobPrice() {
        return fobPrice;
    }

    public String getLeadTime() {
        return leadTime;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductNameTranslation() {
        return productNameTranslation;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductDescriptionTranslation() {
        return productDescriptionTranslation;
    }

    public String getKeySpecification() {
        return keySpecification;
    }

    public String getKeySpecificationTranslation() {
        return keySpecificationTranslation;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public String getKeyWordsTranslation() {
        return keyWordsTranslation;
    }

    public String getMetaTags() {
        return metaTags;
    }

    public String getMetaTagsTranslation() {
        return metaTagsTranslation;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        ProductTranslation rhs = (ProductTranslation) obj;
        builder.append(snapshotId, rhs.getSnapshotId());
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(snapshotId);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("snapshotId", snapshotId).append("paymentTerms", paymentTerms)
                .append("paymentTermsTranslation", paymentTermsTranslation).append("productName", productName)
                .append("productNameTranslation", productNameTranslation)
                .append("productDescription", productDescription)
                .append("productDescriptionTranslation", productDescriptionTranslation)
                .append("keySpecification", keySpecification)
                .append("keySpecificationTranslation", keySpecificationTranslation).append("keyWords", keyWords)
                .append("keyWordsTranslation", keyWordsTranslation).append("metaTags", metaTags)
                .append("metaTagsTranslation", metaTagsTranslation);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 21;
    }
}
