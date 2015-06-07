SELECT DISTINCT
  ppv.product_id,
  ppv.product_audit_id as snapshot_id,
  ppv.product_name, 
  ppv.product_description,
  ppv.keyword_list, 
  0 as cat_count, 
  ppv.level
      FROM published_product_view ppv
INNER JOIN product_category_product_audit as pcpa ON pcpa.product_audit_id = ppv.product_audit_id
WHERE 1 = 1 
  AND (%s)
 