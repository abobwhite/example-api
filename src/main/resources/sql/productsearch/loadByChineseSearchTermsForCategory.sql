SELECT DISTINCT
  ppv.product_id,
  ppv.product_audit_id as snapshot_id,
  pat.translated_product_name as product_name, 
  pat.translated_product_description as product_description,  
  pat.translated_keyword_list as keyword_list, 
  0 as cat_count, 
  ppv.level
FROM published_product_view ppv
  INNER JOIN product_category_product_audit as pcpa ON pcpa.product_audit_id = ppv.product_audit_id
  INNER JOIN (SELECT product_audit_id, 
                     translated_product_name, 
                     translated_product_description, 
                     translated_keyword_list
              FROM product_audit_translation pat
              WHERE pat.language_id = 2) AS pat ON ppv.product_audit_id = pat.product_audit_id
WHERE 1 = 1 
  AND (%s)
