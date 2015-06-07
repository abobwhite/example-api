
-- Business Types
INSERT INTO business_type (business_type_id, business_type_name, last_modified_by, last_modified_date) 
  VALUES (1, 'Agent', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO business_type (business_type_id, business_type_name, last_modified_by, last_modified_date) 
  VALUES (2, 'Procurement Office', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO business_type (business_type_id, business_type_name, last_modified_by, last_modified_date) 
  VALUES (3, 'Government and Related Organzation', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO business_type (business_type_id, business_type_name, last_modified_by, last_modified_date) 
  VALUES (4, 'Trading Company', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO business_type (business_type_id, business_type_name, last_modified_by, last_modified_date) 
  VALUES (5, 'Business Service', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO business_type (business_type_id, business_type_name, last_modified_by, last_modified_date) 
  VALUES (6, 'Distributor/Wholesaler', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO business_type (business_type_id, business_type_name, last_modified_by, last_modified_date) 
  VALUES (7, 'Manufacturer', 'ReferenceDataLoad', CURRENT_TIMESTAMP);

-- Discount Types
INSERT INTO subscription_type (subscription_type_id, subscription_type_name, last_modified_by, last_modified_date) 
  VALUES (1, 'Renew', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO subscription_type (subscription_type_id, subscription_type_name, last_modified_by, last_modified_date) 
  VALUES (2, 'Upgrade', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO subscription_type (subscription_type_id, subscription_type_name, last_modified_by, last_modified_date) 
  VALUES (3, 'New', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
-- Languages
INSERT INTO language (language_id, language_name, abbreviation, last_modified_by, last_modified_date)
  VALUES (1, 'English', 'en', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO language (language_id, language_name, abbreviation, last_modified_by, last_modified_date)
  VALUES (2, 'Chinese', 'zh', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO language (language_id, language_name, abbreviation, last_modified_by, last_modified_date)
  VALUES (3, 'Spanish', 'es', 'ReferenceDataLoad', CURRENT_TIMESTAMP);

-- Message Tags
INSERT INTO message_tag (message_tag_id, message_tag_name, last_modified_by, last_modified_date)
  VALUES (1, 'Specifications', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO message_tag (message_tag_id, message_tag_name, last_modified_by, last_modified_date)
  VALUES (2, 'Company Overview', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO message_tag (message_tag_id, message_tag_name, last_modified_by, last_modified_date)
  VALUES (3, 'Inspection Certificate', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO message_tag (message_tag_id, message_tag_name, last_modified_by, last_modified_date)
  VALUES (4, 'Minimum Order Quantity', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO message_tag (message_tag_id, message_tag_name, last_modified_by, last_modified_date)
  VALUES (5, 'FOB Price', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO message_tag (message_tag_id, message_tag_name, last_modified_by, last_modified_date)
  VALUES (6, 'Delivery Time', 'ReferenceDataLoad', CURRENT_TIMESTAMP);

-- Application parameters
INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (1, 'Recipient Approval Threshold', '250', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (2, 'Name Attribute Relevance Weight', '5', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (3, 'Keyword Attribute Relevance Weight', '5', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (4, 'Category Attribute Relevance Weight', '7', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (5, 'Description Attribute Relevance Weight', '4', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (6, 'Exact Search Term Relevance Degree', '1.5', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (7, 'Exact Word Search Term Relevance Degree', '2', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (8, 'Partial Word Search Term Relevance Degree', '1', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (9, 'Relevance Buckets', '4', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (10, 'Relevance Floor', '6', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (11, 'Chinese Relevance Floor', '2', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (12, 'Chinese Name Attribute Relevance Weight', '5', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (13, 'Chinese Keyword Attribute Relevance Weight', '5', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (14, 'Chinese Category Attribute Relevance Weight', '7', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (15, 'Chinese Description Attribute Relevance Weight', '4', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (16, 'Chinese Exact Search Term Relevance Degree', '1.5', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (17, 'Chinese Exact Word Search Term Relevance Degree', '2', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (18, 'Chinese Partial Word Search Term Relevance Degree', '1', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO application_parameter (application_parameter_id, application_parameter_name, application_parameter_value, last_modified_by, last_modified_date)
  VALUES (19, 'Chinese Relevance Buckets', '4', 'ReferenceDataLoad', CURRENT_TIMESTAMP);

-- Membership Tiers  
INSERT INTO membership_tier (membership_tier_id, tier)
  VALUES (1, 1);
INSERT INTO membership_tier (membership_tier_id, tier)
  VALUES (2, 2);
INSERT INTO membership_tier (membership_tier_id, tier)
  VALUES (3, 3);
INSERT INTO membership_tier (membership_tier_id, tier)
  VALUES (4, 4);
  
--Renewal Credit Scedules 
-- Tier 2
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (1, 2, 12, 15); 
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (2, 2, 11, 15);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (3, 2, 10, 15);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (4, 2, 9, 15);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (5, 2, 8, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (6, 2, 7, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (7, 2, 6, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (8, 2, 5, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (9, 2, 4, 5);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (10, 2, 3, 5);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (11, 2, 2, 5);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (12, 2, 1, 5);
-- Tier 3 
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (13, 3, 12, 15); 
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (14, 3, 11, 15);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (15, 3, 10, 15);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (16, 3, 9, 15);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (17, 3, 8, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (18, 3, 7, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (19, 3, 6, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (20, 3, 5, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (21, 3, 4, 5);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (22, 3, 3, 5);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (23, 3, 2, 5);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (24, 3, 1, 0);
-- Tier 4 
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (25, 4, 12, 15);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (26, 4, 11, 15);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (27, 4, 10, 15);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (28, 4, 9, 15);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (29, 4, 8, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (30, 4, 7, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (31, 4, 6, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (32, 4, 5, 10);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (33, 4, 4, 5);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (34, 4, 3, 5);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (35, 4, 2, 5);
INSERT INTO renewal_credit_schedule (renewal_credit_schedule_id, membership_tier_id, months_remaining, percent_amount)
  VALUES (36, 4, 1, 5);
  
-- Membership Levels         
INSERT INTO membership_level (membership_level_id, membership_tier_id, base_price, term, 
                              number_of_product, number_of_messages, number_of_translations,
                              company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                              export_to_china_tutorial, enhance_profile, third_party_verification,
                              market_analysis, buyer_search, logistics_assistance,
                              website_and_advanced_email, video, contact_china_direct, effective_date, expiration_date, last_modified_by, last_modified_date)
  VALUES (1, 1, 588.0, 6,
          15, 2147483647, 600,
          TRUE, 0, FALSE, TRUE,
          FALSE, 0, FALSE,
          FALSE, FALSE, FALSE,
          FALSE, FALSE, FALSE, DATE_SUB( CURRENT_DATE, INTERVAL 6 MONTH ),  DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'ReferenceDataLoad', CURRENT_TIMESTAMP);
          
INSERT INTO membership_level (membership_level_id, membership_tier_id, base_price, term, 
                              number_of_product, number_of_messages, number_of_translations,
                              company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                              export_to_china_tutorial, enhance_profile, third_party_verification, 
                              market_analysis, buyer_search, logistics_assistance,
                              website_and_advanced_email, video, contact_china_direct, effective_date, expiration_date, last_modified_by, last_modified_date)
  VALUES (2, 2, 488.0, 6,
          20, 2147483647, 2400,
          TRUE, 10, FALSE, TRUE,
          TRUE, 2, FALSE,
          FALSE, FALSE, FALSE,
          FALSE, FALSE, TRUE, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), TO_DATE( '12/31/9999', 'DD/MM/YYYY' ),'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_level (membership_level_id, membership_tier_id, base_price, term, 
                              number_of_product, number_of_messages, number_of_translations,
                              company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                              export_to_china_tutorial, enhance_profile, third_party_verification, 
                              market_analysis, buyer_search, logistics_assistance,
                              website_and_advanced_email, video, contact_china_direct, effective_date, expiration_date, last_modified_by, last_modified_date)
  VALUES (3, 3, 1588.0, 6,
          40, 2147483647, 12000,
          TRUE, 15, TRUE, TRUE,
          TRUE, 2, FALSE,
          FALSE, FALSE, FALSE,
          TRUE, TRUE, TRUE, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), TO_DATE( '12/31/9999', 'DD/MM/YYYY' ), 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_level (membership_level_id, membership_tier_id, base_price, term, 
                              number_of_product, number_of_messages, number_of_translations,
                              company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                              export_to_china_tutorial, enhance_profile, third_party_verification, 
                              market_analysis, buyer_search, logistics_assistance,
                              website_and_advanced_email, video, contact_china_direct, effective_date, expiration_date, last_modified_by, last_modified_date)
  VALUES (4, 4, 1589.0, 6,
          100, 2147483647, 12000,
          TRUE, 16, TRUE, TRUE,
          TRUE, 2, TRUE,
          TRUE, TRUE, TRUE,
          TRUE, TRUE, TRUE, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), TO_DATE( '12/31/9999', 'DD/MM/YYYY' ),'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_level (membership_level_id, membership_tier_id, base_price, term, 
                              number_of_product, number_of_messages, number_of_translations,
                              company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                              export_to_china_tutorial, enhance_profile, third_party_verification,
                              market_analysis, buyer_search, logistics_assistance,
                              website_and_advanced_email, video, contact_china_direct, effective_date, expiration_date, last_modified_by, last_modified_date)
  VALUES (5, 1, 288.0, 6,
          10, 2147483647, 600,
          TRUE, 0, FALSE, TRUE,
          FALSE, 0, FALSE,
          FALSE, FALSE, FALSE,
          FALSE, FALSE, FALSE, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), TO_DATE( '12/31/9999', 'DD/MM/YYYY' ), 'ReferenceDataLoad', CURRENT_TIMESTAMP);
          
-- Discounts
INSERT INTO discounts (discounts_id, discount_code, discount_amount_type, amount, description, ongoing, special, effective_date, expiration_date, last_modified_by, last_modified_date) 
  VALUES (1, 'DOLLAR1', 'D', 100, '100 Dollars Off Coupon Code', FALSE, FALSE, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), TO_DATE( '12/31/9999', 'DD/MM/YYYY' ), 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO discounts (discounts_id, discount_code, discount_amount_type, amount, description, ongoing, special, effective_date, expiration_date, last_modified_by, last_modified_date) 
  VALUES (2, 'DOLLAR2', 'D', 200, '200 Dollars Off Coupon Code', FALSE, FALSE, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), TO_DATE( '12/31/9999', 'DD/MM/YYYY' ), 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO discounts (discounts_id, discount_code, discount_amount_type, amount, description, ongoing, special, effective_date, expiration_date, last_modified_by, last_modified_date) 
  VALUES (3, 'EXPIRED', 'D', 300, '300 Dollars Off Expired Coupon Code', FALSE, FALSE, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), DATE_SUB( CURRENT_DATE, INTERVAL 1 MONTH ), 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO discounts (discounts_id, discount_code, discount_amount_type, amount, description, ongoing, special, effective_date, expiration_date, last_modified_by, last_modified_date) 
  VALUES (4, 'PERCENT', 'P', 10, '10 Percent Off Coupon Code', FALSE, FALSE, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), TO_DATE( '12/31/9999', 'DD/MM/YYYY' ), 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO discounts (discounts_id, discount_code, discount_amount_type, amount, description, ongoing, special, effective_date, expiration_date, last_modified_by, last_modified_date) 
  VALUES (5, 'ONGOING', 'D', 1000, '1000 Dollar Off Ongoing Coupon Code', TRUE, FALSE, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), TO_DATE( '12/31/9999', 'DD/MM/YYYY' ), 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  

INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (1, 1, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (1, 2, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (1, 3, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (2, 4, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (3, 2, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (3, 3, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (3, 4, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (4, 1, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (4, 2, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (4, 3, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (4, 4, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (5, 3, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_membership_tier (discounts_id, membership_tier_id, last_modified_by, last_modified_date) 
  VALUES (5, 4, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO  discounts_subscription_type (discounts_subscription_type_id, discounts_id, subscription_type_id, last_modified_by, last_modified_date) 
  VALUES (1, 1, 1, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_subscription_type (discounts_subscription_type_id, discounts_id, subscription_type_id, last_modified_by, last_modified_date) 
  VALUES (2, 1, 2, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_subscription_type (discounts_subscription_type_id, discounts_id, subscription_type_id, last_modified_by, last_modified_date) 
  VALUES (3, 1, 3, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_subscription_type (discounts_subscription_type_id, discounts_id, subscription_type_id, last_modified_by, last_modified_date) 
  VALUES (4, 2, 3, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_subscription_type (discounts_subscription_type_id, discounts_id, subscription_type_id, last_modified_by, last_modified_date) 
  VALUES (5, 3, 1, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_subscription_type (discounts_subscription_type_id, discounts_id, subscription_type_id, last_modified_by, last_modified_date) 
  VALUES (6, 3, 2, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_subscription_type (discounts_subscription_type_id, discounts_id, subscription_type_id, last_modified_by, last_modified_date) 
  VALUES (7, 3, 3, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_subscription_type (discounts_subscription_type_id, discounts_id, subscription_type_id, last_modified_by, last_modified_date) 
  VALUES (8, 4, 1, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_subscription_type (discounts_subscription_type_id, discounts_id, subscription_type_id, last_modified_by, last_modified_date) 
  VALUES (9, 4, 2, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_subscription_type (discounts_subscription_type_id, discounts_id, subscription_type_id, last_modified_by, last_modified_date) 
  VALUES (10, 4, 3, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO  discounts_subscription_type (discounts_subscription_type_id, discounts_id, subscription_type_id, last_modified_by, last_modified_date) 
  VALUES (11, 5, 3, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
-- Product Categories (all Level 1, most Level 2, excerpts of Level 3 and translations)
INSERT INTO product_category (product_category_id, product_category, legacy_tid, last_modified_by, last_modified_date, product_category_link)
  VALUES (1, 'Agriculture', 666, 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Agriculture.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (10, 1, 1, 'Agriculture', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (11, 1, 2, '农业', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (12, 1, 3, 'Agricultura', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (10, 10, 1, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen,last_modified_by, last_modified_date)
  VALUES (11, 11, 1, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen,last_modified_by, last_modified_date)
  VALUES (12, 12, 1, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (101, 'Agrochemicals & Pesticide Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (1, 101, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (102, 'Animal husbandry', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (1, 102, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (103, 'By-Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (1, 103, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (104, 'Farm Machinery & Equipment', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (1, 104, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (105, 'Farm Production', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (1, 105, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (106, 'Fishing', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (1, 106, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (107, 'Forestry', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (1, 107, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (108, 'Other Agriculture Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (1, 108, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (2, 'Apparel & Accessories', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Apparel & Accessories.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (20, 2, 1, 'Apparel & Accessories', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (21, 2, 2, '服装服饰', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (22, 2, 3, 'Indumentaria & Accesorios', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (20, 20, 2, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (21, 21, 11, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (22, 22, 14, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
 
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (201, 'Apparel Accessories & Tools', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2, 201, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (202, 'Apparel Finished Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2, 202, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (3, 'Arts & Crafts', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Arts & Crafts.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (30, 3, 1, 'Arts & Crafts', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (31, 3, 2, '艺术及工艺品', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (32, 3, 3, 'Arte & Artesanías', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (30, 30, 3, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (31, 31, 19, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (32, 32, 3, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (301, 'Antiques', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (3, 301, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (302, 'Art & Collectible', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (3, 302, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (303, 'Crafts By Material', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (3, 303, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (304, 'Crafts By Style', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (3, 304, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (305, 'Crafts By Technique', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (3, 305, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (306, 'Gifts & Crafts By Use', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (3, 306, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (307, 'Holiday Gifts & Decorations', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (3, 307, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (308, 'Miscellaneous Crafts', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (3, 308, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (309, 'Other Gifts & Crafts', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (3, 309, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (4, 'Automotive & Transport', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Automotive & Transport.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (40, 4, 1, 'Automotive & Transport', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (41, 4, 2, '交通运输', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (42, 4, 3, 'Automotriz & Transporte', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (40, 40, 4, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (41, 41, 24, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (42, 42, 4, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (401, 'Aircraft', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (4, 401, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (402, 'Cars & Bus', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (4, 402, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (403, 'Other Automotive & Transport', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (4, 403, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (404, 'Railway Vehicles', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (4, 404, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (405, 'Transportation & Logistics Services', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (4, 405, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (406, 'Transportation Related Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (4, 406, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (407, 'Trucks', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (4, 407, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (408, 'Watercraft', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (4, 408, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (5, 'Beauty & Personal Care', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Beauty & Personal Care.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (50, 5, 1, 'Beauty & Personal Care', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (51, 5, 2, '美容及个人护理', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (52, 5, 3, 'Belleza & Cuidado Personal', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (50, 50, 6, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (51, 51, 16, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (52, 52, 5, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (501, 'Beauty Supplies', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (5, 501, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (502, 'Other Beauty & Personal Care Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (5, 502, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (503, 'Personal Care Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (5, 503, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (6, 'Chemicals', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (60, 6, 1, 'Chemicals', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (61, 6, 2, '化学化工产品', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (62, 6, 3, 'Químicos', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (60, 60, 8, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (61, 61, 9, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (62, 62, 24, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (601, 'Agrochemicals & Pesticide Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (6, 601, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (602, 'Catalysts & Chemical Auxiliary Agents', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (6, 602, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (603, 'Cosmetic Raw Materials', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (6, 603, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (604, 'Daily Chemicals', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (6, 604, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (605, 'Disinfectant and Preservatives', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (6, 605, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (606, 'Extract Chemicals', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (6, 606, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (607, 'Feed Additive Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (6, 607, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (608, 'Flavour & Fragrance', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (6, 608, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (609, 'Food Additive Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (6, 609, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (610, 'Hair Care Chemicals', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (6, 610, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (7, 'Computer Hardware & Software', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Computer Hardware & Software.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (70, 7, 1, 'Computer Hardware & Software', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (71, 7, 2, '电脑硬件及软件', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (72, 7, 3, 'Informática', 'ReferenceDataLoad', CURRENT_TIMESTAMP); 
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (70, 70, 9, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (71, 71, 25, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (72, 72, 15, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (701, 'Computer Accessories', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (7, 701, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (702, 'Computer Parts', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (7, 702, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (703, 'Computers', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (7, 703, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (704, 'Drive & Storage Devices', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (7, 704, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (705, 'Keyboards & Mouse', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (7, 705, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (706, 'Office Electronics', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (7, 706, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (707, 'Other Computer Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (7, 707, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (708, 'Software', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (7, 708, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (709, 'Used Computers & Accessories', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (7, 709, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (8, 'Construction', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Construction.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (80, 8, 1, 'Construction', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (81, 8, 2, '建筑建材', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (82, 8, 3, 'Construcción', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (80, 80, 10, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (81, 81, 6, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (82, 82, 7, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (801, 'Aggregate', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (8, 801, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (802, 'Anticorrosive Woods', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (8, 802, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (803, 'Bathroom Vanities', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (8, 803, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (804, 'Bricks', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (8, 804, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (805, 'Building Blocks', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (8, 805, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (806, 'Building Coating', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (8, 806, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (807, 'Ceilings', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (8, 807, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (808, 'Cement', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (8, 808, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (809, 'Concrete Admixtures & Mortar Admixtures', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (8, 809, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (810, 'Construction Adhesives & Sealants', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (8, 810, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (9, 'Energy', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Energy.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (90, 9, 1, 'Energy', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (91, 9, 2, '能源', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (92, 9, 3, 'Energía', 'ReferenceDataLoad', CURRENT_TIMESTAMP); 
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (90, 90, 11, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (91, 91, 2, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (92, 92, 9, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (901, 'Biogas', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (9, 901, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (902, 'Charcoal', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (9, 902, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (903, 'Coal', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (9, 903, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (904, 'Crude Oil', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (9, 904, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (905, 'Other Energy Related Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (9, 905, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (906, 'Petrochemical Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (9, 906, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (907, 'Renewable Energies', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (9, 907, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (10, 'Environment', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Environment.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (100, 10, 1, 'Environment', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (101, 10, 2, '环境', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (102, 10, 3, 'Medio Ambiente', 'ReferenceDataLoad', CURRENT_TIMESTAMP);  
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (100, 100, 12, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (101, 101, 23, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (102, 102, 19, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1001, 'Other Environmental Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (10, 1001, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1002, 'Recycling', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (10, 1002, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1003, 'Renewable Energies', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (10, 1003, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1004, 'Solar Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (10, 1004, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1005, 'Waste Management', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (10, 1005, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1006, 'Water Treatment', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (10, 1006, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (11, 'Food & Beverage', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Food & Beverage.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (110, 11, 1, 'Food & Beverage', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (111, 11, 2, '食品饮料', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (112, 11, 3, 'Alimentos & Bebidas', 'ReferenceDataLoad', CURRENT_TIMESTAMP);  
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (110, 110, 13, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (111, 111, 7, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (112, 112, 2, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1101, 'Baby Food & Drink', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (11, 1101, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1102, 'Food & Beverage Processing Machinery', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (11, 1102, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1103, 'Other Food & Beverage', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (11, 1103, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1104, 'Seasonings & Condiments', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (11, 1104, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1105, 'Bakery, Confectionery & Snacks', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (11, 1105, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1106, 'Animal Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (11, 1106, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1107, 'Fruit & Vegetable Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (11, 1107, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1108, 'Drinks', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (11, 1108, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (12, 'Furniture', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Furniture.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (120, 12, 1, 'Furniture', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (121, 12, 2, '家具家私', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (122, 12, 3, 'Muebles', 'ReferenceDataLoad', CURRENT_TIMESTAMP); 
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (120, 120, 14, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (121, 121, 18, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (122, 122, 21, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1201, 'Antique Furniture', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (12, 1201, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1202, 'Folding Furniture', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (12, 1202, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1203, 'Commercial Furniture', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (12, 1203, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1204, 'Furniture Accessories', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (12, 1204, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1205, 'Upholstery Fabric', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (12, 1205, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1206, 'Outdoor Furniture', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (12, 1206, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1207, 'Wood Furniture', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (12, 1207, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1208, 'Glass Furniture', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (12, 1208, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1209, 'Metal Furniture', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (12, 1209, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1210, 'Kids Furniture', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (12, 1210, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1211, 'Home Furniture', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (12, 1211, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (13, 'Health & Medical', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Health & Medical.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (130, 13, 1, 'Health & Medical', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (131, 13, 2, '医疗保健', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (132, 13, 3, 'Salud & Medicina', 'ReferenceDataLoad', CURRENT_TIMESTAMP); 
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (130, 130, 15, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (131, 131, 17, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (132, 132, 26, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1301, 'Dental Equipment', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (13, 1301, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1302, 'Healthcare Supplies', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (13, 1302, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1303, 'Medical Equipment', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (13, 1303, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1304, 'Medical Software', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (13, 1304, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1305, 'Medicine', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (13, 1305, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1306, 'Physical Therapy Equipment', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (13, 1306, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (14, 'Home & Garden', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Home & Garden.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (140, 14, 1, 'Home & Garden', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (141, 14, 2, '家居园艺', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (142, 14, 3, 'Hogar & Jardín', 'ReferenceDataLoad', CURRENT_TIMESTAMP); 
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (140, 140, 16, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (141, 141, 22, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (142, 142, 13, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1401, 'Barware', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (14, 1401, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1402, 'Bathroom Supplies', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (14, 1402, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1403, 'Garden Supplies', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (14, 1403, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1404, 'Home Decor', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (14, 1404, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1405, 'Home Storage & Organization', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (14, 1405, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1406, 'Home Textiles', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (14, 1406, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1407, 'Household Products & Tools', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (14, 1407, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1408, 'Kitchenware', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (14, 1408, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1409, 'Other Home & Garden', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (14, 1409, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1410, 'Tableware', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (14, 1410, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (15, 'Lights & Lighting', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Lights & Lighting.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (150, 15, 1, 'Lights & Lighting', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (151, 15, 2, '灯光照明', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (152, 15, 3, 'Luces & Iluminación', 'ReferenceDataLoad', CURRENT_TIMESTAMP); 
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (150, 150, 17, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (151, 151, 21, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (152, 152, 17, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1501, 'Light Fixtures', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (15, 1501, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1502, 'Other Lights & Lighting Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (15, 1502, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (16, 'Luggage, Bags & Cases', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Luggage, Bags & Cases.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (160, 16, 1, 'Luggage, Bags & Cases', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (161, 16, 2, '行李箱包', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (162, 16, 3, 'Equipaje,  Bolsos & Maletines', 'ReferenceDataLoad', CURRENT_TIMESTAMP); 
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (160, 160, 18, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (161, 161, 14, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (162, 162, 10, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1601, 'Business Bags & Cases', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (16, 1601, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1602, 'Handbags, Wallets & Purses', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (16, 1602, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1603, 'Luggage & Travel Bags', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (16, 1603, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1604, 'Luggage/Bags Parts & Accessories', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (16, 1604, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1605, 'Other Luggage, Bags & Cases', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (16, 1605, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1606, 'Shopping Bags', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (16, 1606, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1607, 'Sports & Leisure Bags', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (16, 1607, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (17, 'Manufacturing & Processing Machinery', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Manufacturing & Processing Machinery.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (170, 17, 1, 'Manufacturing & Processing Machinery', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (171, 17, 2, '制造加工机械', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (172, 17, 3, 'Maquinaria para Manufactura & Procesamiento', 'ReferenceDataLoad', CURRENT_TIMESTAMP); 
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (170, 170, 19, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (171, 171, 4, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (172, 172, 18, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1701, 'Construction Materials Machinery', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (17, 1701, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1702, 'Food & Beverage Processing Machinery', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (17, 1702, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1703, 'Mining & Metallurgy Machinery', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (17, 1703, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1704, 'Other Machinery', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (17, 1704, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (18, 'Minerals & Metallurgy', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Minerals & Metallurgy.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (180, 18, 1, 'Minerals & Metallurgy', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (181, 18, 2, '矿产金属', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (182, 18, 3, 'Minerales & Metales', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (180, 180, 20, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (181, 181, 3, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (182, 182, 20, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1801, 'Aluminum', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (18, 1801, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1802, 'Aluminum Scrap', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (18, 1802, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1803, 'Cast & Forged', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (18, 1803, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1804, 'Copper', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (18, 1804, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1805, 'Copper Scrap', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (18, 1805, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1806, 'Iron Scrap', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (18, 1806, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1807, 'Lime', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (18, 1807, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1808, 'Mineral & Metals Agents', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (18, 1808, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1809, 'Ore', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (18, 1809, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1810, 'Other Non-Metallic Minerals & Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (18, 1810, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (19, 'Real Estate', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Real Estate.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (190, 19, 1, 'Real Estate', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (191, 19, 2, '房地产', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (192, 19, 3, 'Inmobiliaria', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (190, 190, 21, 'Service', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (191, 191, 26, 'Service', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (192, 192, 16, 'Service', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1901, 'Apartments', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (19, 1901, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1902, 'Real Estate Projects', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (19, 1902, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1903, 'Farms', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (19, 1903, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1904, 'Hotels', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (19, 1904, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1905, 'Land', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (19, 1905, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1906, 'Office Buildings', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (19, 1906, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1907, 'Prefab Houses', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (19, 1907, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1908, 'Private Homes', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (19, 1908, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1909, 'Real Estate Agents', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (19, 1909, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (1910, 'Real Estate Projects', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (19, 1910, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (20, 'Rubber & Plastics', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Rubber & Plastics.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (200, 20, 1, 'Rubber & Plastics', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (201, 20, 2, '塑料橡胶', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (202, 20, 3, 'Goma & Plásticos', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (200, 200, 22, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (201, 201, 10, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (202, 202, 12, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2001, 'Recycled Plastic', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (20, 2001, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2002, 'Recycled Rubber', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (20, 2002, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2003, 'Rubber Processing Machinery', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (20, 2003, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2004, 'Rubber Auxiliary Agents', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (20, 2004, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2005, 'Plastic Raw Materials', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (20, 2005, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2006, 'Rubber Stocks', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (20, 2006, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2007, 'Plastic Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (20, 2007, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2008, 'Plastic Processing Machinery', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (20, 2008, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (21, 'Service Equipment', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Service Equipment.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (210, 21, 1, 'Service Equipment', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (211, 21, 2, '服务设备', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (212, 21, 3, 'Equipamiento para Prestación de Servicios', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (210, 210, 23, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (211, 211, 5, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (212, 212, 11, 'Product', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2101, 'Cargo & Storage Equipment', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (21, 2101, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2102, 'Other Service Equipment', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (21, 2102, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2103, 'Restaurant & Hotel Supplies', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (21, 2103, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2104, 'Store & Supermarket Supplies', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (21, 2104, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (22, 'Business Services', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Services.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (220, 22, 1, 'Business Services', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (221, 22, 2, '商务服务', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (222, 22, 3, 'Servicios', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (220, 220, 7, 'Service', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (221, 221, 27, 'Service', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (222, 222, 27, 'Service', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP)

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2201, 'Advertising', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (22, 2201, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2202, 'Brokerage', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (22, 2202, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2203, 'Certification', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (22, 2203, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2204, 'Company Formation', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (22, 2204, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2205, 'Computer & IT Services', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (22, 2205, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2206, 'Consulting', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (22, 2206, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2207, 'Design Services', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (22, 2207, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2208, 'Education & Training', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (22, 2208, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2209, 'Financial Services', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (22, 2209, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2210, 'Franchising', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (22, 2210, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (23, 'Shoes & Accessories', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Shoes & Accessories.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (230, 23, 1, 'Shoes & Accessories', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (231, 23, 2, '鞋子配饰', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (232, 23, 3, 'Calzado & Accesorios', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (230, 230, 24, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (231, 231, 12, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (232, 232, 6, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2301, 'Baby & Childrenï¿½s Shoes', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (23, 2301, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2302, 'Menï¿½s Shoes', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (23, 2302, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2303, 'Other Shoes & Accessories', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (23, 2303, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2304, 'Shoe Parts & Materials', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (23, 2304, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2305, 'Womenï¿½s Shoes', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (23, 2305, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (24, 'Sports & Leisure', 'ReferenceDataLoad', CURRENT_TIMESTAMP,'Sports & Leisure.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (240, 24, 1, 'Sports & Leisure', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (241, 24, 2, '运动休闲', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (242, 24, 3, 'Deportes & Ocio', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (240, 240, 25, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (241, 241, 13, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (242, 242, 8, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2401, 'Golf Clubs', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (24, 2401, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2402, 'Musical Instruments', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (24, 2402, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2403, 'Other Sports & Leisure Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (24, 2403, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2404, 'Outdoor Sports & Activities', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (24, 2404, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2405, 'Sports & Leisure Bags', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (24, 2405, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240501, 'Backpacks & Sports Bags', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2405, 240501, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240502, 'Cooler Bags', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2405, 240502, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240503, 'Other Sports & Leisure Bags', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2405, 240503, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240504, 'Picnic Bags', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2405, 240504, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240505, 'Shoulder Bags', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2405, 240505, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2406, 'Sports Balls', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (24, 2406, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240601, 'Cricket Balls', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2406, 240601, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240602, 'Football & Soccer Balls', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2406, 240602, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240603, 'Other Sports Balls', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2406, 240603, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240604, 'Tennis Balls', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2406, 240604, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2407, 'Sports Gloves', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (24, 2407, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240701, 'Boxing Gloves', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2407, 240701, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240702, 'Golf Gloves', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2407, 240702, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240703, 'Other Sports Gloves', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2407, 240703, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2408, 'Sports Helmets & Headgear', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (24, 2408, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240801, 'Biking Helmets', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2408, 240801, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240802, 'Boxing & Martial Arts Headgear', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2408, 240802, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240803, 'Other Sports Helmets', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2408, 240803, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240804, 'Ski Helmets', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2408, 240804, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2409, 'Sportswear', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (24, 2409, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240901, 'Football & Soccer Wear', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2409, 240901, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240902, 'Horse Riding Wear', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2409, 240902, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240903, 'Martial Arts Wear', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2409, 240903, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240904, 'Other Sportswear', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2409, 240904, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (240905, 'Swimwear & Beachwear', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (2409, 240905, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (25, 'Textiles & Leather Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Textiles & Leather Products.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (250, 25, 1, 'Textiles & Leather Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (251, 25, 2, '纺织皮革制品', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (252, 25, 3, 'Productos Textiles & de Cuero', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (250, 250, 26, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (251, 251, 8, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (252, 252, 23, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2501, 'Apparel Fabric', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (25, 2501, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2502, 'Fiber', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (25, 2502, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2503, 'Fur & Feather', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (25, 2503, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2504, 'Home Textiles', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (25, 2504, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2505, 'Industrial & Nonwoven Fabric', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (25, 2505, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2506, 'Leather', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (25, 2506, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2507, 'Textile Related Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (25, 2507, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2508, 'Yarn & Thread', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (25, 2508, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (26, 'Timepieces, Jewelry & Eyewear', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Timepieces, Jewelry & Eyewear.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (260, 26, 1, 'Timepieces, Jewelry & Eyewear', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (261, 26, 2, '钟表、首饰及眼镜', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (262, 26, 3, 'Relojes,  Joyería & Óptica', 'ReferenceDataLoad', CURRENT_TIMESTAMP);  
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (260, 260, 27, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (261, 261, 15, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (262, 262, 25, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP)

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2601, 'Eyewear', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (26, 2601, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2602, 'Jewelry & Gemstones', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (26, 2602, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2603, 'Watches', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (26, 2603, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (27, 'Baby & Childrens Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Toys.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (270, 27, 1, 'Baby & Childrens Products', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (271, 27, 2, '婴幼儿产品', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (272, 27, 3, 'Productos para Bebés & Niños', 'ReferenceDataLoad', CURRENT_TIMESTAMP); 
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (270, 270, 5, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (271, 271, 20, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (272, 272, 22, 'Product', true, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

  
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2701, 'Dolls', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (27, 2701, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2702, 'Educational Toys', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (27, 2702, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2703, 'Other Toys', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (27, 2703, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2704, 'Pretend Play Toys', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (27, 2704, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2705, 'Toy Animals', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (27, 2705, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2706, 'Wooden Toys', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (27, 2706, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date, product_category_link)
  VALUES (28, 'Travel Services', 'ReferenceDataLoad', CURRENT_TIMESTAMP, 'Travel Services.jpg');
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (280, 28, 1, 'Travel Services', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (281, 28, 2, 'Chinese for Travel Packages', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (282, 28, 3, 'Viajes & Turismo', 'ReferenceDataLoad', CURRENT_TIMESTAMP);    
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (280, 280, 28, 'Service', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (281, 281, 28, 'Service', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation_level_one_display_order(product_category_translation_level_one_display_order_id, product_category_translation_id, display_order, display_type, is_visible_on_home_screen, last_modified_by, last_modified_date)
  VALUES (282, 282, 28, 'Service', false, 'ReferenceDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2801, 'Adventure Tours', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (28, 2801, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2802, 'Business Travel', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (28, 2802, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2803, 'Cruises', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (28, 2803, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2804, 'Eco Travel', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (28, 2804, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2805, 'Honeymoon Specials', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (28, 2805, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2806, 'Hotels & Accommodation', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (28, 2806, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2807, 'Passenger Transportation Services', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (28, 2807, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2808, 'Resorts', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (28, 2808, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2809, 'Tours & Sightseeing', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (28, 2809, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category (product_category_id, product_category, last_modified_by, last_modified_date)
  VALUES (2810, 'Travel Packages', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_association (parent_product_category_id, child_product_category_id, last_modified_by, last_modified_date)
  VALUES (28, 2810, 'ReferenceDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (2810, 2810, 2, 'Chinese for Travel Packages', 'ReferenceDataLoad', CURRENT_TIMESTAMP);