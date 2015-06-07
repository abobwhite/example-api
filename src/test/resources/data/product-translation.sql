INSERT INTO party (party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (888881, '2013-09-10', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description,
                         number_of_employees, company_website, year_established, total_annual_sales,
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone,
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link,
                         company_logo_link, video_link, title, last_modified_by, last_modified_date)
  VALUES (888123, 'Supplier', 'Pending Translation Supplier 999999', null, 'Description 324',
          324, 'Website 324', 2011, 111000.11,
          null, 'First 324', 'Last 324', null, null,
          'Skype 324', 'MSN 324', 'ICQ 324', 'Email 324', null, 'Female', 'Country 324', 'License 324',
          'Company Logo 324', 'video link', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (8883241, 888881, 888123, 1, 'Pending Translation', '2013-09-10 11:32:24', 'SupplierUser', CURRENT_TIMESTAMP);

INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (888, 888881, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order,
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time,
                           model_number, key_specification, meta_tag_list, product_name,
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (888123, 'payment term', '10000',
          'freight on board', 203.42, 'England', 'lead time',
          'model number 23', 'a key specification', 'Here''s some meta tags', 'Cool Product',
          'Cool Product Description', 'here''s some key words', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number,
                                  event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (88888, 888, 888123, 1, 'Pending Translation', '2013-09-10 11:32:24', 'SupplierUser', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (8888881, 888, false, '2013-09-10 11:32:24', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (8888882, 888, false, '2013-09-10 11:32:24', 'UnitTestLoad', CURRENT_TIMESTAMP);
