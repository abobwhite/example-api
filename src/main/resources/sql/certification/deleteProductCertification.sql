UPDATE product_certification
SET is_active = FALSE
WHERE certification_id = :certification_id;