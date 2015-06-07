SELECT
  m.message_id,
  m.public_message_id,
  CASE WHEN m.to_party_id = :userPartyId THEN m.from_party_id ELSE m.to_party_id END AS party_id, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.public_party_id ELSE tlpv.public_party_id END AS public_party_id, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.company_name_english ELSE tlpv.company_name_english END AS company_name_english, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.party_type ELSE tlpv.party_type END AS party_type, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.user_id ELSE tlpv.user_id END AS user_id,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.party_audit_id ELSE tlpv.party_audit_id END AS party_audit_id, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.ip_address ELSE tlpv.ip_address END AS ip_address, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.language_id ELSE tlpv.language_id END AS language_id, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.year_established ELSE tlpv.year_established END AS year_established,  
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.version_number ELSE tlpv.version_number END AS version_number,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.event_type ELSE tlpv.event_type END AS event_type,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.last_modified_by ELSE tlpv.last_modified_by END AS last_modified_by,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.event_datetime ELSE tlpv.event_datetime END AS event_datetime, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.license_link ELSE tlpv.license_link END AS license_link, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.company_logo_link ELSE tlpv.company_logo_link END AS company_logo_link, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.company_name_chinese ELSE tlpv.company_name_chinese END AS company_name_chinese, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.company_description ELSE tlpv.company_description END AS company_description, 
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.number_of_employees ELSE tlpv.number_of_employees END AS number_of_employees,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.company_website ELSE tlpv.company_website END AS company_website,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.total_annual_sales ELSE tlpv.total_annual_sales END AS total_annual_sales,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.total_imports_us_dollars ELSE tlpv.total_imports_us_dollars END AS total_imports_us_dollars,  
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.first_name ELSE tlpv.first_name END AS first_name,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.last_name ELSE tlpv.last_name END AS last_name,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.mobile_phone ELSE tlpv.mobile_phone END AS mobile_phone,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.business_phone ELSE tlpv.business_phone END AS business_phone,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.skype_id ELSE tlpv.skype_id END AS skype_id,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.msn_id ELSE tlpv.msn_id END AS msn_id,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.icq_id ELSE tlpv.icq_id END AS icq_id,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.email_address ELSE tlpv.email_address END AS email_address,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.province ELSE tlpv.province END AS province,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.gender ELSE tlpv.gender END AS gender,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.title ELSE tlpv.title END AS title,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.country ELSE tlpv.country END AS country,
  CASE WHEN m.to_party_id = :userPartyId THEN flpv.registration_date ELSE tlpv.registration_date END AS registration_date,
  m.subject,
  m.message_type,
  CASE WHEN mf.message_id IS NOT NULL THEN TRUE ELSE FALSE END AS flagged,
  null last_interaction_sent_time,
  null all_interactions_read
FROM message m
  INNER JOIN latest_party_view flpv ON m.from_party_id = flpv.party_id
  INNER JOIN latest_party_view tlpv ON m.to_party_id = tlpv.party_id
  LEFT OUTER JOIN (SELECT message_id
                   FROM message_flag
                   WHERE party_id = :userPartyId) mf ON m.message_id = mf.message_id
WHERE m.message_id = :id
  AND m.message_type = 'Product'
