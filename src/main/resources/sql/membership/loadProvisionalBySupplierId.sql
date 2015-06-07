SELECT party_id,
       membership_level_id,
       date_purchased,
       level,
       base_price,
       price_at_purchase,
       price_paid,
       membership_id,
       number_of_messages,
       company_showroom,
       hot_product_listing,
       product_alert,
       supplier_to_supplier_inquiry,
       export_to_china_tutorial,
       enhance_profile,
       third_party_verification,
       website_and_advanced_email,
       effective_date,
       video,
       contact_china_direct,
       market_analysis,
	   buyer_search,
	   logistics_assistance,
       number_of_products,
       number_of_translations,
       expiration_date,
       payment_type,
       payment_invoice,
       renewal_discount_amount, 
       upgrade_credit_amount,
       membership_audit_id,
       event_type,
       version_number,
       term
FROM  provisional_membership_view
WHERE party_id = :partyId