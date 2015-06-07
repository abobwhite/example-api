SELECT product_audit_image_link_id,
       product_audit_id,
       product_image_link,
       is_primary
  FROM product_audit_image_link
 WHERE product_audit_id IN (:snapshotIds);