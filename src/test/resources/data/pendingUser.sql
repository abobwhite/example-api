INSERT INTO pending_user (pending_user_id, username, password, party_id, confirmation_token, last_modified_by, last_modified_date) 
  VALUES(999242, 'Test Username', 'Test Password', 9992421, 'Test Confirmation Token', 'UnitTestLoad', CURRENT_TIMESTAMP);

  
INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (9992421, '0d7m6QgN', null, '2013-08-07', '127.0.0.1', 2, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (99924211, 'Supplier', 'East India Tea Company', 'East India Tea Company in Chinese', 'Brings tea from India back to England',
          '1-10', 'www.eastindiatea.com', 1800, 12345.00, 
          123.45, 'Arthur', 'Dent', '+1112223333123', '4445556666123',
          'dent-skype', 'People use MSN?', 'People use ICQ?', 'pending@user.com', 'Sichuan', 'Male', 'England', null,
          null, 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
          
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (999242111, 9992421, 99924211, 1, 'Unprofiled', '2013-08-07 19:43:00', 'UserDataLoad', CURRENT_TIMESTAMP);