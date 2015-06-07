-- user in change password state
INSERT INTO user_login (user_id, username, password,
                        enabled, failures_since_last_success, last_modified_by, last_modified_date)
  VALUES (1717, 'changedPassword', '-0123456789',
          false, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date)
  VALUES (1011, 'ROLE_SUPPLIER', 1717, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date)
  VALUES (1011, 'ROLE_BUYER', 1717, 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO party (party_id, public_party_id, user_id,  registration_date, ip_address, language_id, legacy_uid, last_modified_by, last_modified_date)
  VALUES (2323, 'p0b8pJVj', 1717, '2013-08-07', '127.0.0.1', 1, 888, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description,
                         number_of_employees, company_website, year_established, total_annual_sales,
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone,
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link,
                         company_logo_link, video_link, title, last_modified_by, last_modified_date)
  VALUES (242424, 'Supplier', 'Changed Password', 'Password Changed Incorporated', 'We change passwords',
          '1-10', 'uww.change-password.com', 1982, 12345678.00,
          123456.78, 'Change', 'Password', '777-888-9999', '000-111-2222',
          'cp-skype', 'cp', 'cp', 'cp@changepassword.com', 'password land', 'Male', 'Albania', null,
          null, 'http://www.youtube.com/watch?v=udS-OcNtSWo', 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date)
  VALUES (2323, 7, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (172423, 2323, 242424, 1, 'Approved', '2013-08-07 19:49:00', 'UserDataLoad', CURRENT_TIMESTAMP);

-- blocked user
INSERT INTO user_login (user_id, username, password,
                        enabled, failures_since_last_success, last_modified_by, last_modified_date)
  VALUES (1818, 'Blocked', '$S$DXZe9Tr0HjTdifmfaC7O3il32DQwUJDHPOJpqelevYln.2S6gCwj',
          false, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date)
  VALUES (1011, 'ROLE_BUYER', 1818, 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, legacy_uid, last_modified_by, last_modified_date)
  VALUES (2424, 'jVgaBX0E', 1818, '2013-08-07', '127.0.0.1', 1, 888, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description,
                         number_of_employees, company_website, year_established, total_annual_sales,
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone,
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link,
                         company_logo_link, video_link, title, last_modified_by, last_modified_date)
  VALUES (252525, 'Supplier', 'Blocked', 'Blocked Chinese', 'I''m a blocked user',
          '1-10', 'http://www.blocked.com', 1982, 12345678.00,
          123456.78, 'Blocked', 'User', '777-888-9999', '000-111-2222',
          'blocked-skype', 'blocked-msn', 'blocked-icq', 'user@blocked.com', 'blockedtopia', 'Male', 'Albania', null,
          null, 'http://www.youtube.com/watch?v=udS-OcNtSWo', 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date)
  VALUES (2424, 7, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (172423, 2424, 252525, 1, 'Approved', '2013-08-07 19:49:00', 'UserDataLoad', CURRENT_TIMESTAMP);