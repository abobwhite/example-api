SELECT ma.number_of_products - count(lpv.product_id)
FROM membership as m
  INNER JOIN membership_audit_status as mas ON m.membership_id = mas.membership_id
  INNER JOIN membership_audit as ma ON mas.membership_audit_id = ma.membership_audit_id
  LEFT OUTER JOIN product_membership as pm ON pm.membership_id = m.membership_id
  LEFT OUTER JOIN latest_product_view as lpv ON lpv.product_id = pm.product_id
WHERE mas.membership_audit_status_id IN (SELECT max(lmas.membership_audit_status_id) membership_audit_status_id FROM membership_audit_status lmas WHERE lmas.membership_id = mas.membership_id)  
  AND m.membership_id = :membershipId
GROUP BY ma.number_of_products
  
  
