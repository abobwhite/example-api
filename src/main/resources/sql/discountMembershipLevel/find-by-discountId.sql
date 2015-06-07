SELECT tier 
  FROM discounts_membership_tier dmt
INNER JOIN membership_tier mt ON mt.membership_tier_id = dmt.membership_tier_id
 WHERE dmt.discounts_id = :discountsId
