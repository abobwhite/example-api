-- Buyer 99911
INSERT INTO party (party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99911, '2013-08-16', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
-- (Pending Approval data 999111)
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999111, 'Supplier', 'English Name 111', null, 'Description 111',
          111, 'Website 111', 2011, 111000.11, 
          null, 'First 111', 'Last 111', null, null,
          'Skype 111', 'MSN 111', 'ICQ 111', 'Email 111', null, 'Female', 'Country 111', 'License 111',
          'Company Logo 111', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999111, 2, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999111, 4, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991111, 99911, 999111, 1, 'Pending Approval', '2013-08-16 13:12:11', 'UnitTestLoad', CURRENT_TIMESTAMP);
-- (Approved data 999112)
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999112, 'Supplier', 'English Name 112', null, 'Description 112',
          112, 'Website 112', 2012, 112000.11, 
          null, 'First 112', 'Last 112', null, null,
          'Skype 112', 'MSN 112', 'ICQ 112', 'Email 112', null, 'Female', 'Country 112', 'License 112',
          'Company Logo 112', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999112, 2, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999112, 7, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991121, 99911, 999112, 1, 'Approved', '2013-08-16 13:13:13', 'UnitTestLoad', CURRENT_TIMESTAMP);
  
  
--First Membership  
INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date) 
  VALUES(66666, 99911, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations, 
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date) 
  VALUES(666661, 1, current_date, 
         1000, DATE_SUB( CURRENT_DATE, INTERVAL 6 MONTH ), 100, 20, 
         0, 1, 0, 1, 
         0, 1, 0, 1,
         0, 0, 30, 1200, 
         0, 0, 0,
         'None', DATE_SUB ( CURRENT_DATE, INTERVAL 3 MONTH ), 800.00, 0, 0, 1, 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, 
                                     last_modified_by, last_modified_date) 
   VALUES(666666, 66666, 666661, 
         1, 'Paid', CURRENT_TIMESTAMP, 
         'UnitTestLoad', CURRENT_TIMESTAMP);  
  
--Second Membership  
INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date) 
  VALUES(77777, 99911, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations, 
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date) 
  VALUES(777771, 1, current_date, 
         1000, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 100, 20, 
         0, 1, 0, 1, 
         0, 1, 0, 1,
         0, 0, 25, 1200, 
         0, 0, 0,
         'None', DATE_ADD ( CURRENT_DATE, INTERVAL 3 MONTH ), 800.00, 0, 0, 1, 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, 
                                     last_modified_by, last_modified_date) 
   VALUES(777777, 77777, 777771, 
         1, 'Paid', CURRENT_TIMESTAMP, 
         'UnitTestLoad', CURRENT_TIMESTAMP);
         
         
--THird  Membership  in Future
INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date) 
  VALUES(88888, 99911, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations, 
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date) 
  VALUES(888888, 1, current_date, 
         1000, DATE_ADD( CURRENT_DATE, INTERVAL 6 MONTH ), 100, 20, 
         0, 1, 0, 1, 
         0, 1, 0, 1,
         0, 0, 25, 1200, 
         0, 0, 0,
         'None', DATE_ADD ( CURRENT_DATE, INTERVAL 9 MONTH ), 800.00, 0, 0, 1, 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, 
                                     last_modified_by, last_modified_date) 
   VALUES(8888888, 88888, 888888, 
         1, 'Provisional', CURRENT_TIMESTAMP, 
         'UnitTestLoad', CURRENT_TIMESTAMP);
         
INSERT INTO membership_audit_discounts (membership_audit_discounts_id, membership_audit_id, discounts_id, amount, 
                                     last_modified_by, last_modified_date) 
   VALUES(888888888, 888888, 1, 100, 
         'UnitTestLoad', CURRENT_TIMESTAMP);

  

