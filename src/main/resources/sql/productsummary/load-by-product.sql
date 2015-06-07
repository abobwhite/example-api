SELECT COUNT(CASE WHEN lpv.is_published = true THEN lpv.product_id ELSE null END) AS published,
       COUNT(CASE WHEN lpv.is_published = false THEN lpv.product_id ELSE null END) AS unpublished,
       COUNT(CASE WHEN lpv.is_hot_product = true THEN lpv.product_id ELSE null END) AS hot_product,
       COUNT(CASE WHEN lpv.event_type = 'Draft' THEN lpv.product_id ELSE null END) AS draft,
       COUNT(CASE WHEN lpv.event_type = 'Pending Approval' THEN lpv.product_id ELSE null END) AS pending_approval,
       COUNT(CASE WHEN lpv.event_type = 'Approved' THEN lpv.product_id ELSE null END) AS approved,
       COUNT(CASE WHEN lpv.event_type = 'Disapproved' THEN lpv.product_id ELSE null END) AS disapproved,
       COUNT(CASE WHEN lpv.event_type = 'Waiting for Information' THEN lpv.product_id ELSE null END) AS waiting_for_info,
       COUNT(CASE WHEN lpv.event_type = 'Pending Translation' THEN lpv.product_id ELSE null END) AS pending_translation,
       COUNT(lpv.product_id) AS total,
       mv.membership_id AS membership_id
FROM latest_product_view lpv
	INNER JOIN latest_supplier_view lsv ON lsv.party_id = lpv.party_id
	LEFT OUTER JOIN membership_view mv ON mv.party_id = lpv.party_id
WHERE mv.membership_id IN (SELECT membership_id FROM product_membership WHERE product_id = :productId)
GROUP BY mv.membership_id
