SELECT lsv.party_id,
	   lsv.public_party_id,
       lsv.party_audit_id,
       lsv.ip_address,
       lsv.event_type,
       lsv.version_number,
       lsv.company_name_english, 
       lsv.company_description,
       pat.company_description AS translated_company_description,
       lsv.number_of_employees, 
       lsv.company_website, 
       lsv.year_established, 
       lsv.total_annual_sales, 
       lsv.first_name, 
       lsv.last_name, 
       lsv.skype_id, 
       lsv.msn_id, 
       lsv.icq_id, 
       lsv.email_address, 
       lsv.gender, 
       lsv.country, 
       lsv.license_link,
       lsv.company_logo_link,
       lsv.video_link,
       mv.party_id as membership_party_id,
	   mv.membership_level_id as membership_membership_level_id,  
	   mv.level as membership_level,
	   mv.membership_id as membership_membership_id,
	   mv.date_purchased as membership_date_purchased,
	   mv.base_price as membership_base_price,
	   mv.price_at_purchase as membership_price_at_purchase,
	   mv.number_of_messages as membership_number_of_messages,
	   mv.company_showroom as membership_company_showroom,
	   mv.hot_product_listing as membership_hot_product_listing,
	   mv.product_alert as membership_product_alert,
	   mv.supplier_to_supplier_inquiry as membership_supplier_to_supplier_inquiry,
	   mv.export_to_china_tutorial as membership_export_to_china_tutorial,
	   mv.enhance_profile as membership_enhance_profile,
	   mv.third_party_verification as membership_third_party_verification,
	   mv.website_and_advanced_email as membership_website_and_advanced_email,
	   mv.effective_date as membership_effective_date,
	   mv.video as membership_video,
	   mv.contact_china_direct as membership_contact_china_direct,
	   mv.market_analysis as membership_market_analysis,
	   mv.buyer_search as membership_buyer_search,
	   mv.logistics_assistance as membership_logistics_assistance,
	   mv.price_paid as membership_price_paid,
	   mv.number_of_products as membership_number_of_products,
	   mv.number_of_translations as membership_number_of_translations,
	   mv.expiration_date as membership_expiration_date,
	   mv.payment_type as membership_payment_type,
	   mv.payment_invoice as membership_payment_invoice,
	   mv.renewal_discount_amount as membership_renewal_discount_amount, 
	   mv.upgrade_credit_amount as membership_upgrade_credit_amount,
	   mv.membership_audit_id as membership_membership_audit_id,
	   mv.event_type as membership_event_type,
	   mv.version_number as membership_version_number,
	   mv.term as membership_term,
	   pmv.party_id as provisional_party_id,
       pmv.membership_level_id as provisional_membership_level_id,  
	   pmv.level as provisional_level,
	   pmv.membership_id as provisional_membership_id,
	   pmv.date_purchased as provisional_date_purchased,
	   pmv.base_price as provisional_base_price,
	   pmv.price_at_purchase as provisional_price_at_purchase,
	   pmv.number_of_messages as provisional_number_of_messages,
	   pmv.company_showroom as provisional_company_showroom,
	   pmv.hot_product_listing as provisional_hot_product_listing,
	   pmv.product_alert as provisional_product_alert,
	   pmv.supplier_to_supplier_inquiry as provisional_supplier_to_supplier_inquiry,
	   pmv.export_to_china_tutorial as provisional_export_to_china_tutorial,
	   pmv.enhance_profile as provisional_enhance_profile,
	   pmv.third_party_verification as provisional_third_party_verification,
	   pmv.website_and_advanced_email as provisional_website_and_advanced_email,
	   pmv.effective_date as provisional_effective_date,
	   pmv.video as provisional_video,
	   pmv.contact_china_direct as provisional_contact_china_direct,
	   pmv.market_analysis as provisional_market_analysis,
	   pmv.buyer_search as provisional_buyer_search,
	   pmv.logistics_assistance as provisional_logistics_assistance,
	   pmv.price_paid as provisional_price_paid,
	   pmv.number_of_products as provisional_number_of_products,
	   pmv.number_of_translations as provisional_number_of_translations,
	   pmv.expiration_date as provisional_expiration_date,
	   pmv.payment_type as provisional_payment_type,
	   pmv.payment_invoice as provisional_payment_invoice,
	   pmv.renewal_discount_amount as provisional_renewal_discount_amount, 
	   pmv.upgrade_credit_amount as provisional_upgrade_credit_amount,
	   pmv.membership_audit_id as provisional_membership_audit_id,
	   pmv.event_type as provisional_event_type,
	   pmv.version_number as provisional_version_number,
	   pmv.term as provisional_term,
       pc.certification_id,
       lsv.language_id,
       lsv.registration_date,
       lsv.business_phone
FROM latest_supplier_view AS lsv
  LEFT OUTER JOIN (SELECT pat.party_audit_id, 
                          pat.company_description 
                   FROM party_audit_translation pat
                     INNER JOIN language l ON pat.language_id = l.language_id
                   WHERE l.abbreviation = :language) AS pat ON lsv.party_audit_id = pat.party_audit_id
  LEFT OUTER JOIN membership_view as mv ON mv.party_id = lsv.party_id AND mv.event_type = 'Paid'                      
  LEFT OUTER JOIN provisional_membership_view as pmv ON pmv.party_id = lsv.party_id
  LEFT OUTER JOIN party_certification pc ON lsv.party_id = pc.party_id
WHERE lsv.party_id = :partyId
  
