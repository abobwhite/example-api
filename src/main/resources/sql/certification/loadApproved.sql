SELECT certification_id,
  	   certification_audit_id,
  	   party_id,
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
FROM approved_certification_view
WHERE certification_id = :certification_id
	AND is_active IS TRUE;