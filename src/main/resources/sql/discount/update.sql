UPDATE discounts d
SET 
  d.discount_code = :discount_code, 
  d.discount_amount_type = :discount_amount_type, 
  d.amount = :amount, 
  d.description = :description,
  d.ongoing = :ongoing,
  d.special = :special,
  d.effective_date = :effective_date,
  d.expiration_date = :expiration_date,
  d.last_modified_by = :last_modified_by,
  d.last_modified_date = :last_modified_date
WHERE discounts_id = :discounts_id
               