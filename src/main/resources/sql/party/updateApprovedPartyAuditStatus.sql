UPDATE party_audit_status
SET 
  event_type = 'Draft', 
  event_datetime = :last_modified_date,
  last_modified_by = :last_modified_by,
  last_modified_date = :last_modified_date
WHERE party_id = :party_id
  AND event_type = 'Approved' 