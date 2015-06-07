-- Anonymous 99911
INSERT INTO party (party_id, public_party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99911, '3VlkjQDP', '2013-08-07', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
-- (Unprofiled data 999111)
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999111, 'Anonymous', 'English Name 111', null, null,
          null, 'Website 111', null, null, 
          null, 'First 111', 'last_name', null, 'Mobile 111',
          null, null, null, 'Email 111', null, null, 'Country 111', null,
          null, null, 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991111, 99911, 999111, 1, 'Unprofiled', '2013-08-07 13:12:11', 'UnitTestLoad', CURRENT_TIMESTAMP);
