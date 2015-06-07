-- EXISTING USERS, ONE FOR EACH ROLE

-- Supplier User (fordp/hoopyfrood)
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (101, 'fordp', '$S$DwyerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (1011, 'ROLE_SUPPLIER', 101, 'UserDataLoad', CURRENT_TIMESTAMP);
  
-- Buyer User (apdent/towel)
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (102, 'apdent', '$S$Df5iZ61.Eg/506ebO0ypYSuLB9QV0.dGmkiPTU9fx5KdNyjIRjqH',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (1021, 'ROLE_BUYER', 102, 'UserDataLoad', CURRENT_TIMESTAMP);

-- Supplier Moderator (zaphodb/heartofgold)
INSERT INTO user_login (user_id, username, password, 
                       enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (103, 'zaphodb', '$S$Dp5VCmJwu9IIM/Isd.faKmfjakrtk/1lAKQOUZMakUa/JR7dJsFR',
          true, 2, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (1031, 'ROLE_SUPPLIER_MODERATOR', 103, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party (party_id, public_party_id, registration_date, ip_address, language_id, user_id, last_modified_by, last_modified_date)
  VALUES (77, '94Pa430l', '2013-08-07', '127.0.0.1', 1, 103, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (77111, 'Internal', null, null, null,
          null, null, null, null, 
          null, 'Zaphod', 'Beeblebrox', null, null,
          null, null, null, 'export2chinatester+1@gmail.com', null, null, null, null,
          null, null, 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (771111, 77, 77111, 1, 'Approved', '2013-08-07 13:12:11', 'UnitTestLoad', CURRENT_TIMESTAMP);

-- Buyer Moderator (tmcmillian/whitemice)
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (104, 'tmcmillian', '$S$DXZe9Tr0HjTdifmfaC7O3il32DQwUJDHPOJpqelevYln.2S6gCwj',
          true, 1, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (1041, 'ROLE_BUYER_MODERATOR', 104, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party (party_id, public_party_id, registration_date, ip_address, language_id, user_id, last_modified_by, last_modified_date)
  VALUES (78, 'lrPpmaV6', '2013-08-07', '127.0.0.1', 1, 104, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (78111, 'Internal', null, null, null,
          null, null, null, null, 
          null, 'Tricia (AKA Trillion)', 'McMillian', null, null,
          null, null, null, 'export2chinatester+2@gmail.com', null, null, null, null,
          null, null, 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (781111, 78, 78111, 1, 'Approved', '2013-08-07 13:12:11', 'UnitTestLoad', CURRENT_TIMESTAMP);

-- Translator (babelf/inyourear)
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (105, 'babelf', '$S$DDqTxFv4db0lEQVPYwkpBbXC0K2siaPbRbpZeR2HVAVO0JWWencY',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (1051, 'ROLE_TRANSLATOR', 105, 'UserDataLoad', CURRENT_TIMESTAMP);
  
  INSERT INTO party (party_id, public_party_id, registration_date, ip_address, language_id, user_id, last_modified_by, last_modified_date)
  VALUES (79, 'AbNA7KPk', '2013-08-07', '127.0.0.1', 1, 105, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (79111, 'Internal', null, null, null,
          null, null, null, null, 
          null, 'Babel', 'Fish', null, null,
          null, null, null, 'export2chinatester+3@gmail.com', null, null, null, null,
          null, null, 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (791111, 79, 79111, 1, 'Approved', '2013-08-07 13:12:11', 'UnitTestLoad', CURRENT_TIMESTAMP);

-- Administrator (sbfast/magrathea)
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (106, 'sbfast', '$S$DcB0VojCIiXj.0T9cLQLNiMfY3Y/XE.bLuZXYaCGIVqZsw9dU7sO',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (2000, 'ROLE_ADMIN', 106, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (2001, 'ROLE_TRANSLATOR', 106, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (2002, 'ROLE_BUYER_MODERATOR', 106, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (2003, 'ROLE_SUPPLIER_MODERATOR', 106, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party (party_id, public_party_id, registration_date, ip_address, language_id, user_id, last_modified_by, last_modified_date)
  VALUES (80, 'KyVkyZ0r', '2013-08-07', '127.0.0.1', 1, 106, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (80111, 'Internal', null, null, null,
          null, null, null, null, 
          null, 'Slartibart', 'Fast', null, null,
          null, null, null, 'export2chinatester+4@gmail.com', null, null, null, null,
          null, null, 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (801111, 80, 80111, 1, 'Approved', '2013-08-07 13:12:11', 'UnitTestLoad', CURRENT_TIMESTAMP);
 
-- Sales (salesAgent/salesAreFun)
INSERT INTO user_login (user_id, username, password, 
                       enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (303, 'salesAgent', '$S$DRRqvKjE3ID6YfFDTGJmskIANJzmx9114KqPpoEcePz.JPCgRcao',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (3031, 'ROLE_SALES', 303, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party (party_id, public_party_id, registration_date, ip_address, language_id, user_id, last_modified_by, last_modified_date)
  VALUES (377, '94PaKqNl', '2013-08-07', '127.0.0.1', 1, 303, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (377111, 'Internal', null, null, null,
          null, null, null, null, 
          null, 'Sales', 'Agent', null, null,
          null, null, null, 'export2chinatester+100@gmail.com', null, null, null, null,
          null, null, 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (3771111, 377, 377111, 1, 'Approved', '2013-08-07 13:12:11', 'UnitTestLoad', CURRENT_TIMESTAMP);  
  
-- Pending Approval Supplier (hostess/Cookies1)
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (107, 'hostess', '$S$Dr3Hm8Od9X0Ux/olZ.B8Uwh6./3E1l4nPfXebovKVvMB9zcmjkTn',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (1071, 'ROLE_SUPPLIER', 107, 'UserDataLoad', CURRENT_TIMESTAMP);

-- Pending Approval Buyer
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (108, 'pendingApprovalBuyer', '$S$DwzerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg',
          false, 5, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (1081, 'ROLE_BUYER', 108, 'UserDataLoad', CURRENT_TIMESTAMP);

-- PENDING USER for Supplier 23 (hotblackd/disasterarea)
INSERT INTO pending_user (pending_user_id, username, password, 
                          party_id, confirmation_token, last_modified_by, last_modified_date) 
  VALUES(123, 'hotblackd', '$S$DP9AyuIundnCwzPO1qSJBNXPt6r.rNoyVPiwj8ZMQXKf7VQTelCu',
         81, '-1234567890', 'UnitTestLoad', CURRENT_TIMESTAMP);
         
INSERT INTO party (party_id, public_party_id, registration_date, ip_address, language_id, user_id, last_modified_by, last_modified_date)
  VALUES (81, 'QmVJm6PE', '2013-08-07', '127.0.0.1', 1, null, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (81111, 'Supplier', null, null, null,
          null, null, null, null, 
          null, null, null, null, null,
          null, null, null, 'export2chinatester+5@gmail.com', null, null, 'United States', null,
          null, null, 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (811111, 81, 81111, 1, 'Unprofiled', '2013-08-07 13:12:11', 'UnitTestLoad', CURRENT_TIMESTAMP);
         
-- Approved Expired Supplier User (cherryBo/whatitis)
INSERT INTO user_login (user_id, username, password, 
                        enabled, failures_since_last_success, last_modified_by, last_modified_date) 
  VALUES (130, 'cherryBo', '$S$D3OHlKw93csv6feqKds2YP0.YmVdJW6SB9p.5E8ie.7xsf8Kox6i',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date) 
  VALUES (1030, 'ROLE_SUPPLIER', 130, 'UserDataLoad', CURRENT_TIMESTAMP);
  
-- BUYERS, IN VARIOUS APPROVAL STATES

-- Anonymous
INSERT INTO party (party_id, public_party_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (44, 'DkP2DJNw', '2013-08-07', '127.0.0.1', 1, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
-- (Unprofiled data 999111)
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (44111, 'Anonymous', 'Laura J. Company', null, null,
          null, 'Website 111', null, null, 
          null, 'Laura', 'Jakubielski', '123-456-7891', null,
          null, null, null, 'laura@isgone.com', null, null, null, null,
          null, null, 'UnitTestLoad', CURRENT_TIMESTAMP);

INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (441111, 44, 44111, 1, 'Unprofiled', '2013-08-07 13:12:11', 'UnitTestLoad', CURRENT_TIMESTAMP);

  
-- Approved Buyer
INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (22, 'pBVQJNb4', 102, '2013-08-07', '127.0.0.1', 2, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (2201, 'Buyer', 'East India Tea Company', 'East India Tea Company in Chinese', 'Brings tea from India back to England',
          '1-10', 'www.eastindiatea.com', 1800, 12345.00, 
          123.45, 'Arthur', 'Dent', '+1112223333123', '4445556666123',
          'dent-skype', 'People use MSN?', 'People use ICQ?', 'dent@eastindiatea.com', 'Sichuan', 'Male', 'United Kingdom', null,
          null, 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (2201, 4, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (2201, 5, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (22011, 22, 2201, 1, 'Approved', '2013-08-07 19:43:00', 'UserDataLoad', CURRENT_TIMESTAMP);

-- Draft Buyer
INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (27, 'aM0nj0dR', null, '2013-09-03', '127.0.0.1', 3, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (2701, 'Buyer', 'Surts Fireware', 'Surts Fireware', 'Best fireplaces in all of Muspelheim',
          '1000+', 'http://muspelhaim.com', 9999999, 9753186.00,
          864202.46, 'Surt', 'Firegiant', '111-222-3333', '444-555-6666',
          'surtshellir', 'surtur', 'surtsey', 'surtr@muspelheim.com', null, 'Male', 'Muspelheim', null,
          null, 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (2701, 1, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (27011, 27, 2701, 1, 'Draft', '2013-09-03 15:12:00', 'UserDataLoad', CURRENT_TIMESTAMP);

-- Pending Approval Buyer
INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (28, 'p2VGOVMw', 108, '2013-11-22', '127.0.0.1', 3, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description,
                         number_of_employees, company_website, year_established, total_annual_sales,
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone,
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link,
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (2801, 'Buyer', 'Hue Buyers Inc', 'Hue Buyers Inc- Chinese', 'Hues of WWW',
          '1000+', 'http://hue-ww.com', 1900, 9753186.00,
          864202.46, 'hue', 'www', '111-222-3333', '444-555-6666',
          'hueuser', 'hueuser_msn', 'hueuser_icq', 'hue.jinko@gmail.com', null, 'Male', 'Muspelheim', null,
          null, 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date)
  VALUES (2801, 1, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (28011, 28, 2801, 1, 'Pending Approval', '2013-09-03 15:12:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
-- Buy Lead for Pending Approval Buyer Passenger Transportation Services
INSERT INTO buy_lead (buy_lead_id, party_id, product_category_id, 
                        effective_date, expiration_date, last_modified_by, last_modified_date) 
VALUES (2, 28, 2807,
          DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY), DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 'UserDataLoad', CURRENT_TIMESTAMP);

-- Waiting for Information Buyer
INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (29, 'Qm0XBVaZ', null, '2013-11-22', '127.0.0.1', 3, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description,
                         number_of_employees, company_website, year_established, total_annual_sales,
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone,
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link,
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (2901, 'Buyer', 'Hue2 Buyers Inc -Waiting', 'Hue Buyers Inc- Waiting Chinese', 'Hues of WWW Waiting',
          '1000+', 'http://hue-2ww.com', 1900, 9753186.00,
          864202.46, 'hue2', 'www', '111-222-3333', '444-555-6666',
          'hueuser2', 'hueuser_msn2', 'hueuser_icq', 'hue.jinko+2@gmail.com', 'testProvince', 'Male', 'Muspelheim', null,
          null, 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date)
  VALUES (2901, 1, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (29011, 29, 2901, 1, 'Waiting for Information', '2013-09-03 15:12:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
-- Buy Lead for Waiting for Information Buyer Travel Packages
INSERT INTO buy_lead (buy_lead_id, party_id, product_category_id, 
                        effective_date, expiration_date, last_modified_by, last_modified_date) 
VALUES (1, 29, 2810,
          DATE_SUB(CURRENT_DATE, INTERVAL 2 MONTH), DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 'UserDataLoad', CURRENT_TIMESTAMP);

-- SUPPLIERS, IN VARIOUS APPROVAL STATES

-- Approved Supplier fordp
INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, legacy_uid, last_modified_by, last_modified_date)
  VALUES (21, 'Y40dgNWM', 101, '2013-08-07', '127.0.0.1', 1, 888, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, video_link, title, last_modified_by, last_modified_date)
  VALUES (2101, 'Supplier', 'Megadodo Publications', 'Megadodo Publications', 'Publisher of The Hitch-Hikers Guide to the Galaxy.  This is a wonderful publisher that is known for publishing good books. Publisher of The Hitch-Hikers Guide to the Galaxy.  This is a wonderful publisher that is known for publishing good books.',
          '1 - 10', 'uww.megadodo.com', 1982, '$1 - $500,000', 
          123456.78, 'Ford', 'Prefect', '777-888-9999', '000-111-2222',
          'prefect-skype', 'Really? MSN?', 'Really? ICQ?', 'prefect@megadodo.com', null, 'Male', 'Albania', null,
          null, 'http://www.youtube.com/watch?v=udS-OcNtSWo', 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (2101, 7, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (21011, 21, 2101, 1, 'Approved', '2013-08-07 19:49:00', 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO membership (membership_id, party_id,  last_modified_by, last_modified_date) 
  VALUES(2166667, 21, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations, 
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date) 
  VALUES(21666667, 4, current_date, 
         1000, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 100, 20, 
         0, 8, 0, 1, 
         0, 2, 0, 1,
         0, 1, 25, 1200,
         0, 0, 0,
         'None', DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 800.00, 0, 0, 12, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, last_modified_by, last_modified_date) 
   VALUES(21666667, 2166667, 21666667, 
          1, 'Paid', CURRENT_TIMESTAMP, 'UnitTestLoad', CURRENT_TIMESTAMP)

-- Draft Supplier
-- Supplier User (fordp2/hoopyfrood)
INSERT INTO user_login (user_id, username, password,
                        enabled, failures_since_last_success, last_modified_by, last_modified_date)
  VALUES (23101, 'fordp2', 'blah1',
          true, 0, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO user_role (user_role_id, role_description, user_id, last_modified_by, last_modified_date)
  VALUES (23102, 'ROLE_SUPPLIER', 23101, 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (23, 'Jp0bwPjG', 23101, '2013-09-03', '127.0.0.1', 1, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (2301, 'Supplier', 'Milliways', 'Milliways', 'Restaurant at the end of the universe',
          '1000+', 'uww.milliways.com', 9999999, 9753186.00, 
          864202.46, 'Max', 'Quordlepleen', '111-222-3333', '444-555-6666',
          'skippy', 'musin', 'hide and seek', 'max@milliways.com', null, 'Male', 'Edge of Universe', null,
          null, 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (2301, 1, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (23011, 23, 2301, 1, 'Draft', '2013-09-03 15:12:00', 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date) 
  VALUES(2366667, 23, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations, 
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date) 
  VALUES(23666667, 2, current_date, 
         50000, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 50001, 35, 
         0, 1, 0, 1, 
         0, 0, 0, 1,
         0, 0, 50, 1500, 
         0, 0, 0, 
         'None', DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 800.00, 0, 0, 12, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, last_modified_by, last_modified_date) 
   VALUES(23666667, 2366667, 23666667, 
          1, 'Provisional', CURRENT_TIMESTAMP, 'UnitTestLoad', CURRENT_TIMESTAMP);

-- Pending Approval Supplier
INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (24, 'MjVgGVEn', 107, '2013-09-03', '127.0.0.1', 1, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (2401, 'Supplier', 'Vogon Battlefleet', 'Vogon Battlefleet', 'General contractors for bureaucracy and mayhem',
          '1000+', 'uww.vogonbattlefleet.mil', -2500, 0.00, 
          0.00, 'Jeltz', 'Prostetnic', '987-654-3210', '546-372-8190',
          'go away', 'i mean it', 'then die', 'jeltz@vogonbattlefleet.mil', null, 'Male', 'Vogsphere', null,
          null, 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (2401, 2, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (24011, 24, 2401, 1, 'Pending Approval', '2013-09-03 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);

-- Waiting for Information Supplier
INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (25, 'xbPMBVjY', null, '2013-09-08', '127.0.0.1', 1, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (2501, 'Supplier', 'Jedi Enterprises', 'Jedi Enterprises', 'Peacekeepers of a galaxy far, far away',
          '51-100', 'www.anskywlkr.com', -2500, 0.00, 
          0.00, 'Anakin', 'Skywalker', '987-654-3210', '546-372-8190',
          'go away', 'i mean it', 'then die', 'yoda@anskywlkr.com', null, 'Male', 'Tatooine', null,
          null, 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (2501, 2, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (25011, 25, 2501, 1, 'Waiting for Information', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date) 
  VALUES(2566667, 25, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations, 
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date) 
  VALUES(25666667, 3, current_date, 
         2345, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 2345, 33, 
         0, 1, 0, 1, 
         0, 2, 0, 1,
         0, 1, 38, 1359, 
         0, 0, 0,
         'None', DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 800.00, 0, 0, 12, 'UnitTestLoad', CURRENT_TIMESTAMP);
         
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, last_modified_by, last_modified_date) 
   VALUES(25666667, 2566667, 25666667, 
          1, 'Paid', CURRENT_TIMESTAMP, 'UnitTestLoad', CURRENT_TIMESTAMP);

-- Supplier w/ Pending Translation
INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (26, 'xo0wK0qL', null, '2013-09-12', '127.0.0.1', 1, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description,
                         number_of_employees, company_website, year_established, total_annual_sales,
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone,
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link,
                         company_logo_link, title, last_modified_by, last_modified_date)
  VALUES (2502, 'Supplier', 'Wyrd Fates', 'Wyrd Fates', 'What was, what is, and what is becoming',
            '1000+', 'www.threenorns.com', -2500, 0.00,
            0.00, 'Udher', 'Verdhandi-Skuld', '987-654-3210', '546-372-8190',
            'past', 'present', 'future', 'wyrdsisters@fates.com', null, 'Female', 'Asgard', null, 
            null, 'Mrs.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date)
  VALUES (2502, 2, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (25012, 26, 2502, 1, 'Pending Translation', '2013-09-12 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_translation (party_audit_translation_id, language_id, party_audit_id, company_description, last_modified_by, last_modified_date)
  VALUES (999923, 2, 2502, 'translated description', 'FatesUnitTestLoad', '2013-10-17 09:18:27');
INSERT INTO product (product_id, party_id, last_modified_by, last_modified_date)
  VALUES (9991, 26, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO legacy_product_xref (legacy_nid, product_id, last_modified_by, last_modified_date)
  VALUES (19991, 9991, 'UnitTestLoad', CURRENT_TIMESTAMP);  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order,
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time,
                           model_number, key_specification, meta_tag_list, product_name,
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (999124, 'payment term', '10000',
          'freight on board', 203.42, 'England', 'lead time',
          'model number', 'key specification', 'these are meta tags', 'product name',
          'product description', 'these are key words', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number,
                                  event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (99999, 9991, 999124, 1, 'Pending Translation', '2013-09-10 11:32:24', 'SupplierUser', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (9999991, 9991, false, '2013-09-10 11:32:24', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (9999992, 9991, false, '2013-09-10 11:32:24', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, translated_key_specification,
                                       translated_payment_term, translated_product_name, translated_product_description, translated_keyword_list,
                                       translated_meta_tag_list, last_modified_by, last_modified_date)
  VALUES (9999992, 999124, 2, 'latest translated key specification',
          'latest translated payment term', 'latest translated product name', 'latest translated product description', 'latest key words translations',
          'latest meta tags translations', 'UnitTestLoad', '2013-10-24 16:44:00');

INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date)
  VALUES(2566668, 26, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased,
                        price_at_purchase, effective_date, base_price, number_of_messages,
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry,
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email,
                        video, contact_china_direct, number_of_products, number_of_translations,
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date)
  VALUES(25666668, 2, current_date,
         2345, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 2345, 33,
         0, 1, 0, 1,
         0, 0, 0, 1,
         0, 1, 38, 1359,
         0, 0, 0,
         'None', DATE_ADD(CURRENT_DATE, INTERVAL 3 MONTH), 800.00, 0, 0, 12, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id,
                                     version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES(25666668, 2566668, 25666668,
         1, 'Paid', CURRENT_TIMESTAMP, 'UnitTestLoad', CURRENT_TIMESTAMP);
         
-- Expired Approved Supplier
INSERT INTO party (party_id, public_party_id, user_id, registration_date, ip_address, language_id, last_modified_by, last_modified_date)
  VALUES (30, '690O6NL4', 130, '2013-08-07', '127.0.0.1', 1, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit (party_audit_id, party_type, company_name_english, company_name_chinese, company_description, 
                         number_of_employees, company_website, year_established, total_annual_sales, 
                         total_imports_us_dollars, first_name, last_name, mobile_phone, business_phone, 
                         skype_id, msn_id, icq_id, email_address, province, gender, country, license_link, 
                         company_logo_link, video_link, title, last_modified_by, last_modified_date)
  VALUES (3001, 'Supplier', 'Cherry Spice', 'Cherry Spice', 'Cherry Spice is a spice company',
          '1-10', 'www.megadodo.com', 1982, 12345678.00, 
          123456.78, 'Cherry', 'Spice', '777-888-9999', '000-111-2222',
          'cherry-skype', 'Really? cherry?', 'Really? cherry?', 'cherry@spice.com', 'Small Planet', 'Male', 'United States', null,
          null, null, 'Mr.', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_business_type (party_audit_id, business_type_id, last_modified_by, last_modified_date) 
  VALUES (3001, 7, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO party_audit_status (party_audit_status_id, party_id, party_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (30011, 30, 3001, 1, 'Approved', '2013-08-07 19:49:00', 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO membership (membership_id, party_id, last_modified_by, last_modified_date) 
  VALUES(3066667, 30, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit (membership_audit_id, membership_level_id, date_purchased, 
                        price_at_purchase, effective_date, base_price, number_of_messages, 
                        company_showroom, hot_product_listing, product_alert, supplier_to_supplier_inquiry, 
                        export_to_china_tutorial, enhance_profile, third_party_verification, website_and_advanced_email, 
                        video, contact_china_direct, number_of_products, number_of_translations, 
                        market_analysis, buyer_search, logistics_assistance,
                        payment_type, expiration_date, price_paid, renewal_discount_amount, upgrade_credit_amount, term, last_modified_by, last_modified_date) 
  VALUES(30666667, 4, current_date, 
         1000, DATE_SUB( CURRENT_DATE, INTERVAL 3 MONTH ), 100, 20, 
         0, 1, 0, 1, 
         0, 2, 0, 1,
         0, 1, 25, 1200, 
         0, 0, 0,
         'None', DATE_SUB( CURRENT_DATE, INTERVAL 2 MONTH ), 800.00, 0, 0, 12, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO membership_audit_status (membership_audit_status_id, membership_id, membership_audit_id, 
                                     version_number, event_type, event_datetime, last_modified_by, last_modified_date) 
   VALUES(30666667, 3066667, 30666667, 
          1, 'Paid', CURRENT_TIMESTAMP, 'UnitTestLoad', CURRENT_TIMESTAMP)


-- Products for Approved Supplier (21)
INSERT INTO product (product_id, party_id, legacy_nid, last_modified_by, last_modified_date)
  VALUES (41, 21, 777, 'UserDataLoad', CURRENT_TIMESTAMP);
  INSERT INTO legacy_product_xref (legacy_nid, product_id, last_modified_by, last_modified_date)
  VALUES (141, 41, 'UnitTestLoad', CURRENT_TIMESTAMP);  
  
INSERT INTO product_membership(product_id, membership_id, event_datetime, last_modified_by, last_modified_date)
	VALUES (41, 2166667, CURRENT_TIMESTAMP, 'UnitTestLoad', CURRENT_TIMESTAMP);
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (51, 41, true, '2013-09-15 09:53:11', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (52, 41, true, '2013-09-26 12:34:56', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (61, 41, false, '2013-09-12 06:13:36', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (62, 41, true, '2013-09-26 12:34:56', 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (71, 'payPal', '1000 +', 
          'Mexico', 800.00, 'Mexico', '3 + Weeks', 
          'XJ900', 'Home grown and tasty.', 'mexican food, beans', 'Black Beans (ABOB)', 
          'ABOB Black Beans are made from the finest beans.  We promise you will love every spoonful of our delicious beans.  We gaurantee it.', 'Beans, Yummy in My Tummy', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_translation (product_audit_translation_id, product_audit_id, language_id, 
                                       translated_key_specification, translated_payment_term, translated_product_name, 
                                       translated_product_description, translated_keyword_list, translated_meta_tag_list, 
                                       last_modified_by, last_modified_date)
  VALUES (711, 71, 2,
          'Translation of Home grown and tasty', 'Translation of payPal', 'Translation of Black Beans (ABOB)',
          'Translation of a long description', 'Translation of Beans, Yummy in My Tummy', 'Translation of mexican food, beans', 
          'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (240601, 71, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (240602, 71, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (240702, 71, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (80, 41, 71, 1, 'Pending Translation', '2013-09-07 08:15:00', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (81, 41, 71, 1, 'Approved', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  
-- Products for Waiting for Information Supplier (25)
INSERT INTO product (product_id, party_id, legacy_nid, last_modified_by, last_modified_date)
  VALUES (51, 25, 778, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO legacy_product_xref (legacy_nid, product_id, last_modified_by, last_modified_date)
  VALUES (151, 51, 'UnitTestLoad', CURRENT_TIMESTAMP);  
  
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (61, 51, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO hot_product_status (hot_product_id, product_id, hot_product, event_datetime, last_modified_by, last_modified_date)
  VALUES (62, 51, true, CURRENT_TIMESTAMP, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (71, 51, false, DATE_SUB( CURRENT_DATE, INTERVAL 1 DAY ), 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO published_product_status (published_product_id, product_id, is_published, event_datetime, last_modified_by, last_modified_date)
  VALUES (72, 51, true, CURRENT_TIMESTAMP, 'UserDataLoad', CURRENT_TIMESTAMP);
  
INSERT INTO product_audit (product_audit_id, payment_term, minimum_order, 
                           freight_on_board_port, freight_on_board_price, country_of_origin, lead_time, 
                           model_number, key_specification, meta_tag_list, product_name, 
                           product_description, keyword_list, last_modified_by, last_modified_date)
  VALUES (81, 'payPal', '1000 +', 
          'China', 800.00, 'United States', '4 + Weeks', 
          'XJ900', 'home grown and tasty', 'mexican food, beans', 'Pinto Beans', 
          'ABOB Pinto Beans', 'beans, yummy', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_category_product_audit (product_category_id, product_audit_id, last_modified_by, last_modified_date)
  VALUES (240602, 81, 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO product_audit_status (product_audit_status_id, product_id, product_audit_id, version_number, event_type, event_datetime, last_modified_by, last_modified_date)
  VALUES (91, 51, 81, 2, 'Draft', '2013-09-08 15:23:00', 'UserDataLoad', CURRENT_TIMESTAMP);
  

-- Supplier 21 Certification
INSERT INTO certification (certification_id, last_modified_by, last_modified_date)
  VALUES (777, 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO party_certification (certification_id, party_id, is_active, last_modified_by, last_modified_date)
  VALUES  (777, 21, TRUE, 'UserDataLoad', CURRENT_TIMESTAMP);
    
INSERT INTO certification_audit (certification_audit_id, certification_standard, certification_number, certification_link,
 								  issued_date, issued_by, scope_range, last_modified_by, last_modified_date)
  VALUES  (779, 'Standard', 'cert-number', null, '2013-09-17', 'issued-by', 'scope-range!', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO certification_audit_status (certification_audit_status_id, certification_id, certification_audit_id, event_type,
                                         event_datetime, version_number, last_modified_by, last_modified_date)
  VALUES (780, 777, 779, 'Approved', '2013-09-17 15:10:00', 13, 'UserDataLoad', CURRENT_TIMESTAMP);
  
-- Product 51 Certification
INSERT INTO certification (certification_id, last_modified_by, last_modified_date)
  VALUES (666, 'UserDataLoad', CURRENT_TIMESTAMP);

INSERT INTO product_certification (certification_id, product_id, is_active, last_modified_by, last_modified_date)
  VALUES  (666, 51, TRUE, 'UserDataLoad', CURRENT_TIMESTAMP);

 INSERT INTO certification_audit (certification_audit_id, certification_standard, certification_number, certification_link,
 								  issued_date, issued_by, scope_range, last_modified_by, last_modified_date)
  VALUES  (668, 'Standard', 'cert-number', 'www.cert-link.com', '2013-09-17', 'issued-by', 'scope-range!', 'UserDataLoad', CURRENT_TIMESTAMP);
INSERT INTO certification_audit_status (certification_audit_status_id, certification_id, certification_audit_id, event_type,
                                         event_datetime, version_number, last_modified_by, last_modified_date)
  VALUES (669, 666, 668, 'Draft', '2013-09-17 15:10:00', 13, 'UserDataLoad', CURRENT_TIMESTAMP);

-- Message from Buyer 22 to Supplier 21 about Product 41
INSERT INTO message (message_id, public_message_id, from_party_id, to_party_id, subject, message_type, last_modified_by, last_modified_date)
  VALUES (222142, 'KNzl759P', 22, 21, 'Inquiry about Black Beans (ABOB)', 'Product', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO message_product (product_id, message_id, last_modified_by, last_modified_date)
  VALUES (41, 222142, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (2221411, 222142, 22, 21, 'Do you know where I can buy black beans in this galaxy? I looked at http://blackbeans.com and cannot find any.',
          '2013-11-22 15:38:00', true, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (2221412, 222142, 21, 22, 'There should be some in the fridge on the Heart of Gold',
          '2013-11-22 16:43:00', true, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (2221413, 222142, 22, 21, 'I think Trillian ate them all with her last Pan-Galactic Gargle Blaster',
          '2013-11-22 16:45:00', false, 'UnitTestLoad', CURRENT_TIMESTAMP);
          
-- Message from Buyer 29 to Supplier 21 about Product Category Travel Packages
INSERT INTO message (message_id, public_message_id, from_party_id, to_party_id, subject, message_type, last_modified_by, last_modified_date)
  VALUES (222144, 'kP2BrOOP', 29, 21, 'Inquiry about Passenger Transportation Services', 'Buy Lead', 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO message_buy_lead (buy_lead_id, message_id, last_modified_by, last_modified_date)
  VALUES (1, 222144, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (2221431, 222144, 22, 21, 'Do you know where I can find a nice Transportation? I cannot find any.',
          '2013-11-22 15:38:00', true, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (2221432, 222144, 21, 22, 'I have many to choose form.  What is is your desired destination.',
          '2013-11-22 16:43:00', true, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO interaction (interaction_id, message_id, from_party_id, to_party_id, body, 
                         sent_timestamp, read_by_receiver, last_modified_by, last_modified_date)
  VALUES (2221433, 222144, 22, 21, 'I would like to go to ABOB castle in Lake St. Louis.',
          '2013-11-22 16:45:00', false, 'UnitTestLoad', CURRENT_TIMESTAMP);
         
-- Pending inquiry for party 22
INSERT INTO pending_inquiry (pending_inquiry_id, party_id, subject, body, submission_timestamp, is_disapproved, last_modified_by, last_modified_date)
  VALUES (221, 22, 'Arthur Spam List', 'I need to survive out there in the galaxy!', '2013-12-17 16:12:00', false, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO pending_inquiry_product (pending_inquiry_id, product_id, last_modified_by, last_modified_date)
  VALUES (221, 41, 'UnitTestLoad', CURRENT_TIMESTAMP);
INSERT INTO pending_inquiry_message_tag (pending_inquiry_id, message_tag_id, last_modified_by, last_modified_date)
  VALUES (221, 2, 'UnitTestLoad', CURRENT_TIMESTAMP);
