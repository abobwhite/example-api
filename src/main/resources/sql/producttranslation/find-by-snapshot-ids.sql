SELECT 
  pat.product_audit_id, 
  pat.translated_key_specification, 
  pat.translated_payment_term, 
  pat.translated_product_name, 
  pat.translated_product_description, 
  pat.translated_keyword_list, 
  pat.translated_meta_tag_list 
FROM product_audit_translation pat
  INNER JOIN language l ON pat.language_id = l.language_id
WHERE pat.product_audit_id IN (:snapshotIds)
  AND l.abbreviation = :language
