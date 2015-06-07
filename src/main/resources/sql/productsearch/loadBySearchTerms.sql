SELECT 
  ppv.product_id,
  ppv.product_audit_id as snapshot_id,
  ppv.product_name, 
  ppv.product_description,
  ppv.keyword_list, 
  (CASE WHEN cat_count IS NULL THEN 0 ELSE cat_count END) as cat_count, 
  ppv.level
FROM published_product_view ppv
  LEFT OUTER JOIN (SELECT ppv.product_id, count(pcpa.product_category_id) cat_count
                   FROM product_category_product_audit as pcpa
                     INNER JOIN product_category pc ON pc.product_category_id = pcpa.product_category_id                                
                     INNER JOIN published_product_view ppv ON pcpa.product_audit_id = ppv.product_audit_id    
                   WHERE 1 = 1 AND (%s)
                   GROUP BY ppv.product_id) as cc ON ppv.product_id = cc.product_id
 WHERE 1 = 1 
   AND (%s OR cc.cat_count > 0 )
 