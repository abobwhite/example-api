SELECT
  d.discounts_id,
  d.discount_code, 
  d.discount_amount_type, 
  d.amount, 
  d.description,
  d.ongoing,
  d.special,
  d.effective_date,
  d.expiration_date,
  mt.tier,
  dt.subscription_type_name
FROM discounts d
 INNER JOIN discounts_membership_tier as dmt on dmt.discounts_id = d.discounts_id
 INNER JOIN membership_tier as mt on mt.membership_tier_id = dmt.membership_tier_id
 INNER JOIN discounts_subscription_type as ddt on ddt.discounts_id = d.discounts_id
 INNER JOIN subscription_type as dt on dt.subscription_type_id = ddt.subscription_type_id
               