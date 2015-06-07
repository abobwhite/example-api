    SELECT mad.membership_audit_discounts_id,
           mad.membership_audit_id,
           mad.amount,
           d.discounts_id,
           d.discount_code,
           d.discount_amount_type,
           d.amount as discount_amount,
           d.description,
           d.ongoing,
           d.special,
           d.effective_date,
           d.expiration_date
      FROM membership_audit_discounts mad
INNER JOIN discounts as d on d.discounts_id = mad.discounts_id 
 WHERE mad.membership_audit_id = :snapshotId