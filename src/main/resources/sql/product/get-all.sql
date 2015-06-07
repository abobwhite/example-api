SELECT
  lpv.product_id,
  lpv.product_audit_id,
  lpv.party_id,
  lpv.public_party_id,
  lpv.version_number,
  lpv.event_type, 
  lpv.event_datetime,
  lpv.payment_term,
  lpv.minimum_order,
  lpv.freight_on_board_port,
  lpv.freight_on_board_price,
  lpv.country_of_origin,
  lpv.lead_time,
  lpv.model_number,
  lpv.key_specification,
  lpv.meta_tag_list,
  lpv.product_name,
  cpat.translated_product_name AS chinese_product_name,
  lpv.product_description,
  lpv.keyword_list,
  lpv.is_hot_product,
  lpv.is_published,
  lpv.certification_id,
  lpv.publication_datetime,
  lpv.hot_product_override
FROM latest_product_view lpv
  LEFT OUTER JOIN (SELECT product_audit_id, 
                          translated_product_name 
                   FROM product_audit_translation
                   WHERE language_id = 2) AS cpat ON lpv.product_audit_id = cpat.product_audit_id
               