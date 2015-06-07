-- Supplier 99911

INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (9991132843, 'fordp', '$S$DwyerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (463736, 'ROLE_SUPPLIER', 9991132843, 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO party (party_id, user_id, public_party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99911, 9991132843, '3VlkjQDP', '2013-08-16', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
-- (Pending Approval data 999111)
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999111, 'Supplier', 'Pending Approval Supplier 99911', null, 'Description 111',
          '1-10', 'Website 111', 2011, '1111-11111', 
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
  VALUES (999112, 'Supplier', 'Approved Supplier 99911', null, 'Description 112',
          '11-20', 'Website 112', 2012, '1122-11222', 
          null, 'First 112', 'Last 112', null, null,
          'Skype 112', 'MSN 112', 'ICQ 112', 'Email 112', null, 'Female', 'Country 112', 'License 112',
          'Company Logo 112', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999112, 2, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999112, 7, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_translation (party_audit_translation_id, language_id, party_audit_id, company_description, last_modified_by, last_modified_date)
  VALUES (9991129, 2, 999112, 'translated description', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991121, 99911, 999112, 1, 'Approved', '2013-08-16 13:13:13', 'UnitTestLoad', CURRENT_TIMESTAMP);
-- Supplier 99911 Membership
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
         1000, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 100, 20, 
         0, 1, 0, 1, 
         0, 1, 0, 1,
         0, 0, 25, 1200, 
         0, 0, 0, 
        'None', DATE_ADD ( CURRENT_DATE, INTERVAL 3 MONTH ), 800.00, 0, 0, 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, 
                                     last_modified_by, last_modified_date) 
   VALUES(666662, 66666, 666661, 
         1, 'Paid', CURRENT_TIMESTAMP, 
         'UnitTestLoad', CURRENT_TIMESTAMP);
 -- Supplier 99911 Provisional Membership
INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date) 
  VALUES(77777, 99911, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations, 
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date) 
  VALUES(777771, 2, current_date, 
         1000, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 100, 20, 
         0, 1, 0, 1, 
         0, 1, 0, 1,
         0, 0, 25, 1200, 
         0, 0, 0, 
        'None', DATE_ADD ( CURRENT_DATE, INTERVAL 3 MONTH ), 800.00, 0, 0, 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, 
                                     last_modified_by, last_modified_date) 
   VALUES(777772, 77777, 777771, 
         1, 'Draft', CURRENT_TIMESTAMP, 
         'UnitTestLoad', CURRENT_TIMESTAMP);        
         
-- Supplier 99911 Certification
INSERT INTO certification (certification_id, last_modified_by, last_modified_date)
  VALUES (555, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_certification (certification_id, party_id, is_active, last_modified_by, last_modified_date)
  VALUES  (555, 99911, TRUE, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO certification_audit (certification_audit_id, certification_standard, certification_number, certification_link,
                  issued_date, issued_by, scope_range, last_modified_by, last_modified_date)
  VALUES  (557, 'Standard', 'cert-number', 'www.cert-link.com', '2013-09-17', 'issued-by', 'scope-range!', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO certification_audit_status (certification_audit_status_id, certification_id, certification_audit_id, event_type,
                                         event_datetime, version_number, last_modified_by, last_modified_date)
  VALUES (558, 555, 557, 'Draft', '2013-09-17 15:10:00', 13, 'UserDataLoad', CURRENT_TIMESTAMP);
