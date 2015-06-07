-- Supplier w/o Pending Translation 999999 (needed to put one with pending translation in data.sql)
INSERT INTO party (party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (999999, '2013-09-10', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description,
                         number_of_employees, company_website, year_established, total_annual_sales,
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone,
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link,
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (999123, 'Supplier', 'Pending Translation Supplier 999999', null, 'Description 324',
          324, 'Website 324', 2011, 111000.11,
          null, 'First 324', 'Last 324', null, null,
          'Skype 324', 'MSN 324', 'ICQ 324', 'Email 324', null, 'Female', 'Country 324', 'License 324',
          'Company Logo 324', null, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9993241, 999999, 999123, 1, 'Pending Translation', '2013-09-10 11:32:24', 'SupplierUser', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (9993242, 999999, 999123, 1, 'Approved', '2013-09-10 11:32:24', 'SupplierUser', CURRENT_TIMESTAMP);
INSERT INTO party_audit_translation (party_audit_translation_id, language_id, party_audit_id, company_description, last_modified_by, last_modified_date)
  VALUES (999925, 2, 999123, 'newest translation', 'NewestTranslator', '2013-09-11 11:32:24');