-- Supplier Pending Approval 99912
INSERT INTO party (party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99912, '2013-08-27', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
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
  
INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date)
  VALUES(25666681, 99912, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased,
                        price_at_purchase, effective_date, base_price, number_of_messages,
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry,
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email,
                        video, contact_china_direct, number_of_products, number_of_translations,
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date)
  VALUES(256666681, 2, current_date,
         2345, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 2345, 33,
         0, 1, 0, 1,
         0, 0, 0, 1,
         0, 1, 38, 1359,
         0, 0, 0,
         'None', DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 800.00, 0, 0, 12, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id,
                                     version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES(256666681, 25666681, 256666681,
         1, 'Paid', CURRENT_TIMESTAMP, 'UnitTestLoad', CURRENT_TIMESTAMP);

-- Supplier Waiting for Information 99913
INSERT INTO party (party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99913, '2013-08-27', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999131, 'Supplier', 'Waiting for Info Supplier 99913', null, 'Description 131',
          131, 'Website 131', 2012, 112000.11, 
          null, 'First 131', 'Last 131', null, null,
          'Skype 131', 'MSN 131', 'ICQ 131', 'Email 131', null, 'Female', 'Country 131', 'License 131',
          'Company Logo 131', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991311, 99913, 999131, 2, 'Waiting for Information', '2013-08-28 07:45:13', 'ModeratorUser', CURRENT_TIMESTAMP);

  INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date)
  VALUES(25666682, 99913, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased,
                        price_at_purchase, effective_date, base_price, number_of_messages,
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry,
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email,
                        video, contact_china_direct, number_of_products, number_of_translations,
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date)
  VALUES(256666682, 2, current_date,
         2345, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 2345, 33,
         0, 1, 0, 1,
         0, 0, 0, 1,
         0, 1, 38, 1359,
         0, 0, 0,
         'None', DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 800.00, 0, 0, 12, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id,
                                     version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES(256666682, 25666682, 256666682,
         1, 'Paid', CURRENT_TIMESTAMP, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
-- Product Pending Approval 88821
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (88821, 99912, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (888211, 88821, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (888212, 88821, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (88851, 'payPal', '1000 +', 
          'China', 800.00, 'United States', '4 + Weeks', 
          'XJ900', 'product is big', 'product', 'Pending Approval Product 88821', 
          'ABOB Product', 'beans, yummy', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (88861, 88821, 88851, 3, 'Pending Approval', '2013-09-12 16:46:00', 'SupplierUser', CURRENT_TIMESTAMP);

-- Product Waiting for Information 88822
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (88822, 99912, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (888221, 88822, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (888222, 88822, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (88852, 'payPal', '1000 +', 
          'China', 800.00, 'United States', '4 + Weeks', 
          'XJ900', 'product is small', 'product', 'Waiting for Info Product 88822', 
          'ABOB Product', 'beans, yummy', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (88862, 88822, 88852, 4, 'Waiting for Information', '2013-09-12 16:55:00', 'ModeratorUser', CURRENT_TIMESTAMP);
  
-- Supplier Certification Pending Approval 77711
INSERT INTO certification (certification_id, last_modified_by, last_modified_date)
  VALUES (77711, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_certification (certification_id, party_id, is_active, last_modified_by, last_modified_date)
  VALUES (77711, 99912, true, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO certification_audit (certification_audit_id, certification_standard, certification_number, certification_link, 
                                 issued_date, issued_by, last_modified_by, last_modified_date)
  VALUES (777111, 'Pend Approval Cert 77711', 'Number 77711', 'Link 77711',
          '2013-09-13', 'Issuer 77711', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO certification_audit_status (certification_audit_status_id, certification_id, certification_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (7771111, 77711, 777111, 5, 'Pending Approval', '2013-09-13 12:21:00', 'SupplierUser', CURRENT_TIMESTAMP);
  
-- Product Certification Waiting for Information 77712
INSERT INTO certification (certification_id, last_modified_by, last_modified_date)
  VALUES (77712, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO product_certification (certification_id, product_id, is_active, last_modified_by, last_modified_date)
  VALUES (77712, 88822, true, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO certification_audit (certification_audit_id, certification_standard, certification_number, certification_link, 
                                 issued_date, issued_by, last_modified_by, last_modified_date)
  VALUES (777121, 'Waiting for Info Cert 77712', 'Number 77712', 'Link 77712',
          '2013-09-14', 'Issuer 77712', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO certification_audit_status (certification_audit_status_id, certification_id, certification_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (7771211, 77712, 777121, 6, 'Waiting for Information', '2013-09-13 12:23:00', 'ModeratorUser', CURRENT_TIMESTAMP);
