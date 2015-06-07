package com.daugherty.e2c.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents business information about a Product.
 */
public class ProductInformation extends ValueObject {

    private static final long serialVersionUID = 1L;

    private Supplier supplier;
    private String name;
    private String description;
    private String paymentTerms;
    private final String minimumOrder;
    private final String freightOnBoardPort;
    private final String freightOnBoardPrice;
    private final String country;
    private final String leadTime;
    private final String modelNumber;
    private String specifications;

    public ProductInformation(Long supplierId, String name, String description, String paymentTerms,
            String minimumOrder, String freightOnBoardPort, String freightOnBoardPrice, String country,
            String leadTime, String modelNumber, String specifications) {
        supplier = new Supplier(supplierId);
        this.name = name;
        this.description = description;
        this.paymentTerms = paymentTerms;
        this.minimumOrder = minimumOrder;
        this.freightOnBoardPort = freightOnBoardPort;
        this.freightOnBoardPrice = freightOnBoardPrice;
        this.country = country;
        this.leadTime = leadTime;
        this.modelNumber = modelNumber;
        this.specifications = specifications;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public final void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public String getMinimumOrder() {
        return minimumOrder;
    }

    public String getFreightOnBoardPort() {
        return freightOnBoardPort;
    }

    public String getFreightOnBoardPrice() {
        return freightOnBoardPrice;
    }

    public String getCountry() {
        return country;
    }

    public String getLeadTime() {
        return leadTime;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void translate(ProductTranslation translation) {
        if (translation != null) {
            if (StringUtils.isNotBlank(translation.getProductNameTranslation())) {
                name = translation.getProductNameTranslation();
            }
            if (StringUtils.isNotBlank(translation.getProductDescriptionTranslation())) {
                description = translation.getProductDescriptionTranslation();
            }
            if (StringUtils.isNotBlank(translation.getPaymentTermsTranslation())) {
                paymentTerms = translation.getPaymentTermsTranslation();
            }
            if (StringUtils.isNotBlank(translation.getKeySpecificationTranslation())) {
                specifications = translation.getKeySpecificationTranslation();
            }
        }
    }

    @Override
    protected void addFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        ProductInformation rhs = (ProductInformation) obj;
        builder.append(supplier, rhs.supplier).append(name, rhs.name).append(description, rhs.description)
                .append(paymentTerms, rhs.paymentTerms).append(minimumOrder, rhs.minimumOrder)
                .append(freightOnBoardPort, rhs.freightOnBoardPort)
                .append(freightOnBoardPrice, rhs.freightOnBoardPrice).append(country, rhs.country)
                .append(leadTime, rhs.leadTime).append(modelNumber, rhs.modelNumber)
                .append(specifications, rhs.specifications);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 1005;
    }

    @Override
    protected void addFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(supplier).append(name).append(description).append(paymentTerms).append(minimumOrder)
                .append(freightOnBoardPort).append(freightOnBoardPrice).append(country).append(leadTime)
                .append(modelNumber).append(specifications);
    }

    @Override
    protected void addFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("supplier", supplier).append("name", name).append("description", description)
                .append("paymentTerms", paymentTerms).append("minimumOrder", minimumOrder)
                .append("freightOnBoardPort", freightOnBoardPort).append("freightOnBoardPrice", freightOnBoardPrice)
                .append("country", country).append("leadTime", leadTime).append("modelNumber", modelNumber)
                .append("specifications", specifications);
    }

}
