SELECT 
  lsv.party_id,
  lsv.public_party_id,
  lsv.party_audit_id,
  lsv.company_description,
  pat.company_description AS company_description_translation
FROM latest_supplier_view AS lsv
  LEFT OUTER JOIN (SELECT party_audit_id, 
                         company_description 
                   FROM party_audit_translation
                   WHERE language_id = 2) AS pat ON lsv.party_audit_id = pat.party_audit_id
WHERE lsv.party_id = :supplierId
  AND lsv.event_type = 'Pending Translation'
  