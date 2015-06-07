SELECT rcs.percent_amount
  FROM renewal_credit_schedule as rcs
  INNER JOIN membership_tier as mt ON mt.membership_tier_id = rcs.membership_tier_id
 WHERE mt.tier = :tier AND rcs.months_remaining = :monthsRemaining