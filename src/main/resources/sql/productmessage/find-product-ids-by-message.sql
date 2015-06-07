SELECT
  product_id,
  message_id
FROM message_product
WHERE message_id IN (:messageIds)
