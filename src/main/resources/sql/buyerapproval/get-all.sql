SELECT 
  party_id,
  public_party_id,
  party_audit_id,
  company_name_english,
  first_name,
  last_name,
  email_address,
  event_type,
  version_number,
  last_modified_by,
  event_datetime
FROM latest_buyer_view
WHERE event_type IN ('Pending Approval', 'Waiting for Information')
