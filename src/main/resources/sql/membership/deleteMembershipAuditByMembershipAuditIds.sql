delete from membership_audit 
where
    membership_audit_id in (:membershipAuditIds);