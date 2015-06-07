
-- USER SUBJECT AREA

CREATE VIEW latest_party_view
(
  party_id,
  public_party_id,
  party_type,
  user_id,
  party_audit_id,
  ip_address,
  language_id,
  year_established,
  version_number,
  event_type,
  last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  was_approved_once,
  registration_date
) AS
SELECT
  pas.party_id,
  p.public_party_id,
  pa.party_type,
  p.user_id,
  pas.party_audit_id,
  ip_address,
  language_id,
  pa.year_established,
  version_number,
  event_type,
  pas.last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  CASE 
    WHEN version_number > 1 THEN 1 
    WHEN version_number = 1 AND event_type = 'Approved' THEN 1
    ELSE 0
  END as was_approved_once,
  p.registration_date
FROM party_audit_status AS pas
  INNER JOIN party_audit AS pa ON pas.party_audit_id = pa.party_audit_id
  INNER JOIN party p ON pas.party_id = p.party_id
 WHERE pas.party_audit_status_id IN (SELECT max(lpas.party_audit_status_id) party_audit_status_id FROM party_audit_status lpas WHERE lpas.party_id = pas.party_id)
;

CREATE VIEW latest_anonymous_view
(
  party_id,
  public_party_id,
  party_audit_id,
  ip_address,
  year_established,
  version_number,
  event_type,
  last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  video_link,
  language_id,
  registration_date
) AS
SELECT
  pas.party_id,
  p.public_party_id,
  pas.party_audit_id,
  ip_address,
  pa.year_established,
  version_number,
  event_type,
  pas.last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  video_link,
  p.language_id,
  p.registration_date
FROM party_audit_status AS pas
  INNER JOIN party_audit AS pa ON pas.party_audit_id = pa.party_audit_id
  INNER JOIN party p ON pas.party_id = p.party_id
WHERE pa.party_type = 'Anonymous'
  AND pas.party_audit_status_id IN (SELECT max(lpas.party_audit_status_id) party_audit_status_id FROM party_audit_status lpas WHERE lpas.party_id = pas.party_id)
;

CREATE VIEW user_view 
(
  party_id,
  public_party_id,
  party_type,
  party_audit_id,
  ip_address,
  language_id,
  year_established,
  version_number,
  event_type,
  last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  was_approved_once,
  registration_date,
  user_id,
  username, 
  password, 
  enabled,
  failures_since_last_success,
  locked,
  role_description
) AS
SELECT 
  p.party_id,
  p.public_party_id,
  p.party_type,
  p.party_audit_id,
  p.ip_address,
  p.language_id,
  p.year_established,
  p.version_number,
  p.event_type,
  p.last_modified_by,
  p.event_datetime,
  p.license_link,
  p.company_logo_link,
  p.company_name_english,
  p.company_name_chinese,
  p.company_description,
  p.number_of_employees,
  p.company_website,
  p.total_annual_sales,
  p.total_imports_us_dollars,
  p.first_name,
  p.last_name,
  p.mobile_phone,
  p.business_phone,
  p.skype_id,
  p.msn_id,
  p.icq_id,
  p.email_address,
  p.province,
  p.gender,
  p.title,
  p.country,
  p.was_approved_once,
  p.registration_date,
  ul.user_id,
  ul.username, 
  ul.password, 
  ul.enabled,
  ul.failures_since_last_success,
  (ul.failures_since_last_success = 5) as locked,
  ur.role_description
FROM user_login AS ul
  INNER JOIN user_role AS ur ON ur.user_id = ul.user_id
  INNER JOIN latest_party_view AS p ON ul.user_id = p.user_id
;

CREATE VIEW pending_user_view 
(
  party_id,
  public_party_id,
  party_type,
  party_audit_id,
  ip_address,
  language_id,
  year_established,
  version_number,
  event_type,
  last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  was_approved_once,
  registration_date,
  user_id,
  username, 
  password, 
  enabled,
  failures_since_last_success,
  locked,
  role_description
) AS
SELECT 
  p.party_id,
  p.public_party_id,
  p.party_type,
  p.party_audit_id,
  p.ip_address,
  p.language_id,
  p.year_established,
  p.version_number,
  p.event_type,
  p.last_modified_by,
  p.event_datetime,
  p.license_link,
  p.company_logo_link,
  p.company_name_english,
  p.company_name_chinese,
  p.company_description,
  p.number_of_employees,
  p.company_website,
  p.total_annual_sales,
  p.total_imports_us_dollars,
  p.first_name,
  p.last_name,
  p.mobile_phone,
  p.business_phone,
  p.skype_id,
  p.msn_id,
  p.icq_id,
  p.email_address,
  p.province,
  p.gender,
  p.title,
  p.country,
  p.was_approved_once,
  p.registration_date,
  pu.pending_user_id as user_id,
  pu.username, 
  pu.password, 
  true AS enabled,
  0 as failures_since_last_success,
  false AS locked,
  null AS role_description
FROM pending_user AS pu
  INNER JOIN latest_party_view AS p ON pu.party_id = p.party_id
;

-- PARTY (BUSINESS/SUPPLIER/EMPLOYEE) SUBJECT AREA

CREATE VIEW employee_view 
(
  party_id,
  public_party_id,
  party_type,
  party_audit_id,
  ip_address,
  language_id,
  year_established,
  version_number,
  event_type,
  last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  was_approved_once,
  registration_date,
  user_id,
  username, 
  password, 
  enabled,
  failures_since_last_success,
  locked,
  role_description
) AS
SELECT 
  p.party_id,
  p.public_party_id,
  p.party_type,
  p.party_audit_id,
  p.ip_address,
  p.language_id,
  p.year_established,
  p.version_number,
  p.event_type,
  p.last_modified_by,
  p.event_datetime,
  p.license_link,
  p.company_logo_link,
  p.company_name_english,
  p.company_name_chinese,
  p.company_description,
  p.number_of_employees,
  p.company_website,
  p.total_annual_sales,
  p.total_imports_us_dollars,
  p.first_name,
  p.last_name,
  p.mobile_phone,
  p.business_phone,
  p.skype_id,
  p.msn_id,
  p.icq_id,
  p.email_address,
  p.province,
  p.gender,
  p.title,
  p.country,
  p.was_approved_once,
  p.registration_date,
  ul.user_id,
  ul.username, 
  ul.password, 
  ul.enabled,
  ul.failures_since_last_success,
  (ul.failures_since_last_success = 5) as locked,
  ur.role_description
FROM user_login AS ul
  INNER JOIN user_role AS ur ON ur.user_id = ul.user_id
  INNER JOIN latest_party_view AS p ON ul.user_id = p.user_id AND p.party_type = 'Internal'
;

CREATE VIEW latest_buyer_view
(
  party_id,
  public_party_id,
  party_audit_id,
  ip_address,
  year_established,
  version_number,
  event_type,
  last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  registration_date
) AS
SELECT
  pas.party_id,
  p.public_party_id,
  pas.party_audit_id,
  ip_address,
  pa.year_established,
  version_number,
  event_type,
  pas.last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  registration_date
FROM party_audit_status AS pas
  INNER JOIN party_audit AS pa ON pas.party_audit_id = pa.party_audit_id
  INNER JOIN party p ON pas.party_id = p.party_id
WHERE pa.party_type = 'Buyer'
  AND pas.party_audit_status_id IN (SELECT max(lpas.party_audit_status_id) party_audit_status_id FROM party_audit_status lpas WHERE lpas.party_id = pas.party_id)
;

CREATE VIEW approved_buyer_view
(
  party_id,
  public_party_id,
  party_audit_id,
  ip_address,
  year_established,
  version_number,
  event_type,
  last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  registration_date
) AS
SELECT
  pas.party_id,
  p.public_party_id,
  pas.party_audit_id,
  ip_address,
  pa.year_established,
  version_number,
  event_type,
  pas.last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  registration_date
FROM party_audit_status AS pas
  INNER JOIN party_audit AS pa ON pas.party_audit_id = pa.party_audit_id
  INNER JOIN party p ON pas.party_id = p.party_id
WHERE pa.party_type = 'Buyer'
  AND pas.party_audit_status_id IN (SELECT max(lapas.party_audit_status_id) FROM party_audit_status lapas WHERE lapas.party_id = pas.party_id AND lapas.event_type = 'Approved')
;

CREATE VIEW latest_supplier_view
(
  party_id,
  public_party_id,
  party_audit_id,
  ip_address,
  year_established,
  version_number,
  event_type,
  last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  video_link,
  language_id,
  registration_date
) AS
SELECT
  pas.party_id,
  p.public_party_id,
  pas.party_audit_id,
  ip_address,
  pa.year_established,
  version_number,
  event_type,
  pas.last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  video_link,
  p.language_id,
  p.registration_date
FROM party_audit_status AS pas
  INNER JOIN party_audit AS pa ON pas.party_audit_id = pa.party_audit_id
  INNER JOIN party p ON pas.party_id = p.party_id
WHERE pa.party_type = 'Supplier'
  AND pas.party_audit_status_id IN (SELECT max(lpas.party_audit_status_id) party_audit_status_id FROM party_audit_status lpas WHERE lpas.party_id = pas.party_id)
;

CREATE VIEW approved_supplier_view
(
  party_id,
  user_id,
  public_party_id,
  party_audit_id,
  ip_address,
  year_established,
  version_number,
  event_type,
  last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  video_link,
  language_id,
  registration_date
) AS
SELECT
  pas.party_id,
  p.user_id,
  p.public_party_id,
  pas.party_audit_id,
  ip_address,
  pa.year_established,
  version_number,
  event_type,
  pas.last_modified_by,
  event_datetime,
  license_link,
  company_logo_link,
  company_name_english,
  company_name_chinese,
  company_description,
  number_of_employees,
  company_website,
  total_annual_sales,
  total_imports_us_dollars,
  first_name,
  last_name,
  mobile_phone,
  business_phone,
  skype_id,
  msn_id,
  icq_id,
  email_address,
  province,
  gender,
  title,
  country,
  video_link,
  p.language_id,
  p.registration_date
FROM party_audit_status AS pas
  INNER JOIN party_audit AS pa ON pas.party_audit_id = pa.party_audit_id
  INNER JOIN party p ON pas.party_id = p.party_id
  INNER JOIN user_login ul ON ul.user_id = p.user_id
WHERE pa.party_type = 'Supplier'
  AND pas.party_audit_status_id IN (SELECT max(lapas.party_audit_status_id) FROM party_audit_status lapas WHERE lapas.party_id = pas.party_id AND lapas.event_type = 'Approved')
  AND ul.enabled = true
;

CREATE VIEW approved_translated_view
(
  party_audit_translation_id, 
  language_id, 
  party_audit_id, 
  company_description, 
  language_name, 
  abbreviation, 
  event_type, 
  party_id
) AS
SELECT
  pat.party_audit_translation_id, 
  l.language_id, 
  pas.party_audit_id, 
  pat.company_description, 
  l.language_name, 
  l.abbreviation, 
  pas.event_type, 
  p.party_id
FROM party_audit_status AS pas
  INNER JOIN party_audit_translation AS pat ON pas.party_audit_id = pat.party_audit_id
  INNER JOIN language l ON l.language_id = pat.language_id
  INNER JOIN party p ON pas.party_id = p.party_id
  INNER JOIN party_audit pa ON pa.party_audit_id  = pas.party_audit_id
WHERE pa.party_type = 'Supplier'
  AND pas.party_audit_status_id IN (SELECT max(lapas.party_audit_status_id) FROM party_audit_status lapas WHERE lapas.party_id = pas.party_id AND lapas.event_type = 'Approved')
;

-- MEMBERSHIP SUBJECT AREA

CREATE VIEW active_membership_level_view
(
  membership_level_id,
  level,
  term,
  base_price,
  number_of_product,
  number_of_messages,
  number_of_translations,
  company_showroom,
  hot_product_listing,
  product_alert,
  supplier_to_supplier_inquiry,
  export_to_china_tutorial,
  enhance_profile,
  third_party_verification,
  website_and_advanced_email,
  video,
  contact_china_direct,
  market_analysis,
  buyer_search,
  logistics_assistance,
  effective_date,
  expiration_date,
  last_modified_by,
  last_modified_date
) AS
SELECT
  m.membership_level_id,
  mt.tier as level,
  m.term,
  m.base_price,
  m.number_of_product,
  m.number_of_messages,
  m.number_of_translations,
  m.company_showroom,
  m.hot_product_listing,
  m.product_alert,
  m.supplier_to_supplier_inquiry,
  m.export_to_china_tutorial,
  m.enhance_profile,
  m.third_party_verification,
  m.website_and_advanced_email,
  m.video,
  m.contact_china_direct,
  m.market_analysis,
  m.buyer_search,
  m.logistics_assistance,
  m.effective_date,
  m.expiration_date,
  m.last_modified_by,
  m.last_modified_date
FROM membership_level as m
INNER JOIN membership_tier mt on mt.membership_tier_id = m.membership_tier_id
WHERE current_date >= m.effective_date 
  AND current_date <= m.expiration_date
;

CREATE VIEW latest_paid_membership_view
(
  party_id,
  membership_level_id,  
  level,
  membership_id,
  date_purchased,
  base_price,
  price_at_purchase,
  number_of_messages,
  company_showroom,
  hot_product_listing,
  product_alert,
  supplier_to_supplier_inquiry,
  export_to_china_tutorial,
  enhance_profile,
  third_party_verification,
  website_and_advanced_email,
  effective_date,
  video,
  contact_china_direct,
  market_analysis,
  buyer_search,
  logistics_assistance,
  price_paid,
  number_of_products,
  number_of_translations,
  expiration_date,
  payment_type,
  payment_invoice,
  renewal_discount_amount, 
  upgrade_credit_amount,
  membership_audit_id,
  event_type,
  version_number,
  term
) AS
SELECT
  m.party_id,
  ml.membership_level_id,  
  mt.tier as level,
  m.membership_id,
  ma.date_purchased,
  ma.base_price,
  ma.price_at_purchase,
  ma.number_of_messages,
  ma.company_showroom,
  ma.hot_product_listing,
  ma.product_alert,
  ma.supplier_to_supplier_inquiry,
  ma.export_to_china_tutorial,
  ma.enhance_profile,
  ma.third_party_verification,
  ma.website_and_advanced_email,
  ma.effective_date,
  ma.video,
  ma.contact_china_direct,
  ma.market_analysis,
  ma.buyer_search,
  ma.logistics_assistance,
  ma.price_paid,
  ma.number_of_products,
  ma.number_of_translations,
  ma.expiration_date,
  ma.payment_type,
  ma.payment_invoice,
  ma.renewal_discount_amount, 
  ma.upgrade_credit_amount,
  ma.membership_audit_id,
  mas.event_type,
  mas.version_number,
  ma.term
FROM membership as m
  INNER JOIN membership_audit_status as mas ON m.membership_id = mas.membership_id
  INNER JOIN membership_audit as ma ON mas.membership_audit_id = ma.membership_audit_id
  INNER JOIN membership_level as ml ON ma.membership_level_id = ml.membership_level_id
  INNER JOIN membership_tier as mt ON ml.membership_tier_id = mt.membership_tier_id 
WHERE mas.membership_id IN (SELECT max(mrm.membership_id) membership_id FROM membership mrm INNER JOIN membership_audit_status lmas on lmas.membership_id = mrm.membership_id and lmas.event_type='Paid' WHERE mrm.party_id = m.party_id)
  AND mas.membership_audit_status_id IN (SELECT max(lmas.membership_audit_status_id) membership_audit_status_id FROM membership_audit_status lmas WHERE lmas.membership_id = mas.membership_id) 
;

CREATE VIEW active_paid_membership_view
(
   party_id,
   membership_level_id,  
   level,
   membership_id,
   date_purchased,
   base_price,
   price_at_purchase,
   number_of_messages,
   company_showroom,
   hot_product_listing,
   product_alert,
   supplier_to_supplier_inquiry,
   export_to_china_tutorial,
   enhance_profile,
   third_party_verification,
   website_and_advanced_email,
   effective_date,
   video,
   contact_china_direct,
   market_analysis,
   buyer_search,
   logistics_assistance,
   price_paid,
   number_of_products,
   number_of_translations,
   expiration_date,
   payment_type,
   payment_invoice,
   renewal_discount_amount, 
   upgrade_credit_amount,
   membership_audit_id,
   event_type,
   version_number,
   term
) AS
SELECT
   m.party_id,
   ml.membership_level_id,  
   mt.tier as level,
   m.membership_id,
   ma.date_purchased,
   ma.base_price,
   ma.price_at_purchase,
   ma.number_of_messages,
   ma.company_showroom,
   ma.hot_product_listing,
   ma.product_alert,
   ma.supplier_to_supplier_inquiry,
   ma.export_to_china_tutorial,
   ma.enhance_profile,
   ma.third_party_verification,
   ma.website_and_advanced_email,
   ma.effective_date,
   ma.video,
   ma.contact_china_direct,
   ma.market_analysis,
   ma.buyer_search,
   ma.logistics_assistance,
   ma.price_paid,
   ma.number_of_products,
   ma.number_of_translations,
   ma.expiration_date,
   ma.payment_type,
   ma.payment_invoice,
   ma.renewal_discount_amount, 
   ma.upgrade_credit_amount,
   ma.membership_audit_id,
   mas.event_type,
   mas.version_number,
   ma.term
FROM membership as m
INNER JOIN membership_audit_status as mas ON m.membership_id = mas.membership_id
INNER JOIN membership_audit as ma ON mas.membership_audit_id = ma.membership_audit_id
INNER JOIN membership_level as ml ON ma.membership_level_id = ml.membership_level_id
INNER JOIN membership_tier as mt ON ml.membership_tier_id = mt.membership_tier_id 
WHERE mas.membership_id IN (SELECT mrm.membership_id FROM membership mrm INNER JOIN membership_audit_status lmas on lmas.membership_id = mrm.membership_id WHERE mrm.party_id = m.party_id)
  AND mas.membership_audit_status_id IN (SELECT max(lmas.membership_audit_status_id) membership_audit_status_id FROM membership_audit_status lmas WHERE lmas.membership_id = mas.membership_id)  
  AND current_date >= ma.effective_date 
  AND current_date <= ma.expiration_date
  AND event_type='Paid' 
;

CREATE VIEW provisional_membership_view
(
  party_id,
  membership_level_id,  
  level,
  membership_id,
  date_purchased,
  base_price,
  price_at_purchase,
  number_of_messages,
  company_showroom,
  hot_product_listing,
  product_alert,
  supplier_to_supplier_inquiry,
  export_to_china_tutorial,
  enhance_profile,
  third_party_verification,
  website_and_advanced_email,
  effective_date,
  video,
  contact_china_direct,
  market_analysis,
  buyer_search,
  logistics_assistance,
  price_paid,
  number_of_products,
  number_of_translations,
  expiration_date,
  payment_type,
  payment_invoice,
  renewal_discount_amount, 
  upgrade_credit_amount,
  membership_audit_id,
  event_type,
  version_number,
  term
) AS
SELECT
  m.party_id,
  ml.membership_level_id,  
  mt.tier as level,
  m.membership_id,
  ma.date_purchased,
  ma.base_price,
  ma.price_at_purchase,
  ma.number_of_messages,
  ma.company_showroom,
  ma.hot_product_listing,
  ma.product_alert,
  ma.supplier_to_supplier_inquiry,
  ma.export_to_china_tutorial,
  ma.enhance_profile,
  ma.third_party_verification,
  ma.website_and_advanced_email,
  ma.effective_date,
  ma.video,
  ma.contact_china_direct,
  ma.market_analysis,
  ma.buyer_search,
  ma.logistics_assistance,
  ma.price_paid,
  ma.number_of_products,
  ma.number_of_translations,
  ma.expiration_date,
  ma.payment_type,
  ma.payment_invoice,
  ma.renewal_discount_amount, 
  ma.upgrade_credit_amount,
  ma.membership_audit_id,
  mas.event_type,
  mas.version_number,
  ma.term
FROM membership as m
  INNER JOIN membership_audit_status as mas ON m.membership_id = mas.membership_id
  INNER JOIN membership_audit as ma ON mas.membership_audit_id = ma.membership_audit_id
  INNER JOIN membership_level as ml ON ma.membership_level_id = ml.membership_level_id
  INNER JOIN membership_tier as mt ON ml.membership_tier_id = mt.membership_tier_id 
WHERE mas.membership_id IN (SELECT max(mrm.membership_id) membership_id FROM membership mrm INNER JOIN membership_audit_status lmas on lmas.membership_id = mrm.membership_id WHERE mrm.party_id = m.party_id and (mas.event_type = 'Provisional' or mas.event_type = 'Draft')
  AND mas.membership_audit_status_id IN (SELECT max(lmas.membership_audit_status_id) membership_audit_status_id FROM membership_audit_status lmas WHERE lmas.membership_id = mas.membership_id) 
  AND (mas.event_type = 'Provisional' or mas.event_type = 'Draft'))
;
  
-- PRODUCT SUBJECT AREA

CREATE VIEW latest_product_view
(
  product_id,
  product_audit_id,
  party_id,
  public_party_id,
  version_number,
  event_type, 
  event_datetime,
  last_modified_by,
  payment_term,
  minimum_order,
  freight_on_board_port,
  freight_on_board_price,
  country_of_origin,
  lead_time,
  model_number,
  key_specification,
  meta_tag_list,
  product_name,
  product_description,
  keyword_list,
  is_hot_product,
  is_published,
  certification_id,
  email_address,
  publication_datetime,
  membership_id,
  hot_product_override	
) AS
SELECT
  p.product_id,
  pa.product_audit_id,
  p.party_id,
  lsv.public_party_id,
  pas.version_number,
  pas.event_type, 
  pas.event_datetime,
  pas.last_modified_by,
  pa.payment_term,
  pa.minimum_order,
  pa.freight_on_board_port,
  pa.freight_on_board_price,
  pa.country_of_origin,
  pa.lead_time,
  pa.model_number,
  pa.key_specification,
  pa.meta_tag_list,
  pa.product_name,
  pa.product_description,
  pa.keyword_list,
  hp.hot_product,
  pp.is_published,
  pc.certification_id,
  lsv.email_address,
  pp.event_datetime,
  pm.membership_id,
  CASE WHEN hpo.hot_product_override_id IS NULL THEN FALSE ELSE TRUE END AS hot_product_override
FROM product_audit_status AS pas
  INNER JOIN product_audit AS pa ON pas.product_audit_id = pa.product_audit_id
  INNER JOIN product AS p ON pas.product_id = p.product_id
  INNER JOIN hot_product_status AS hp ON p.product_id = hp.product_id
  INNER JOIN published_product_status AS pp ON p.product_id = pp.product_id
  INNER JOIN latest_supplier_view lsv ON p.party_id = lsv.party_id
  LEFT OUTER JOIN product_membership AS pm ON pm.product_id = p.product_id
  LEFT OUTER JOIN product_certification AS pc ON p.product_id = pc.product_id
  LEFT OUTER JOIN hot_product_override as hpo ON p.product_id = hpo.product_id
WHERE pas.product_audit_status_id IN (SELECT max(lpas.product_audit_status_id) product_audit_status_id FROM product_audit_status lpas WHERE lpas.product_id = p.product_id)
  AND hp.hot_product_id IN (SELECT max(lhp.hot_product_id) hot_product_id FROM hot_product_status lhp WHERE lhp.product_id = hp.product_id)
  AND pp.published_product_id IN (SELECT max(lpp.published_product_id) published_product_id FROM published_product_status lpp WHERE lpp.product_id = pp.product_id)
;

CREATE VIEW published_product_view
(
  product_id,
  product_audit_id,
  party_id,
  public_party_id,
  version_number,
  event_type, 
  event_datetime,
  last_modified_by,
  payment_term,
  minimum_order,
  freight_on_board_port,
  freight_on_board_price,
  country_of_origin,
  lead_time,
  model_number,
  key_specification,
  meta_tag_list,
  product_name,
  product_description,
  keyword_list,
  is_hot_product,
  is_published,
  certification_id,
  party_audit_id,
  level,
  publication_datetime,
  hot_product_listing,
  hot_product_override
) AS
SELECT
  p.product_id,
  pa.product_audit_id,
  p.party_id,
  apv.public_party_id,
  pas.version_number,
  pas.event_type, 
  pas.event_datetime,
  pas.last_modified_by,
  pa.payment_term,
  pa.minimum_order,
  pa.freight_on_board_port,
  pa.freight_on_board_price,
  pa.country_of_origin,
  pa.lead_time,
  pa.model_number,
  pa.key_specification,
  pa.meta_tag_list,
  pa.product_name,
  pa.product_description,
  pa.keyword_list,
  hp.hot_product,
  pp.is_published,
  pc.certification_id,
  apv.party_audit_id,
  apmv.level,
  pp.event_datetime,
  apmv.hot_product_listing,
  CASE WHEN hpo.hot_product_override_id IS NULL THEN FALSE ELSE TRUE END AS hot_product_override
FROM product_audit_status AS pas
  INNER JOIN product_audit AS pa ON pas.product_audit_id = pa.product_audit_id
  INNER JOIN product AS p ON pas.product_id = p.product_id
  INNER JOIN hot_product_status AS hp ON p.product_id = hp.product_id
  INNER JOIN published_product_status AS pp ON p.product_id = pp.product_id
  INNER JOIN approved_supplier_view apv ON p.party_id = apv.party_id
  INNER JOIN latest_paid_membership_view apmv ON p.party_id = apmv.party_id
  LEFT OUTER JOIN product_certification AS pc ON p.product_id = pc.product_id
  LEFT OUTER JOIN hot_product_override as hpo ON p.product_id = hpo.product_id
WHERE pas.product_audit_status_id IN (SELECT max(lapas.product_audit_status_id) product_audit_status_id FROM product_audit_status lapas WHERE lapas.product_id = p.product_id AND lapas.event_type = 'Approved')
  AND hp.hot_product_id IN (SELECT max(lhp.hot_product_id) hot_product_id FROM hot_product_status lhp WHERE lhp.product_id = hp.product_id)
  AND pp.published_product_id IN (SELECT max(lppp.published_product_id) published_product_id FROM published_product_status lppp WHERE lppp.product_id = pp.product_id)
  AND pp.is_published = TRUE
;

CREATE VIEW approved_product_view
(
  product_id,
  product_audit_id,
  party_id,
  public_party_id,
  version_number,
  event_type, 
  event_datetime,
  last_modified_by,
  payment_term,
  minimum_order,
  freight_on_board_port,
  freight_on_board_price,
  country_of_origin,
  lead_time,
  model_number,
  key_specification,
  meta_tag_list,
  product_name,
  product_description,
  keyword_list,
  is_hot_product,
  is_published,
  certification_id,
  party_audit_id,
  level,
  publication_datetime,
  hot_product_listing,
  hot_product_override
) AS
SELECT
  p.product_id,
  pa.product_audit_id,
  p.party_id,
  apv.public_party_id,
  pas.version_number,
  pas.event_type, 
  pas.event_datetime,
  pas.last_modified_by,
  pa.payment_term,
  pa.minimum_order,
  pa.freight_on_board_port,
  pa.freight_on_board_price,
  pa.country_of_origin,
  pa.lead_time,
  pa.model_number,
  pa.key_specification,
  pa.meta_tag_list,
  pa.product_name,
  pa.product_description,
  pa.keyword_list,
  hp.hot_product,
  pp.is_published,
  pc.certification_id,
  apv.party_audit_id,
  apmv.level,
  pp.event_datetime,
  apmv.hot_product_listing,
  CASE WHEN hpo.hot_product_override_id IS NULL THEN FALSE ELSE TRUE END AS hot_product_override
FROM product_audit_status AS pas
  INNER JOIN product_audit AS pa ON pas.product_audit_id = pa.product_audit_id
  INNER JOIN product AS p ON pas.product_id = p.product_id
  INNER JOIN hot_product_status AS hp ON p.product_id = hp.product_id
  INNER JOIN published_product_status AS pp ON p.product_id = pp.product_id
  INNER JOIN approved_supplier_view apv ON p.party_id = apv.party_id
  INNER JOIN latest_paid_membership_view apmv ON p.party_id = apmv.party_id
  LEFT OUTER JOIN product_certification AS pc ON p.product_id = pc.product_id
  LEFT OUTER JOIN hot_product_override as hpo ON p.product_id = hpo.product_id
WHERE pas.product_audit_status_id IN (SELECT max(lapas.product_audit_status_id) product_audit_status_id FROM product_audit_status lapas WHERE lapas.product_id = p.product_id AND lapas.event_type = 'Approved')
  AND hp.hot_product_id IN (SELECT max(lhp.hot_product_id) hot_product_id FROM hot_product_status lhp WHERE lhp.product_id = hp.product_id)
  AND pp.published_product_id IN (SELECT max(lppp.published_product_id) published_product_id FROM published_product_status lppp WHERE lppp.product_id = pp.product_id)
;

CREATE VIEW last_hot_product_change_view
(
  hot_product_id,
  product_id,
  hot_product
) AS
SELECT 
  max(hot_product_id) hot_product_id, 
  product_id, 
  hot_product
FROM hot_product_status 
GROUP by product_id, hot_product
;

-- CERTIFICATION SUBJECT AREA

CREATE VIEW latest_certification_view
(
  certification_id,
  certification_audit_id,
  party_id,
  is_active,
  version_number,
  event_type, 
  event_datetime,
  last_modified_by,
  certification_standard,
  certification_number,
  certification_link,
  issued_date,
  issued_by,
  scope_range
) AS
SELECT
  c.certification_id,
  ca.certification_audit_id,
  CASE WHEN pc.party_id IS NULL THEN p.party_id ELSE pc.party_id END AS party_id,
  CASE WHEN pc.is_active IS NULL THEN prc.is_active ELSE pc.is_active END AS is_active,
  cas.version_number,
  cas.event_type, 
  cas.event_datetime,
  cas.last_modified_by,
  ca.certification_standard,
  ca.certification_number,
  ca.certification_link,
  ca.issued_date,
  ca.issued_by,
  ca.scope_range
FROM certification_audit_status AS cas
  INNER JOIN certification_audit AS ca ON cas.certification_audit_id = ca.certification_audit_id
  INNER JOIN certification AS c ON cas.certification_id = c.certification_id
  LEFT OUTER JOIN party_certification AS pc ON c.certification_id = pc.certification_id
  LEFT OUTER JOIN product_certification AS prc ON c.certification_id = prc.certification_id
  LEFT OUTER JOIN product AS p ON prc.product_id = p.product_id  
WHERE cas.certification_audit_status_id IN (SELECT max(lcas.certification_audit_status_id) certification_audit_status_id FROM certification_audit_status lcas WHERE lcas.certification_id = cas.certification_id)
;

CREATE VIEW approved_certification_view
(
  certification_id,
  certification_audit_id,
  party_id,
  is_active,
  version_number,
  event_type, 
  event_datetime,
  last_modified_by,
  certification_standard,
  certification_number,
  certification_link,
  issued_date,
  issued_by,
  scope_range
) AS
SELECT
  c.certification_id,
  ca.certification_audit_id,
  CASE WHEN pc.party_id IS NULL THEN p.party_id ELSE pc.party_id END AS party_id,
  CASE WHEN pc.is_active IS NULL THEN prc.is_active ELSE pc.is_active END AS is_active,
  cas.version_number,
  cas.event_type, 
  cas.event_datetime,
  cas.last_modified_by,
  ca.certification_standard,
  ca.certification_number,
  ca.certification_link,
  ca.issued_date,
  ca.issued_by,
  ca.scope_range
FROM certification_audit_status AS cas
  INNER JOIN certification_audit AS ca ON cas.certification_audit_id = ca.certification_audit_id
  INNER JOIN certification AS c ON cas.certification_id = c.certification_id
  LEFT OUTER JOIN party_certification AS pc ON c.certification_id = pc.certification_id
  LEFT OUTER JOIN product_certification AS prc ON c.certification_id = prc.certification_id
  LEFT OUTER JOIN product AS p ON prc.product_id = p.product_id  
WHERE cas.certification_audit_status_id IN (SELECT MAX(lacas.certification_audit_status_id) FROM certification_audit_status lacas WHERE lacas.certification_id = cas.certification_id AND lacas.event_type = 'Approved' )
;

-- SUPPLIER APPROVALS AND TRANSLATIONS SUBJECT AREA

CREATE VIEW supplier_approval_view
(
  approval_id,
  public_approval_id,
  snapshot_id,
  title,
  party_id,
  approval_type,
  event_type,
  version_number,
  last_modified_by,
  event_datetime,
  language_id
) AS
SELECT 
  lsv.party_id AS approval_id,
  public_party_id AS public_approval_id,
  party_audit_id AS snapshot_id,
  company_name_english AS title,
  lsv.party_id,
  'Profile' AS approval_type,
  lsv.event_type,
  lsv.version_number,
  lsv.last_modified_by,
  lsv.event_datetime,
  language_id
FROM latest_supplier_view as lsv
UNION ALL
SELECT 
  product_id AS approval_id,
  CAST(product_id AS CHAR(20)) AS public_approval_id,
  product_audit_id AS snapshot_id,
  product_name AS title,
  party_id,
  'Product' AS approval_type,
  lpv.event_type,
  lpv.version_number,
  lpv.last_modified_by,
  lpv.event_datetime,
  NULL AS language_id
FROM latest_product_view as lpv
UNION ALL
SELECT 
  certification_id AS approval_id,
  CAST(certification_id AS CHAR(20)) AS public_approval_id,
  certification_audit_id AS snapshot_id,
  certification_standard AS title,
  lsv.party_id as party_id,
  'Certification' AS approval_type,
  lcv.event_type,
  lcv.version_number,
  lcv.last_modified_by,
  lcv.event_datetime,
  NULL AS  language_id
FROM latest_certification_view lcv
INNER JOIN latest_supplier_view lsv ON lsv.party_id = lcv.party_id
;

CREATE VIEW translatable_supplier_approvals_view
(
  approval_id,
  public_approval_id,
  snapshot_id,
  title,
  email_address,
  approval_type,
  event_type,
  version_number,
  last_modified_by,
  event_datetime,
  language_id
) AS            
SELECT 
	party_id,
	public_party_id AS public_approval_id,
	party_audit_id AS snapshot_id,
	company_name_english AS title,
	email_address,
	'Profile' AS approval_type,
	lsv.event_type,
	lsv.version_number,
	lsv.last_modified_by,
	lsv.event_datetime,
	language_id
FROM latest_supplier_view as lsv
WHERE event_type = 'Pending Translation'
UNION ALL
SELECT 
	product_id,
	CAST(product_id AS CHAR(20)) AS public_approval_id,
	product_audit_id AS snapshot_id,
	product_name AS title,
	email_address,
	'Product' AS approval_type,
	lpv.event_type,
	lpv.version_number,
	lpv.last_modified_by,
	lpv.event_datetime,
	NULL AS language_id
FROM latest_product_view as lpv
WHERE event_type = 'Pending Translation'
;

CREATE VIEW supplier_translation_view
(
  snapshot_id,
  approval_type,
  last_modified_by,
  last_modified_date
) AS
SELECT
  party_audit_id AS snapshot_id,
  'Profile' AS approval_type,
  last_modified_by,
  last_modified_date
FROM party_audit_translation
UNION
SELECT
  product_audit_id AS snapshot_id,
  'Product' AS approval_type,
  last_modified_by,
  last_modified_date
FROM product_audit_translation
;

CREATE VIEW pending_translation_view
(
  approval_id,
  public_approval_id,
  snapshot_id,
  title,
  approval_type,
  event_type,
  version_number,
  event_datetime,
  last_modified_by,
  translated
) AS
SELECT
	sav.approval_id,
	sav.public_approval_id,
	sav.snapshot_id,
	sav.title,
	sav.approval_type,
	sav.event_type,
	sav.version_number,
	(CASE WHEN stv.last_modified_date IS NULL THEN sav.event_datetime ELSE stv.last_modified_date END) as event_datetime,
	(CASE WHEN stv.last_modified_by IS NULL THEN sav.last_modified_by ELSE stv.last_modified_by END) as last_modified_by,
	stv.snapshot_id IS NOT NULL AS translated
FROM translatable_supplier_approvals_view AS sav
  LEFT OUTER JOIN supplier_translation_view AS stv ON sav.snapshot_id = stv.snapshot_id AND sav.approval_type = stv.approval_type
;

CREATE VIEW buyer_approval_view
(
  approval_id,
  snapshot_id,
  title,
  email_address,
  event_type,
  version_number,
  last_modified_by,
  event_datetime
) AS
SELECT 
  party_id AS approval_id,
  party_audit_id AS snapshot_id,
  company_name_english AS title,
  email_address,
  lbv.event_type,
  lbv.version_number,
  lbv.last_modified_by,
  lbv.event_datetime
FROM latest_buyer_view as lbv
;
    
CREATE VIEW membership_view
(
  party_id,
  membership_level_id,  
  level,
  membership_id,
  date_purchased,
  base_price,
  price_at_purchase,
  number_of_messages,
  company_showroom,
  hot_product_listing,
  product_alert,
  supplier_to_supplier_inquiry,
  export_to_china_tutorial,
  enhance_profile,
  third_party_verification,
  website_and_advanced_email,
  effective_date,
  video,
  contact_china_direct,
  market_analysis,
  buyer_search,
  logistics_assistance,
  price_paid,
  number_of_products,
  number_of_translations,
  expiration_date,
  payment_type,
  payment_invoice,
  renewal_discount_amount, 
  upgrade_credit_amount,
  membership_audit_id,
  event_type,
  version_number,
  term
) AS
SELECT
  m.party_id,
  ml.membership_level_id,  
  mt.tier as level,
  m.membership_id,
  ma.date_purchased,
  ma.base_price,
  ma.price_at_purchase,
  ma.number_of_messages,
  ma.company_showroom,
  ma.hot_product_listing,
  ma.product_alert,
  ma.supplier_to_supplier_inquiry,
  ma.export_to_china_tutorial,
  ma.enhance_profile,
  ma.third_party_verification,
  ma.website_and_advanced_email,
  ma.effective_date,
  ma.video,
  ma.contact_china_direct,
  ma.market_analysis,
  ma.buyer_search,
  ma.logistics_assistance,
  ma.price_paid,
  ma.number_of_products,
  ma.number_of_translations,
  ma.expiration_date,
  ma.payment_type,
  ma.payment_invoice,
  ma.renewal_discount_amount, 
  ma.upgrade_credit_amount,
  ma.membership_audit_id,
  mas.event_type,
  mas.version_number,
  ma.term
FROM membership as m
  INNER JOIN membership_audit_status as mas ON m.membership_id = mas.membership_id
  INNER JOIN membership_audit as ma ON mas.membership_audit_id = ma.membership_audit_id
  INNER JOIN membership_level as ml ON ma.membership_level_id = ml.membership_level_id
  INNER JOIN membership_tier as mt ON ml.membership_tier_id = mt.membership_tier_id 
WHERE mas.membership_audit_status_id IN (SELECT max(lmas.membership_audit_status_id) membership_audit_status_id FROM membership_audit_status lmas WHERE lmas.membership_id = mas.membership_id);

CREATE VIEW buy_lead_view
(
       buy_lead_id,
       effective_date,
       expiration_date,
       buy_lead_last_modified_by,
       buy_lead_last_modified_date,
       party_id,
       public_party_id,
       party_type,
       user_id,
       party_audit_id,
       ip_address,
       language_id,
       year_established,
       version_number,
       event_type,
       last_modified_by,
       event_datetime,
       license_link,
       company_logo_link,
       company_name_english,
       company_name_chinese,
       company_description,
       number_of_employees,
       company_website,
       total_annual_sales,
       total_imports_us_dollars,
       first_name,
       last_name,
       mobile_phone,
       business_phone,
       skype_id,
       msn_id,
       icq_id,
       email_address,
       province,
       gender,
       title,
       country,
       registration_date,
       product_category_id,
       product_category,
       product_category_link
) AS
SELECT
       bl.buy_lead_id,
       bl.effective_date,
       bl.expiration_date,
       bl.last_modified_by,
       bl.last_modified_date,
       p.party_id,
       p.public_party_id,
       p.party_type,
       p.user_id,
       p.party_audit_id,
       p.ip_address,
       p.language_id,
       p.year_established,
       p.version_number,
       p.event_type,
       p.last_modified_by,
       p.event_datetime,
       p.license_link,
       p.company_logo_link,
       p.company_name_english,
       p.company_name_chinese,
       p.company_description,
       p.number_of_employees,
       p.company_website,
       p.total_annual_sales,
       p.total_imports_us_dollars,
       p.first_name,
       p.last_name,
       p.mobile_phone,
       p.business_phone,
       p.skype_id,
       p.msn_id,
       p.icq_id,
       p.email_address,
       p.province,
       p.gender,
       p.title,
       p.country,
       p.registration_date,
       pc.product_category_id,
       pc.product_category,
       pc.product_category_link
FROM buy_lead bl
 INNER JOIN latest_party_view as p on p.party_id = bl.party_id
 INNER JOIN product_category as pc on pc.product_category_id = bl.product_category_id;
