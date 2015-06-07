SELECT
  lpv.product_id,
  lpv.product_audit_id,
  lpv.country_of_origin,
  lpv.model_number,
  lpv.payment_term,
  pat.translated_payment_term,
  lpv.minimum_order,
  lpv.freight_on_board_port,
  lpv.freight_on_board_price,
  lpv.lead_time,
  lpv.product_name,
  pat.translated_product_name,
  lpv.product_description,
  pat.translated_product_description,
  lpv.key_specification,
  pat.translated_key_specification,
  lpv.keyword_list,
  pat.translated_keyword_list,
  lpv.meta_tag_list,
  pat.translated_meta_tag_list,
  lpv.event_type
FROM latest_product_view AS lpv
  LEFT OUTER JOIN (SELECT translated_payment_term,
                          translated_product_name,
                          translated_product_description,
                          translated_key_specification,
                          translated_keyword_list,
                          translated_meta_tag_list,
                          product_audit_id
                   FROM product_audit_translation
                   WHERE language_id = 2) AS pat ON lpv.product_audit_id = pat.product_audit_id
WHERE lpv.product_id = :productId
  AND lpv.event_type = 'Pending Translation'
