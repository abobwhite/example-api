delete from party_audit_business_type 
where
    party_audit_id in (:partyAuditIds);