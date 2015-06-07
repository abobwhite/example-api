-- Previously Approved Supplier with Paid Membership 99911
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (9991132843, 'fordp', '$S$DwyerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (463736, 'ROLE_SUPPLIER', 9991132843, 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO party (party_id, user_id, public_party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99911, 9991132843, '3VlkjQDP', '2013-08-16', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
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
  VALUES(99911666, 99911, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations,
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount,  term, last_modified_by, last_modified_date) 
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

-- Supplier 99911 Approved unpublished Product 22
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (22, 99911, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (33, 22, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (43, 22, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (52, 'payPal', '1000 +', 
          'China', 800.00, 'United States', '4 + Weeks', 
          'XJ900', 'product is small', 'product', 'A Product 2', 
          'ABOB Product', 'beans, yummy', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (12, 52, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (62, 22, 52, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_membership(product_membership_id, product_id, membership_id, event_datetime, last_modified_by, last_modified_date)
  VALUES (1, 22, 99911666, CURRENT_DATE, 'UserDataLoad', CURRENT_TIMESTAMP);

-- Supplier 99911 Approved published Product 24
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (24, 99911, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (241, 24, true, CURRENT_DATE, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (241, 24, true, '2013-09-27 10:33:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2401, '9 months', '500 - 1000', 
          'London', 586.00, 'United Kingdom', '2 Weeks', 
          '2401', 'hard, round and fast', 'cricket ball', 'Product 24', 
          'Product 24 description', 'red cricket ball', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, translated_keyword_list, last_modified_by, last_modified_date)
  VALUES (240112, 2401, 2, 'Translation of red cricket ball', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (240601, 2401, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (24011, 24, 2401, 3, 'Approved', '2013-09-25 18:46:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2402, '9 months', '500 - 1000', 
          'London', 586.00, 'United Kingdom', '2 Weeks', 
          '2401', 'hard, round and fast', 'cricket ball', 'Product 24', 
          'Product 24 description', 'red leather cricket ball', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (240601, 2402, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (24021, 24, 2402, 4, 'Draft', '2013-09-25 19:25:00', 'UserDataLoad', CURRENT_TIMESTAMP);

-- Pending Approval Supplier 99912
INSERT INTO party (party_id, public_party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99912, 'gPjx7mYN', '2013-08-27', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999121, 'Supplier', 'Pending Approval Supplier 99912', null, 'Description 121',
          121, 'Website 121', 2011, 111000.11, 
          null, 'First 121', 'Last 121', null, null,
          'Skype 121', 'MSN 121', 'ICQ 121', 'Email 121', null, 'Female', 'Country 121', 'License 121',
          'Company Logo 121', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991211, 99912, 999121, 1, 'Pending Approval', '2013-08-27 16:38:12', 'SupplierUser', CURRENT_TIMESTAMP);

-- Supplier 99912 Draft Product 21
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (21, 99912, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (32, 21, true, CURRENT_TIMESTAMP, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (42, 21, true, CURRENT_TIMESTAMP, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (51, 'payPal', '1000 +', 
          'China', 800.00, 'United States', '4 + Weeks', 
          'XJ900', 'product is big', 'product', 'Product 1', 
          'ABOB Product', 'beans, yummy', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (12, 51, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (61, 21, 51, 1, 'Draft', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);

-- Supplier 99912 Approved published Product 23
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (23, 99912, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (231, 23, false, CURRENT_DATE, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (231, 23, true, CURRENT_DATE, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2301, '12 months', '1000 +', 
          'Los Angeles', 666.00, 'United States', '2 Weeks', 
          '2301', 'skeleton keys', 'key', 'Product 23', 
          'Product 23 description', 'magic key', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (240602, 2301, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (23011, 23, 2301, 2, 'Approved', '2013-09-25 18:41:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
-- Approved Supplier with Provisional Membership 99913
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (9991132443, 'fordp', '$S$DwyerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (463726, 'ROLE_SUPPLIER', 9991132443, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party (party_id, user_id, public_party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99913, 9991132443, 'RPekAL6N', '2013-09-26', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999131, 'Supplier', 'Approved Supplier 99913', null, 'Description 113',
          113, 'Website 113', 2013, '113-1133', 
          null, 'First 113', 'Last 113', null, null,
          'Skype 113', 'MSN 113', 'ICQ 113', 'Email 113', null, 'Male', 'Country 113', 'License 113',
          'Company Logo 113', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999131, 3, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991311, 99913, 999131, 1, 'Approved', '2013-08-16 13:13:13', 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date) 
  VALUES(99913666, 99913, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations, 
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date) 
  VALUES(999136661, 4, current_date, 
         1000, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 100, 20, 
         0, 1, 0, 1, 
         0, 1, 0, 1,
         0, 1, 25, 1200, 
         0, 0, 0, 
         'None', DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 800.00, 0, 0, 12, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, last_modified_by, last_modified_date) 
   VALUES(9991366611, 99913666, 999136661, 
          1, 'Provisional', CURRENT_TIMESTAMP, 'UnitTestLoad', CURRENT_TIMESTAMP)

-- Supplier 99913 Approved published Product 25
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (25, 99913, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (251, 25, false, CURRENT_DATE, 'UserDataLoad', CURRENT_TIMESTAMP);  
INSERT INTO hot_product_override (hot_product_override_id, product_id, last_modified_by, last_modified_date)
  VALUES (251, 25, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (251, 25, true, CURRENT_DATE, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (2501, '5 months', '500 - 1000', 
          'Hobart', 123.00, 'Australia', '3 Weeks', 
          '2501', 'bouncy and green', 'tennis ball', 'Product 25', 
          'Product 25 description', 'green tennis ball', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (240604, 2501, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (25011, 25, 2501, 1, 'Approved', '2013-09-26 08:07:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
  
  
  
  
  -- Blocked Supplier with Provisional Membership 99913
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (9923132443, 'fordp', '$S$DwyerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg',
          false, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (4632323726, 'ROLE_SUPPLIER', 9923132443, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party (party_id, user_id, public_party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (9991334343, 9923132443, 'vGk0DMAe', '2013-09-26', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (9991345454541, 'Supplier', 'Approved Supplier 99913', null, 'Description 113',
          113, 'Website 113', 2013, '113-1133', 
          null, 'First 113', 'Last 113', null, null,
          'Skype 113', 'MSN 113', 'ICQ 113', 'Email 113', null, 'Male', 'Country 113', 'License 113',
          'Company Logo 113', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (9991345454541, 3, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (999189938311, 9991334343, 9991345454541, 1, 'Approved', '2013-08-16 13:13:13', 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date) 
  VALUES(999121213666, 9991334343, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations, 
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date) 
  VALUES(99913666222221, 4, current_date, 
         1000, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 100, 20, 
         0, 1, 0, 1, 
         0, 1, 0, 1,
         0, 1, 25, 1200, 
         0, 0, 0, 
         'None', DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 800.00, 0, 0, 12, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, last_modified_by, last_modified_date) 
   VALUES(99934341366611, 999121213666, 99913666222221, 
          1, 'Provisional', CURRENT_TIMESTAMP, 'UnitTestLoad', CURRENT_TIMESTAMP)

-- Supplier 99913 Approved published Product 25
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (253434, 9991334343, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (2523231, 253434, false, CURRENT_DATE, 'UserDataLoad', CURRENT_TIMESTAMP);  
INSERT INTO hot_product_override (hot_product_override_id, product_id, last_modified_by, last_modified_date)
  VALUES (2523231, 253434, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (2523231, 253434, true, CURRENT_DATE, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (25023231, '5 months', '500 - 1000', 
          'Hobart', 123.00, 'Australia', '3 Weeks', 
          '2501', 'bouncy and green', 'tennis ball', 'Product 25', 
          'Product 25 description', 'green tennis ball', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (2406045454, 25023231, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (2504545411, 253434, 25023231, 1, 'Approved', '2013-09-26 08:07:00', 'UserDataLoad', CURRENT_TIMESTAMP);

