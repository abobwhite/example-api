UPDATE membership_level 
SET
    expiration_date =  DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY)
WHERE
    membership_tier_id = :level
        AND expiration_date > CURRENT_DATE