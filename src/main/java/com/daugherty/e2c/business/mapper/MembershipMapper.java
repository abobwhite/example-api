package com.daugherty.e2c.business.mapper;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonDiscount;
import com.daugherty.e2c.service.json.JsonMembership;
import com.daugherty.e2c.service.json.JsonMembershipDiscount;
import com.google.common.collect.Lists;

@Component
public class MembershipMapper {
    @Inject
    protected Hashids hashids;
    
    public JsonMembership fromDomainObject(Membership membership){
        return new JsonMembership(membership, hashids.encode(membership.getSupplierId()));
    }
    
    public Membership toExistingDomainObject(Long id, JsonMembership json){
        MembershipLevel membershipLevel = new MembershipLevel(json.getLevel(), json.getBasePrice(), json.getTerm(), json.getProductCount(), json.getMessageCount(),
                json.getTranslationsRemaining(), json.getProfilePublic(), json.getHotProductCount(), json.getIncludedInProductAlerts(),
                json.getSupplierMessagingEnabled(), json.getExportTutorialAccessible(), json.getAdditionalProductImageCount(),
                json.getVerifiableByThirdParty(), json.getAdvancedWebAndMailCapabilityEnabled(), json.getVideoUploadable(), json.getContactChinaDirect(),
                json.getMarketAnalysis(), json.getBuyerSearch(), json.getLogisticsAssistance());

        List<MembershipDiscount> discounts = Lists.newArrayList();

        BigDecimal calculatedPurchasePrice = json.getBasePrice();
        
        if (json.getEarlyRenewalDiscount() != null && json.getEarlyRenewalDiscount().doubleValue() > 0) {
            calculatedPurchasePrice = calculatedPurchasePrice.subtract(json.getEarlyRenewalDiscount());
        }

        if (json.getUpgradeCredit() != null && json.getUpgradeCredit().doubleValue() > 0) {
            calculatedPurchasePrice = calculatedPurchasePrice.subtract(json.getUpgradeCredit());
        }

        for (JsonMembershipDiscount jsonMembershipDiscount : json.getDiscounts()) {
            JsonDiscount jsonDiscount = jsonMembershipDiscount.getDiscount();
            Discount discount = new Discount(jsonDiscount.getId(), jsonDiscount.getCode(),
                    DiscountAmountType.findByDescription(jsonDiscount.getType()), jsonDiscount.getDiscountAmount(),
                    jsonDiscount.getDescription(), jsonDiscount.getOngoing(), jsonDiscount.getSpecial(),
                    jsonDiscount.getEffectiveDate(), jsonDiscount.getExpirationDate());

            discounts.add(new MembershipDiscount(discount, jsonDiscount.getAmount()));

            calculatedPurchasePrice = calculatedPurchasePrice.subtract(jsonDiscount.getAmount());
        }
        
        return new Membership(id, hashids.decode(json.getSupplierId())[0], membershipLevel, ApprovalStatus.findByName(json.getApprovalStatus()), json.getVersion(),
                json.getPurchaseDate(), json.getEffectiveDate(), json.getExpirationDate(), calculatedPurchasePrice, json.getPaymentAmount(), null,
                PaymentType.findByType(json.getPaymentType()), json.getPaymentInvoice(), json.getEarlyRenewalDiscount(), json.getUpgradeCredit(), discounts);
    }
    
    public Membership toDomainObject(JsonMembership json){
        return toExistingDomainObject(null, json);
    }
}
