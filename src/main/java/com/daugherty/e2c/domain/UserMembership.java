package com.daugherty.e2c.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: SHK0723
 * Date: 1/2/14
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserMembership extends Membership{

    private static final long serialVersionUID = 1L;
    public static final String USERNAME_SERIAL_PROPERTY = "username";
    public static final String FIRST_NAME_SERIAL_PROPERTY = "firstName";
    public static final String LAST_NAME_SERIAL_PROPERTY = "lastName";
    public static final String EMAIL_SERIAL_PROPERTY = "email";
    public static final String COMPANY_NAME_SERIAL_PROPERTY = "companyName";
    public static final String APPROVAL_STATUS_SERIAL_PROPERTY = "userApprovalStatus";
    public static final String MEMBERSHIP_LEVEL_SERIAL_PROPERTY = "level";
    public static final String EFFECTIVE_DATE_SERIAL_PROPERTY = "effectiveDate";
    public static final String EXPIRATION_DATE_SERIAL_PROPERTY = "expirationDate";


    private String userName;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String companyNameEnglish;
    private ApprovalStatus userApprovalStatus = ApprovalStatus.UNPROFILED;

    public UserMembership(Long id,
                          Long supplierId, MembershipLevel level, ApprovalStatus approvalStatus, Integer version, Date purchaseDate, Date effectiveDate, Date expirationDate, BigDecimal purchasePrice, BigDecimal paymentAmount, Long snapshotId, PaymentType paymentType, String paymentInvoice, BigDecimal earlyRenewalDiscount, BigDecimal upgradeCredit,
                          String userName, String firstName, String lastName, String emailAddress, String companyNameEnglish, ApprovalStatus userApprovalStatus) {
        super(id, supplierId, level, approvalStatus, version, purchaseDate, effectiveDate, expirationDate, purchasePrice, paymentAmount, snapshotId, paymentType, paymentInvoice, earlyRenewalDiscount, upgradeCredit);
        setUserName(userName);
        setFirstName(firstName);
        setLastName(lastName);
        setEmailAddress(emailAddress);
        setCompanyNameEnglish(companyNameEnglish);
        setUserApprovalStatus(userApprovalStatus);
    }

    public UserMembership(Long id,
                          Long supplierId, MembershipLevel level, ApprovalStatus approvalStatus, Integer version, Date purchaseDate, Date effectiveDate, Date expirationDate, BigDecimal purchasePrice, BigDecimal paymentAmount, Long snapshotId, PaymentType paymentType, String paymentInvoice, BigDecimal earlyRenewalDiscount, BigDecimal upgradeCredit) {
        super(id, supplierId, level, approvalStatus, version, purchaseDate, effectiveDate, expirationDate, purchasePrice, paymentAmount, snapshotId, paymentType, paymentInvoice, earlyRenewalDiscount, upgradeCredit);
    }

    public UserMembership(Long id, Long supplierId, MembershipLevel level, ApprovalStatus approvalStatus, Integer version, Date purchaseDate, Date effectiveDate, Date expirationDate, BigDecimal purchasePrice, BigDecimal paymentAmount, Long snapshotId, PaymentType paymentType, String paymentInvoice, BigDecimal earlyRenewalDiscount, BigDecimal upgradeCredit, List<MembershipDiscount> membershipDiscounts) {
        super(id, supplierId, level, approvalStatus, version, purchaseDate, effectiveDate, expirationDate, purchasePrice, paymentAmount, snapshotId, paymentType, paymentInvoice, earlyRenewalDiscount, upgradeCredit, membershipDiscounts);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCompanyNameEnglish() {
        return companyNameEnglish;
    }

    public void setCompanyNameEnglish(String companyNameEnglish) {
        this.companyNameEnglish = companyNameEnglish;
    }

    public ApprovalStatus getUserApprovalStatus() {
        return userApprovalStatus;
    }

    public void setUserApprovalStatus(ApprovalStatus userApprovalStatus) {
        this.userApprovalStatus = userApprovalStatus;
    }
    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        UserMembership rhs = (UserMembership) obj;
        builder.append(userName, rhs.userName).append(firstName, rhs.firstName).append(lastName, rhs.lastName).append(emailAddress, rhs.emailAddress).append(companyNameEnglish, rhs.companyNameEnglish)
        .append(userApprovalStatus, rhs.userApprovalStatus);
        super.addEntityFieldsToEqualsBuilder(builder, obj);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 7;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(userName).append(firstName).append(lastName);
        super.addEntityFieldsToHashCodeBuilder(builder);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("userName", userName).append("firstName", firstName).append("lastName", lastName).append("emailAddress", emailAddress).append("companyNameEnglish", companyNameEnglish)
        .append("userApprovalStatus", userApprovalStatus);
        super.addEntityFieldsToToStringBuilder(builder);
    }
}
