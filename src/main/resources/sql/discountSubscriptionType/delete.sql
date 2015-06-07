DELETE 
  FROM discounts_subscription_type 
 WHERE discounts_id = :discountsId
   AND subscription_type_id IN (:subscriptionTypes)
