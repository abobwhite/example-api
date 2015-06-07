SELECT sav.approval_id,
	   sav.public_approval_id,
       sav.snapshot_id,
       sav.title,
       sav.party_id,
       sav.approval_type,
       sav.event_type,
       sav.version_number,
       sav.last_modified_by,
       sav.event_datetime
FROM supplier_approval_view sav
WHERE sav.event_type IN ('Draft', 'Pending Approval', 'Waiting for Information')
  AND sav.approval_id = :id
