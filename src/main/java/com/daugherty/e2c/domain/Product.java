package com.daugherty.e2c.domain;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.daugherty.e2c.security.Role;
import com.google.common.collect.Lists;

/**
 * Represents a Product to be Exported
 */
public class Product extends Entity implements Approvable, Validatable, MembershipLevelAware {

    private static final long serialVersionUID = 1L;

    public static final String NAME_SERIAL_PROPERTY = "name";
    public static final String DESCRIPTION_SERIAL_PROPERTY = "description";
    public static final String PAYMENT_TERMS_SERIAL_PROPERTY = "paymentTerms";
    public static final String MINIMUM_ORDER_SERIAL_PROPERTY = "minimumOrder";
    public static final String FREIGHT_ON_BOARD_PORT_SERIAL_PROPERTY = "freightOnBoardPort";
    public static final String FREIGHT_ON_BOARD_PRICE_SERIAL_PROPERTY = "freightOnBoardPrice";
    public static final String COUNTRY_SERIAL_PROPERTY = "country";
    public static final String LEAD_TIME_SERIAL_PROPERTY = "leadTime";
    public static final String MODEL_NUMBER_SERIAL_PROPERTY = "modelNumber";
    public static final String SPECIFICATIONS_SERIAL_PROPERTY = "specifications";
    public static final String META_TAGS_SERIAL_PROPERTY = "metaTags";
    public static final String KEYWORDS_SERIAL_PROPERTY = "keywords";
    public static final String HOT_PRODUCT_SERIAL_PROPERTY = "hotProduct";
    public static final String PUBLISHED_SERIAL_PROPERTY = "published";
    public static final String APPROVAL_STATUS_SERIAL_PROPERTY = "approvalStatus";
    public static final String CATEGORIES_SERIAL_PROPERTY = "categories";
    public static final String IMAGES_SERIAL_PROPERTY = "images";
    public static final String CHINESE_NAME_SERIAL_PROPERTY = "chineseName";

    private final ProductInformation information;
    private final ProductMetadata metadata;
    private final Long certificationId;
    private List<ProductImage> images = Lists.newArrayList();
    private ApprovalStatus approvalStatus = ApprovalStatus.DRAFT;
    private Integer version = 1;
    private Long snapshotId;

    /**
     * Constructor for new Product instances.
     */
    public Product(ProductInformation information, List<ProductCategory> categories) {
        this(null, information, new ProductMetadata(categories), null, ApprovalStatus.DRAFT, 1, null);
    }

    /**
     * Constructor for existing Product instances.
     */
    public Product(Long id, ProductInformation information, ProductMetadata metadata, Long certificationId,
            ApprovalStatus approvalStatus, Integer version, Long snapshotId) {
        super(id);
        this.information = information;
        this.metadata = metadata;
        this.certificationId = certificationId;
        this.approvalStatus = approvalStatus;
        this.version = version;
        this.snapshotId = snapshotId;
    }

    public ProductInformation getInformation() {
        return information;
    }

    public ProductMetadata getMetadata() {
        return metadata;
    }

    public Long getCertificationId() {
        return certificationId;
    }

    @Override
    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public Long getSnapshotId() {
        return snapshotId;
    }

    @Override
    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    @Override
    public void visit(ApprovalStateTransitionVisitor stateTransitionVisitor) {
        version = stateTransitionVisitor.getNewVersion(version, approvalStatus);
        approvalStatus = stateTransitionVisitor.getNewApprovalStatus(approvalStatus);
    }

    public void translate(ProductTranslation translation) {
        information.translate(translation);
        metadata.translate(translation);
    }

    @Override
    public ValidationError validate() {
        ValidationError errors = new ValidationError();
        validateName(errors);
        validateDescription(errors);
        validatePaymentTerms(errors);
        validateMinimumOrder(errors);
        validateFreightOnBoardPort(errors);
        validateFreightOnBoardPrice(errors);
        validateCountry(errors);
        validateLeadTime(errors);
        validateModelNumber(errors);
        validateSpecifications(errors);
        validateCategories(errors);
        validateImages(errors);

        boolean shouldCheckMetaTagsAndKeywordsForRequired = SecurityUtils
                .authenticatedUserHasRole(Role.SUPPLIER_MODERATOR);
        validateMetaTags(errors, shouldCheckMetaTagsAndKeywordsForRequired);
        validateKeywords(errors, shouldCheckMetaTagsAndKeywordsForRequired);

        return errors;
    }

    @Override
    public Integer getMembershipLevel() {
        return information.getSupplier().getMembershipLevel();
    }

    @Override
    public Integer getRelevance() {
        return 1;
    }

    private void validateName(ValidationError errors) {
        if (information == null || StringUtils.isBlank(information.getName())) {
            errors.add(NAME_SERIAL_PROPERTY, PRODUCT_NAME_REQUIRED);
        } else if (information.getName().length() > 60) {
            errors.add(NAME_SERIAL_PROPERTY, PRODUCT_NAME_LENGTH);
        }
    }

    private void validateDescription(ValidationError errors) {
        if (information == null || StringUtils.isBlank(information.getDescription())) {
            errors.add(DESCRIPTION_SERIAL_PROPERTY, PRODUCT_DESCRIPTION_REQUIRED);
        } else if (information.getDescription().length() > 2000) {
            errors.add(DESCRIPTION_SERIAL_PROPERTY, PRODUCT_DESCRIPTION_LENGTH);
        }
    }

    private void validatePaymentTerms(ValidationError errors) {
        if (information != null && StringUtils.isNotBlank(information.getPaymentTerms())
                && information.getPaymentTerms().length() > 120) {
            errors.add(PAYMENT_TERMS_SERIAL_PROPERTY, PRODUCT_PAYMENT_TERMS_LENGTH);
        }
    }

    private void validateMinimumOrder(ValidationError errors) {
        if (information == null || StringUtils.isBlank(information.getMinimumOrder())) {
            errors.add(MINIMUM_ORDER_SERIAL_PROPERTY, PRODUCT_MINIMUM_ORDER_REQUIRED);
        } else if (information.getMinimumOrder().length() > 20) {
            errors.add(MINIMUM_ORDER_SERIAL_PROPERTY, PRODUCT_MINIMUM_ORDER_LENGTH);
        }
    }

    private void validateFreightOnBoardPort(ValidationError errors) {
        if (information != null && StringUtils.isNotBlank(information.getFreightOnBoardPort())
                && information.getFreightOnBoardPort().length() > 75) {
            errors.add(FREIGHT_ON_BOARD_PORT_SERIAL_PROPERTY, PRODUCT_FOB_PORT_LENGTH);
        }
    }

    private void validateFreightOnBoardPrice(ValidationError errors) {
        if (information != null && StringUtils.isNotBlank(information.getFreightOnBoardPrice())
                && information.getFreightOnBoardPrice().length() > 100) {
            errors.add(FREIGHT_ON_BOARD_PRICE_SERIAL_PROPERTY, PRODUCT_FOB_PRICE_LENGTH);
        }
    }

    private void validateCountry(ValidationError errors) {
        if (information == null || StringUtils.isBlank(information.getCountry())) {
            errors.add(COUNTRY_SERIAL_PROPERTY, PRODUCT_COUNTRY_REQUIRED);
        } else if (information.getCountry().length() > 75) {
            errors.add(COUNTRY_SERIAL_PROPERTY, PRODUCT_COUNTRY_LENGTH);
        }
    }

    private void validateLeadTime(ValidationError errors) {
        if (information != null && StringUtils.isNotBlank(information.getLeadTime())
                && information.getLeadTime().length() > 20) {
            errors.add(LEAD_TIME_SERIAL_PROPERTY, PRODUCT_LEAD_TIME_LENGTH);
        }
    }

    private void validateModelNumber(ValidationError errors) {
        if (information != null && StringUtils.isNotBlank(information.getModelNumber())
                && information.getModelNumber().length() > 30) {
            errors.add(MODEL_NUMBER_SERIAL_PROPERTY, PRODUCT_MODEL_NUMBER_LENGTH);
        }
    }

    private void validateSpecifications(ValidationError errors) {
        if (information != null && StringUtils.isNotBlank(information.getSpecifications())
                && information.getSpecifications().length() > 2000) {
            errors.add(SPECIFICATIONS_SERIAL_PROPERTY, PRODUCT_SPECIFICATIONS_LENGTH);
        }
    }

    private void validateMetaTags(ValidationError errors, boolean checkIsRequired) {
        if (checkIsRequired && (metadata == null || StringUtils.isBlank(metadata.getMetaTags()))) {
            errors.add(KEYWORDS_SERIAL_PROPERTY, PRODUCT_META_TAGS_REQUIRED);
        }
        if (metadata != null && StringUtils.isNotBlank(metadata.getMetaTags()) && metadata.getMetaTags().length() > 100) {
            errors.add(META_TAGS_SERIAL_PROPERTY, PRODUCT_META_TAGS_LENGTH);
        }
    }

    private void validateKeywords(ValidationError errors, boolean checkIsRequired) {
        if (checkIsRequired && (metadata == null || StringUtils.isBlank(metadata.getKeywords()))) {
            errors.add(KEYWORDS_SERIAL_PROPERTY, PRODUCT_KEYWORDS_REQUIRED);
        }
        if (metadata != null && StringUtils.isNotBlank(metadata.getKeywords()) && metadata.getKeywords().length() > 100) {
            errors.add(KEYWORDS_SERIAL_PROPERTY, PRODUCT_KEYWORDS_LENGTH);
        }
    }

    private void validateCategories(ValidationError errors) {
        if (metadata == null || metadata.getCategories() == null || metadata.getCategories().isEmpty()) {
            errors.add(CATEGORIES_SERIAL_PROPERTY, PRODUCT_CATEGORIES_REQUIRED);
        } else if (metadata.getCategories().size() > 3) {
            errors.add(CATEGORIES_SERIAL_PROPERTY, PRODUCT_CATEGORIES_LENGTH);
        }
    }

    private void validateImages(ValidationError errors) {
        if (!exists() && !images.isEmpty()) {
            errors.add(IMAGES_SERIAL_PROPERTY, PRODUCT_IMAGES_ORPHANED);
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        Product rhs = (Product) obj;
        builder.append(certificationId, rhs.certificationId).append(images, rhs.images)
                .append(approvalStatus, rhs.approvalStatus).append(version, rhs.version)
                .append(snapshotId, rhs.snapshotId);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 13;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(certificationId).append(images).append(approvalStatus).append(version).append(snapshotId);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("certificationId", certificationId).append("images", images)
                .append("approvalStatus", approvalStatus).append("version", version).append("snapshotId", snapshotId);
    }

}
