UPDATE product_audit_translation
SET translated_key_specification = :keySpecifications,
    translated_payment_term = :paymentTerms, 
    translated_product_name = :name, 
    translated_product_description = :description, 
    translated_keyword_list = :keywords,
    translated_meta_tag_list = :metaTags
WHERE product_audit_translation_id = :updateId
