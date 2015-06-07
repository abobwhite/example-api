SELECT 
  pat.product_audit_translation_id
FROM latest_product_view AS lpv
  LEFT OUTER JOIN product_audit_translation AS pat ON lpv.product_audit_id = pat.product_audit_id
WHERE lpv.product_id = :productId
  AND lpv.event_type = 'Pending Translation'
