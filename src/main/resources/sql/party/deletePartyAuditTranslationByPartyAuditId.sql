delete from party_audit_translation 
where
    party_audit_id in (:partyAuditIds);