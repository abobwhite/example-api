package com.daugherty.e2c.service.json;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Marshalls/unmarshalls a Product domain object to/from JSON.
 */
@JsonRootName(value = "product")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonProduct {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("supplier")
    private JsonSupplier supplier;
    @JsonProperty(Product.NAME_SERIAL_PROPERTY)
    private String name;
    @JsonProperty(Product.CHINESE_NAME_SERIAL_PROPERTY)
    private String chineseName;
    @JsonProperty("translatedName")
    private String translatedName;
    @JsonProperty("englishName")
    private String englishName;
    @JsonProperty(Product.DESCRIPTION_SERIAL_PROPERTY)
    private String description;
    @JsonProperty("published")
    private boolean published;
    @JsonProperty("hotProduct")
    private boolean hotProduct;
    @JsonProperty("hotProductOverride")
    private boolean hotProductOverride;
    @JsonProperty(Product.PAYMENT_TERMS_SERIAL_PROPERTY)
    private String paymentTerms;
    @JsonProperty(Product.MINIMUM_ORDER_SERIAL_PROPERTY)
    private String minimumOrder;
    @JsonProperty(Product.FREIGHT_ON_BOARD_PORT_SERIAL_PROPERTY)
    private String freightOnBoardPort;
    @JsonProperty(Product.FREIGHT_ON_BOARD_PRICE_SERIAL_PROPERTY)
    private String freightOnBoardPrice;
    @JsonProperty(Product.COUNTRY_SERIAL_PROPERTY)
    private String country;
    @JsonProperty(Product.LEAD_TIME_SERIAL_PROPERTY)
    private String leadTime;
    @JsonProperty(Product.MODEL_NUMBER_SERIAL_PROPERTY)
    private String model;
    @JsonProperty(Product.SPECIFICATIONS_SERIAL_PROPERTY)
    private String specifications;
    @JsonProperty(Product.META_TAGS_SERIAL_PROPERTY)
    private String metaTags;
    @JsonProperty(Product.KEYWORDS_SERIAL_PROPERTY)
    private String keywords;
    @JsonProperty(Product.APPROVAL_STATUS_SERIAL_PROPERTY)
    private String approvalStatus;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty(Product.IMAGES_SERIAL_PROPERTY)
    private List<JsonProductImage> images = Lists.newArrayList();
    @JsonProperty(Product.CATEGORIES_SERIAL_PROPERTY)
    private List<Long> categories = Lists.newArrayList();
    @JsonProperty("publicationDate")
    private Date publicationDate;
    @JsonProperty("links")
    protected Map<String, String> links;

    public JsonProduct() {
    }

    public JsonProduct(Product product, DocumentUrlFactory urlFactory, Locale locale, Principal principal) {
        id = product.getId();
        setInformationFields(product.getInformation(), urlFactory, locale, principal);
        setMetadataFields(product.getMetadata());
        images = serializeImages(product.getImages(), urlFactory, locale);
        approvalStatus = product.getApprovalStatus().getName();
        version = product.getVersion();
        setLinks(product.getCertificationId());
    }

    private void setInformationFields(ProductInformation information, DocumentUrlFactory urlFactory, Locale locale,
            Principal principal) {
        supplier = new JsonSupplier(information.getSupplier(), urlFactory, locale, principal);
        name = information.getName();
        description = information.getDescription();
        paymentTerms = information.getPaymentTerms();
        minimumOrder = information.getMinimumOrder();
        freightOnBoardPort = information.getFreightOnBoardPort();
        freightOnBoardPrice = information.getFreightOnBoardPrice();
        country = information.getCountry();
        leadTime = information.getLeadTime();
        model = information.getModelNumber();
        specifications = information.getSpecifications();
    }

    private void setMetadataFields(ProductMetadata metadata) {
        categories = serializeCategories(metadata.getCategories());
        chineseName = metadata.getChineseName();
        translatedName = metadata.getTranslatedName();
        englishName = metadata.getEnglishName();
        metaTags = metadata.getMetaTags();
        keywords = metadata.getKeywords();
        published = Boolean.TRUE.equals(metadata.isPublished());
        hotProduct = Boolean.TRUE.equals(metadata.isHot());
        hotProductOverride = Boolean.TRUE.equals(metadata.isHotProductOverride());
        publicationDate = metadata.getPublishedDate();
    }

    private List<Long> serializeCategories(List<ProductCategory> entityCategories) {
        List<Long> categoryIds = new ArrayList<Long>();
        for (ProductCategory productCategory : entityCategories) {
            categoryIds.add(productCategory.getId());
        }
        return categoryIds;
    }

    private List<JsonProductImage> serializeImages(List<ProductImage> entityImages, DocumentUrlFactory urlFactory,
            Locale locale) {
        List<JsonProductImage> jsonImages = Lists.newArrayList();
        for (ProductImage productImage : entityImages) {
            jsonImages.add(new JsonProductImage(productImage, urlFactory, locale));
        }
        return jsonImages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JsonSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(JsonSupplier supplier) {
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
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

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setHotProduct(boolean hotProduct) {
        this.hotProduct = hotProduct;
    }

    public boolean isHotProduct() {
        return hotProduct;
    }

    public boolean isHotProductOverride() {
        return hotProductOverride;
    }

    public void setHotProductOverride(boolean hotProductOverride) {
        this.hotProductOverride = hotProductOverride;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(String mimimumOrder) {
        minimumOrder = mimimumOrder;
    }

    public String getFreightOnBoardPort() {
        return freightOnBoardPort;
    }

    public void setFreightOnBoardPort(String freightOnBoardPort) {
        this.freightOnBoardPort = freightOnBoardPort;
    }

    public String getFreightOnBoardPrice() {
        return freightOnBoardPrice;
    }

    public void setFreightOnBoardPrice(String freightOnBoardPrice) {
        this.freightOnBoardPrice = freightOnBoardPrice;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(String leadTime) {
        this.leadTime = leadTime;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getMetaTags() {
        return metaTags;
    }

    public void setMetaTags(String metaTags) {
        this.metaTags = metaTags;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<JsonProductImage> getImages() {
        return images;
    }

    public void setImages(List<JsonProductImage> images) {
        this.images = images;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    public final Date getPublicationDate() {
        return publicationDate;
    }

    public final void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Long certificationId) {

        Long now = new Date().getTime();

        links = Maps.newHashMap();
        if (certificationId == null || certificationId == 0L) {
            links.put("certification", null);
        } else {
            links.put("certification", "certifications/" + certificationId + "?latest=true&_=" + now);
        }
    }
}
