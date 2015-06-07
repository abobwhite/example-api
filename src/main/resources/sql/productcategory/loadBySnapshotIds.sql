SELECT pcpa.product_audit_id,
       pc.product_category_id,  
       pc.product_category_link,
       pc.product_category 
       FROM product_category_product_audit as pcpa
 INNER JOIN product_category pc ON pcpa.product_category_id = pc.product_category_id
      WHERE pcpa.product_audit_id IN (:snapshotIds)
      ORDER BY pcpa.product_audit_id;
 