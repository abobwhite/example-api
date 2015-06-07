SELECT
  ppv.product_id,
  ppv.product_audit_id,
  cpat.translated_product_name AS product_name,
  ppv.event_datetime
FROM published_product_view ppv
  INNER JOIN (SELECT product_audit_id, 
                     translated_product_name 
              FROM product_audit_translation
              WHERE language_id = 2) AS cpat ON ppv.product_audit_id = cpat.product_audit_id
