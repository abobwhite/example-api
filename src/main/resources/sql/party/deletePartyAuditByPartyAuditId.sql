delete from party_audit 
where
    party_audit_id in (:partyAuditIds);