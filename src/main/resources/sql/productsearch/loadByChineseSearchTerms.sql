SELECT 
  ppv.product_id,
  ppv.product_audit_id as snapshot_id,
  pat.translated_product_name as product_name, 
  pat.translated_product_description as product_description,  
  pat.translated_keyword_list as keyword_list, 
  (CASE WHEN cat_count IS NULL THEN 0 ELSE cat_count END) as cat_count, 
  ppv.level
FROM published_product_view ppv
  LEFT OUTER JOIN (SELECT ppv.product_id, count(pct.product_category_id) cat_count
                   FROM product_category_product_audit as pcpa
                     INNER JOIN product_category_translation pct ON pct.product_category_id = pcpa.product_category_id
                     INNER JOIN published_product_view ppv ON pcpa.product_audit_id = ppv.product_audit_id    
                   WHERE pct.language_id = 2 AND (%s)
                   GROUP BY ppv.product_id) as cc ON ppv.product_id = cc.product_id
  INNER JOIN (SELECT product_audit_id, 
                     translated_product_name, 
                     translated_product_description, 
                     translated_keyword_list
              FROM product_audit_translation pat
              WHERE pat.language_id = 2) AS pat ON ppv.product_audit_id = pat.product_audit_id
WHERE 1 = 1 
  AND (%s OR cc.cat_count > 0 )
