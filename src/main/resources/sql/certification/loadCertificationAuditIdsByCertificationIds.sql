select 
    certification_audit_id
from
    certification_audit_status
where
    certification_id IN (:certificationIds);