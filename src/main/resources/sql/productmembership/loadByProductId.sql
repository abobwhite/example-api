SELECT
  m.party_id,
  ml.membership_level_id,  
  mt.tier as level,
  m.membership_id,
  ma.date_purchased,
  ma.base_price,
  ma.price_at_purchase,
  ma.number_of_messages,
  ma.company_showroom,
  ma.hot_product_listing,
  ma.product_alert,
  ma.supplier_to_supplier_inquiry,
  ma.export_to_china_tutorial,
  ma.enhance_profile,
  ma.third_party_verification,
  ma.website_and_advanced_email,
  ma.effective_date,
  ma.video,
  ma.contact_china_direct,
  ma.market_analysis,
  ma.buyer_search,
  ma.logistics_assistance,
  ma.price_paid,
  ma.number_of_products,
  ma.number_of_translations,
  ma.expiration_date,
  ma.payment_type,
  ma.payment_invoice,
  ma.renewal_discount_amount, 
  ma.upgrade_credit_amount,
  ma.membership_audit_id,
  mas.event_type,
  mas.version_number
FROM product_membership as pm
  INNER JOIN membership as m ON pm.membership_id = m.membership_id
  INNER JOIN membership_audit_status as mas ON m.membership_id = mas.membership_id
  INNER JOIN membership_audit as ma ON mas.membership_audit_id = ma.membership_audit_id
  INNER JOIN membership_level as ml ON ma.membership_level_id = ml.membership_level_id
  INNER JOIN membership_tier as mt ON ml.membership_tier_id = mt.membership_tier_id 
WHERE mas.membership_audit_status_id IN (SELECT max(lmas.membership_audit_status_id) membership_audit_status_id FROM membership_audit_status lmas WHERE lmas.membership_id = mas.membership_id)
  AND pm.product_id = :productId