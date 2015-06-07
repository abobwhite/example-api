SELECT party_audit_id, business_type_id
FROM party_audit_business_type
WHERE party_audit_id IN (:snapshotIds)