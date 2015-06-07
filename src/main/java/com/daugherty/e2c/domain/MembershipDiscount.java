package com.daugherty.e2c.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents the Membership Discount Domain Object
 */
@SuppressWarnings("serial")
public class MembershipDiscount extends Entity {

    private Long snapshotId;
    private Discount discount;
    private BigDecimal amount;

    public MembershipDiscount(Discount discount, BigDecimal amount) {
        this(null, null, discount, amount);
    }

    public MembershipDiscount(Long id, Long snapshotId, Discount discount, BigDecimal amount) {
        super(id);
        this.snapshotId = snapshotId;
        this.discount = discount;
        this.amount = amount;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public Discount getDiscount() {
        return discount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        MembershipDiscount rhs = (MembershipDiscount) obj;
        builder.append(snapshotId, rhs.getSnapshotId()).append(discount, rhs.getDiscount()).append(amount, getAmount());
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(snapshotId).append(discount).append(amount);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("snapshotId", snapshotId).append("discount", discount).append("amount", amount);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 5;
    }
}
