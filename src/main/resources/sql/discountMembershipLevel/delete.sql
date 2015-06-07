DELETE 
  FROM discounts_membership_tier 
 WHERE discounts_id = :discountsId
   AND membership_tier_id IN (:levels)
