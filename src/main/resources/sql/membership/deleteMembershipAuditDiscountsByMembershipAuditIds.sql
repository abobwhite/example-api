delete from membership_audit_discounts 
where
    membership_audit_id in (:membershipAuditIds);