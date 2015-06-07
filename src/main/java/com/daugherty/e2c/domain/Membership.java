package com.daugherty.e2c.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;

/**
 * Represents a site membership term for a Supplier.
 */
public class Membership extends Entity implements Validatable {

    private static final long serialVersionUID = 1L;

    public static final String PURCHASE_PRICE_SERIAL_PROPERTY = "purchasePrice";
    public static final String PAYMENT_AMOUNT_SERIAL_PROPERTY = "paymentAmount";
    public static final String EFFECTIVE_DATE_SERIAL_PROPERTY = "effectiveDate";
    public static final String EXPIRATION_DATE_SERIAL_PROPERTY = "expirationDate";

    private final Long supplierId; // TODO Would we ever need the full Party/Supplier object here?
    private BigDecimal purchasePrice;
    private BigDecimal paymentAmount;
    private MembershipLevel level;
    private Date purchaseDate;
    private Date effectiveDate;
    private Date expirationDate;
    private ApprovalStatus approvalStatus = ApprovalStatus.PROVISIONAL;
    private Integer version = 1;
    private Long snapshotId;
    private PaymentType paymentType = PaymentType.NONE;
    private String paymentInvoice;
    private BigDecimal earlyRenewalDiscount = BigDecimal.ZERO;
    private BigDecimal upgradeCredit = BigDecimal.ZERO;
    private List<MembershipDiscount> membershipDiscounts = Lists.newArrayList();

    /**
     * Constructor for new Membership instances.
     */
    public Membership(Long supplierId, MembershipLevel level) {
        this(0L, supplierId, level, level.getDefaultApprovalStatus(), 1, today(), today(), createExpirationDate(level),
                level.getPrice(), BigDecimal.ZERO, null, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO,
                new ArrayList<MembershipDiscount>());
    }

    /**
     * Constructor for existing Membership instances.
     */
    public Membership(Long id, Long supplierId, MembershipLevel level, ApprovalStatus approvalStatus, Integer version,
            Date purchaseDate, Date effectiveDate, Date expirationDate, BigDecimal purchasePrice,
            BigDecimal paymentAmount, Long snapshotId, PaymentType paymentType, String paymentInvoice,
            BigDecimal earlyRenewalDiscount, BigDecimal upgradeCredit) {
        this(id, supplierId, level, approvalStatus, version, purchaseDate, effectiveDate, expirationDate,
                purchasePrice, paymentAmount, snapshotId, paymentType, paymentInvoice, earlyRenewalDiscount,
                upgradeCredit, new ArrayList<MembershipDiscount>());

    }

    /**
     * Constructor for existing Membership instances with Discounts.
     */
    public Membership(Long id, Long supplierId, MembershipLevel level, ApprovalStatus approvalStatus, Integer version,
            Date purchaseDate, Date effectiveDate, Date expirationDate, BigDecimal purchasePrice,
            BigDecimal paymentAmount, Long snapshotId, PaymentType paymentType, String paymentInvoice,
            BigDecimal earlyRenewalDiscount, BigDecimal upgradeCredit, List<MembershipDiscount> membershipDiscounts) {
        super(id);
        this.supplierId = supplierId;
        this.level = level;
        this.approvalStatus = approvalStatus;
        this.version = version;
        this.purchaseDate = purchaseDate;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.purchasePrice = purchasePrice;
        this.paymentAmount = paymentAmount;
        this.snapshotId = snapshotId;
        this.paymentType = paymentType;
        this.paymentInvoice = paymentInvoice;
        this.earlyRenewalDiscount = earlyRenewalDiscount;
        this.upgradeCredit = upgradeCredit;
        this.membershipDiscounts = membershipDiscounts;

    }

    public Long getSupplierId() {
        return supplierId;
    }

    public MembershipLevel getLevel() {
        return level;
    }

    public void setLevel(MembershipLevel level) {
        this.level = level;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public Integer getVersion() {
        return version;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public void setPaymentInvoice(String paymentInvoice) {
        this.paymentInvoice = paymentInvoice;
    }

    public String getPaymentInvoice() {
        return paymentInvoice;
    }

    public BigDecimal getEarlyRenewalDiscount() {
        return earlyRenewalDiscount;
    }

    public void setEarlyRenewalDiscount(BigDecimal earlyRenewalDiscount) {
        this.earlyRenewalDiscount = earlyRenewalDiscount;
    }

    public BigDecimal getUpgradeCredit() {
        return upgradeCredit;
    }

    public void setUpgradeCredit(BigDecimal upgradeCredit) {
        this.upgradeCredit = upgradeCredit;
    }

    public List<MembershipDiscount> getMembershipDiscounts() {
        return membershipDiscounts;
    }

    public void setMembershipDiscounts(List<MembershipDiscount> membershipDiscounts) {
        this.membershipDiscounts = membershipDiscounts;
    }

    public boolean isExpired() {
        return new DateTime(new Date()).isAfter(expirationDate.getTime());
    }

    @Override
    public ValidationError validate() {
        ValidationError error = new ValidationError();

        validatePurchasePrice(error);
        validatePaymentAmount(error);
        validateEffectiveDate(error);

        error.add(level.validate());

        return error;
    }

    private void validatePurchasePrice(ValidationError errors) {
        if (purchasePrice == null) {
            errors.add(PURCHASE_PRICE_SERIAL_PROPERTY, PURCHASE_PRICE_REQUIRED);
        }
    }

    private void validatePaymentAmount(ValidationError errors) {
        if (paymentAmount == null && ApprovalStatus.PAID.equals(approvalStatus)) {
            errors.add(PAYMENT_AMOUNT_SERIAL_PROPERTY, PAYMENT_AMOUNT_REQUIRED);
        }
    }

    private void validateEffectiveDate(ValidationError errors) {
        if (effectiveDate == null) {
            errors.add(EFFECTIVE_DATE_SERIAL_PROPERTY, EFFECTIVE_DATE_REQUIRED);
        }

        if (effectiveDate != null && expirationDate != null && effectiveDate.after(expirationDate)) {
            errors.add(EFFECTIVE_DATE_SERIAL_PROPERTY, EFFECTIVE_DATE_INVALID);
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        Membership rhs = (Membership) obj;
        builder.append(supplierId, rhs.supplierId).append(level, rhs.level)
                .append(approvalStatus, rhs.getApprovalStatus());
    }

    @Override
    protected int hashCodeMultiplier() {
        return 7;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(supplierId).append(level).append(approvalStatus);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("supplierId", supplierId).append("level", level).append("approvalStatus", approvalStatus)
                .append("version", version).append("snapshotId", snapshotId).append("effectiveDate", effectiveDate)
                .append("expirationDate", expirationDate);
    }

    // TODO Duplicate of JdbcDao today
    private static Date today() {
        return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
    }

    private static Date createExpirationDate(MembershipLevel level) {
        return DateUtils.addDays(DateUtils.addMonths(today(), level.getMonthsOfTerm()), -1);
    }
}
