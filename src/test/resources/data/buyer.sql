-- User for Buyer 99911
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (999, 'buyer99911', '$S$DwzerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (9991, 'ROLE_BUYER', 999, 'UserDataLoad', CURRENT_TIMESTAMP);

-- Buyer 99911
INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99911, '3VlkjQDP', 999, '2013-08-07', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
-- (Pending Approval data 999111)
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999111, 'Buyer', 'English Name 111', 'Chinese Name 111', 'Description 111',
          '1-10', 'Website 111', 2011, '1111-11111', 
          '111-1111', 'First 111', 'Last 111', 'Mobile 111', 'Business 111',
          'Skype 111', 'MSN 111', 'ICQ 111', 'Email 111', 'Province 111', null, 'Country 111', 'License 111',
          'Company Logo 111', 'Title 111', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999111, 2, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999111, 4, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991111, 99911, 999111, 1, 'Pending Approval', '2013-08-07 13:12:11', 'UnitTestLoad', CURRENT_TIMESTAMP);
-- (Approved data 999112)
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999112, 'Buyer', 'English Name 112', 'Chinese Name 112', 'Description 112',
          '11-20', 'Website 112', 2012, '1122-11222', 
          '112-1122', 'First 112', 'Last 112', 'Mobile 112', 'Business 112',
          'Skype 112', 'MSN 112', 'ICQ 112', 'Email 112', 'Province 112', null, 'Country 112', 'License 112',
          'Company Logo 112', 'Title 112', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999112, 2, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (999112, 7, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991121, 99911, 999112, 1, 'Approved', '2013-08-07 13:13:13', 'UnitTestLoad', CURRENT_TIMESTAMP);
