SELECT subscription_type_id
  FROM discounts_subscription_type dst
 WHERE dst.discounts_id = :discountsId
