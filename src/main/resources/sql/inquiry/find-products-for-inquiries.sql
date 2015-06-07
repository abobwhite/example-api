SELECT pending_inquiry_id,
       product_id
FROM pending_inquiry_product
WHERE pending_inquiry_id IN (:inquiryIds)
