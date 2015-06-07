package com.daugherty.e2c.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;

/**
 * Represents a BuyLead for a Party
 */
@SuppressWarnings("serial")
public class BuyLead extends Entity implements Validatable {
    public static final String PARTY_SERIAL_PROPERTY = "requester";
    public static final String PRODUCT_CATEGORY_SERIAL_PROPERTY = "category";
    public static final String EFFECTIVE_DATE_SERIAL_PROPERTY = "effectiveDate";
    public static final String EXPIRATION_DATE_SERIAL_PROPERTY = "expirationDate";
    public static final String LAST_MODIFIED_SERIAL_PROPERTY = "lastModifiedDate";

    private Party requester;
    private ProductCategory productCategory;
    private Date effectiveDate;
    private Date expirationDate;
    private List<Long> respondingSuppliers = Lists.newArrayList();

    /**
     * New Buy Leads
     */
    public BuyLead(Party requester, ProductCategory productCategory, Date effectiveDate, Date expirationDate) {
        this(null, requester, productCategory, effectiveDate, expirationDate);
    }

    /**
     * Existing Buy Leads
     */
    public BuyLead(Long id, Party requester, ProductCategory productCategory, Date effectiveDate, Date expirationDate) {
        super(id);
        this.requester = requester;
        this.productCategory = productCategory;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
    }

    public Party getRequester() {
        return requester;
    }

    public void setRequester(Party requester) {
        this.requester = requester;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public boolean isExpired() {
        return new DateTime(new Date()).isAfter(expirationDate.getTime());
    }

    public List<Long> getRespondingSuppliers() {
        return respondingSuppliers;
    }

    public void setRespondingSuppliers(List<Long> respondingSuppliers) {
        this.respondingSuppliers = respondingSuppliers;
    }

    @Override
    public ValidationError validate() {
        ValidationError error = new ValidationError();

        validateParty(error);
        validateProductCategory(error);
        validateEffectiveDate(error);
        validateExpirationDate(error);

        return error;
    }

    private void validateParty(ValidationError errors) {
        if (requester == null) {
            errors.add(PARTY_SERIAL_PROPERTY, BUY_LEAD_PARTY_REQUIRED);
        }
    }

    private void validateProductCategory(ValidationError errors) {
        if (productCategory == null) {
            errors.add(PRODUCT_CATEGORY_SERIAL_PROPERTY, BUY_LEAD_PRODUCT_CATGEORY_REQUIRED);
        }
    }

    private void validateEffectiveDate(ValidationError errors) {
        if (effectiveDate == null) {
            errors.add(EFFECTIVE_DATE_SERIAL_PROPERTY, BUY_LEAD_EFFECTIVE_DATE_REQUIRED);
        }
    }

    private void validateExpirationDate(ValidationError errors) {
        if (expirationDate == null) {
            errors.add(EXPIRATION_DATE_SERIAL_PROPERTY, BUY_LEAD_EXPIRATION_DATE_REQUIRED);
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        BuyLead rhs = (BuyLead) obj;
        builder.append(requester, rhs.requester).append(productCategory, rhs.productCategory)
                .append(effectiveDate, rhs.effectiveDate).append(expirationDate, rhs.expirationDate);

    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(requester).append(productCategory).append(effectiveDate).append(expirationDate);

    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("requester", requester).append("productCategory", productCategory)
                .append("effectiveDate", effectiveDate).append("expirationDate", expirationDate);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 3;
    }

}
