SELECT
  mv.party_id,
  mv.membership_level_id,  
  mv.level,
  mv.membership_id,
  mv.date_purchased,
  mv.base_price,
  mv.price_at_purchase,
  mv.number_of_messages,
  mv.company_showroom,
  mv.hot_product_listing,
  mv.product_alert,
  mv.supplier_to_supplier_inquiry,
  mv.export_to_china_tutorial,
  mv.enhance_profile,
  mv.third_party_verification,
  mv.website_and_advanced_email,
  mv.effective_date,
  mv.video,
  mv.contact_china_direct,
  mv.market_analysis,
  mv.buyer_search,
  mv.logistics_assistance,
  mv.price_paid,
  mv.number_of_products,
  mv.number_of_translations,
  mv.expiration_date,
  mv.payment_type,
  mv.payment_invoice,
  mv.renewal_discount_amount, 
  mv.upgrade_credit_amount,
  mv.membership_audit_id,
  mv.event_type,
  mv.version_number,
  mv.term
FROM membership_view as mv
