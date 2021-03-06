SELECT party_id, 
	   public_party_id,
       party_audit_id,
       ip_address,
       event_type,
       version_number,
       company_name_english, 
       company_name_chinese, 
       company_description, 
       number_of_employees, 
       company_website, 
       year_established, 
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
       country, 
       license_link,
       company_logo_link, 
       title,
       registration_date
FROM latest_buyer_view
WHERE party_id = :partyId
