SELECT pi.pending_inquiry_id,
       pi.party_id,
       pi.subject,
       pi.body,
       pi.submission_timestamp,
       pi.is_disapproved,
       lpv.party_id,
       lpv.public_party_id,
       lpv.party_type,
       lpv.user_id,
       lpv.party_audit_id,
       lpv.ip_address,
       lpv.language_id,
       lpv.year_established,
       lpv.version_number,
       lpv.event_type,
       lpv.last_modified_by,
       lpv.event_datetime,
       lpv.license_link,
       lpv.company_logo_link,
       lpv.company_name_english,
       lpv.company_name_chinese,
       lpv.company_description,
       lpv.number_of_employees,
       lpv.company_website,
       lpv.total_annual_sales,
       lpv.total_imports_us_dollars,
       lpv.first_name,
       lpv.last_name,
       lpv.mobile_phone,
       lpv.business_phone,
       lpv.skype_id,
       lpv.msn_id,
       lpv.icq_id,
       lpv.email_address,
       lpv.province,
       lpv.gender,
       lpv.title,
       lpv.country,
       lpv.registration_date
FROM pending_inquiry pi
  INNER JOIN latest_party_view lpv ON pi.party_id = lpv.party_id
