-- Buyer Pending Approval 99923
INSERT INTO party (party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99923, '2013-08-27', '127.0.0.1', 3, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999223, 'Buyer', 'Pending Approval Buyer 99923', null, 'Description 121',
          121, 'Website 121', 2011, 111000.11, 
          null, 'First 121', 'Last 121', null, null,
          'Skype 121', 'MSN 121', 'ICQ 121', 'Email 121', null, 'Female', 'Country 121', 'License 121',
          'Company Logo 121', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991211, 99923, 999223, 1, 'Pending Approval', '2013-08-27 16:38:12', 'BuyerUser', CURRENT_TIMESTAMP);

-- Buyer Waiting for Information 99924
INSERT INTO party (party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (99924, '2013-08-27', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999233, 'Buyer', 'Waiting for Info Buyer 99924', null, 'Description 131',
          131, 'Website 131', 2012, 112000.11, 
          null, 'First 131', 'Last 131', null, null,
          'Skype 131', 'MSN 131', 'ICQ 131', 'Email 131', null, 'Female', 'Country 131', 'License 131',
          'Company Logo 131', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9991311, 99924, 999233, 2, 'Waiting for Information', '2013-08-28 07:45:13', 'ModeratorUser', CURRENT_TIMESTAMP);