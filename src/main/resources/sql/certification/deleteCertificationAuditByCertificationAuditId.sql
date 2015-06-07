delete from certification_audit 
where
    certification_audit_id in (:certificationAuditIds);