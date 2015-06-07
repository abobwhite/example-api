delete from certification_audit_status 
where
    certification_id in (:certificationIds);