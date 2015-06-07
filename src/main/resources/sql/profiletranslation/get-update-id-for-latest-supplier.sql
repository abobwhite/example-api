SELECT 
  pat.party_audit_translation_id
FROM latest_supplier_view AS lsv
  LEFT OUTER JOIN party_audit_translation AS pat ON lsv.party_audit_id = pat.party_audit_id
WHERE lsv.party_id = :supplierId
  AND lsv.event_type = 'Pending Translation'
  