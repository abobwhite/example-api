-- Previously Approved Supplier with Paid Membership 99911
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (9991132843, 'fordp', '$S$DwyerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (463736, 'ROLE_SUPPLIER', 9991132843, 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO party (party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99911, 9991132843, '2013-08-16', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
-- (Last Approved)
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999112, 'Supplier', 'Approved Supplier 99911', null, 'Description 112',
          112, 'Website 112', 2012, '1122-11222', 
          null, 'First 112', 'Last 112', null, null,
          'Skype 112', 'MSN 112', 'ICQ 112', 'Email 112', null, 'Female', 'Country 112', 'License 112',
          'Company Logo 112', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999112, 2, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999112, 5, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991121, 99911, 999112, 1, 'Approved', '2013-08-16 13:13:13', 'UnitTestLoad', CURRENT_TIMESTAMP);
-- (Draft again)
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999113, 'Supplier', 'Approved Supplier 99911', null, 'Description 112',
          112, 'Website 112', 2012, '1122-11222', 
          null, 'First 112', 'Last 112', null, null,
          'Skype 112', 'MSN 112', 'ICQ 112', 'Email 112', null, 'Female', 'Country 112', 'License 112',
          'Company Logo 112', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999113, 7, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991131, 99911, 999113, 2, 'Draft', '2013-09-25 19:37:00', 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date) 
  VALUES(99911666, 99911,'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations,
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date) 
  VALUES(999116661, 3, current_date, 
         1000, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 100, 20, 
         0, 1, 0, 1, 
         0, 1, 0, 1,
         0, 1, 25, 1200, 
         0, 0, 0, 
         'None', DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 800.00, 0, 0, 12, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, last_modified_by, last_modified_date) 
   VALUES(9991166611, 99911666, 999116661, 
          1, 'Paid', CURRENT_TIMESTAMP, 'UnitTestLoad', CURRENT_TIMESTAMP)

-- Product 100
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (100, 99911, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (200, 100, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (300, 100, true, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (400, 'payPal', '1000 +', 
          'China', 800.00, 'United States', '4 + Weeks', 
          'XJ900', 'product is small', 'product', 'Red Wine', 
          'Finest in USA', 'wine, grapes', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (11, 400, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (500, 100, 400, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, translated_product_name, translated_product_description, translated_keyword_list, last_modified_by, last_modified_date)
  VALUES (600, 400, 2, 'Translated Red Wine', 'Translated Finest in USA', 'Translated wine, grapes', 'UserDataLoad', CURRENT_TIMESTAMP);
  
-- Product 101
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (101, 99911, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (201, 101, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (301, 101, true, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (401, 'payPal', '1000 +', 
          'China', 800.00, 'United States', '4 + Weeks', 
          'XJ900', 'product is small', 'product', 'Sparkling Pear Cider', 
          'Best Sparkling Wine in USA', 'cider, grapes', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (11, 401, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (501, 101, 401, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, translated_product_name, translated_product_description, translated_keyword_list, last_modified_by, last_modified_date)
  VALUES (601, 401, 2, 'Translated Sparkling Pear Cider', 'Translated Best Sparkling Wine in USA', 'Translated cider, grapes', 'UserDataLoad', CURRENT_TIMESTAMP);
  
-- Product 102
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (102, 99911, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (202, 102, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (302, 102, true, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (402, 'payPal', '1000 +', 
          'China', 800.00, 'Mexico', '4 + Weeks', 
          'XJ900', 'product is small', 'product', 'Sparkling Apple Cider', 
          'Best Sparkling Cider in Mexico', 'Wine, grapes', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (11, 402, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (502, 102, 402, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, translated_product_name, translated_product_description, translated_keyword_list, last_modified_by, last_modified_date)
  VALUES (602, 402, 2, 'Translated Sparkling Apple Cider', 'Translated Best Sparkling Cider in Mexico', 'Translated Wine, grapes', 'UserDataLoad', CURRENT_TIMESTAMP);
  
-- Product 103
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (103, 99911, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (203, 103, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (303, 103, true, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (403, 'payPal', '1000 +', 
          'China', 800.00, 'Mexico', '4 + Weeks', 
          'XJ900', 'product is small', 'product', 'Hot Dog', 
          'All Beef franks', 'Beef Food Furniture', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (11, 403, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (503, 103, 403, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, translated_product_name, translated_product_description, translated_keyword_list, last_modified_by, last_modified_date)
  VALUES (603, 403, 2, 'Translated Hot Dog', 'Translated All Beef franks', 'Translated Beef Food Furniture', 'UserDataLoad', CURRENT_TIMESTAMP);
  
  
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (700, 11, 2, 'Translated Food Category', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_translation (product_category_translation_id, product_category_id, language_id, translated_product_category, last_modified_by, last_modified_date)
  VALUES (701, 12, 2, 'Translated Furniture Category', 'ReferenceDataLoad', CURRENT_TIMESTAMP);
  
