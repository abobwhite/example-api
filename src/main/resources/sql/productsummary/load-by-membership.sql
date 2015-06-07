SELECT COUNT(CASE WHEN is_published = true THEN product_id ELSE null END) AS published,
       COUNT(CASE WHEN is_published = false THEN product_id ELSE null END) AS unpublished,
       COUNT(CASE WHEN is_hot_product = true THEN product_id ELSE null END) AS hot_product,
       COUNT(CASE WHEN lpv.event_type = 'Draft' THEN product_id ELSE null END) AS draft,
       COUNT(CASE WHEN lpv.event_type = 'Pending Approval' THEN product_id ELSE null END) AS pending_approval,
       COUNT(CASE WHEN lpv.event_type = 'Approved' THEN product_id ELSE null END) AS approved,
       COUNT(CASE WHEN lpv.event_type = 'Disapproved' THEN product_id ELSE null END) AS disapproved,
       COUNT(CASE WHEN lpv.event_type = 'Waiting for Information' THEN product_id ELSE null END) AS waiting_for_info,
       COUNT(CASE WHEN lpv.event_type = 'Pending Translation' THEN product_id ELSE null END) AS pending_translation,
       COUNT(product_id) AS total,
       mv.membership_id AS membership_id
FROM latest_product_view lpv
	INNER JOIN latest_supplier_view lsv ON lsv.party_id = lpv.party_id
	LEFT OUTER JOIN membership_view mv ON mv.party_id = lpv.party_id
	WHERE mv.membership_id = :membershipId
GROUP BY mv.membership_id


