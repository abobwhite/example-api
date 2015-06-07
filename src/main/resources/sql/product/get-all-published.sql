SELECT
  ppv.product_id,
  ppv.product_audit_id,
  ppv.party_id,
  ppv.public_party_id,
  ppv.version_number,
  ppv.event_type, 
  ppv.event_datetime,
  ppv.payment_term,
  ppv.minimum_order,
  ppv.freight_on_board_port,
  ppv.freight_on_board_price,
  ppv.country_of_origin,
  ppv.lead_time,
  ppv.model_number,
  ppv.key_specification,
  ppv.meta_tag_list,
  ppv.product_name,
  cpat.translated_product_name AS chinese_product_name,
  ppv.product_description,
  ppv.keyword_list,
  ppv.is_hot_product,
  ppv.is_published,
  ppv.certification_id,
  ppv.party_audit_id,
  ppv.level,
  ppv.publication_datetime,
  ppv.hot_product_listing,
  ppv.hot_product_override
FROM published_product_view ppv
  LEFT OUTER JOIN (SELECT product_audit_id, 
                          translated_product_name 
                   FROM product_audit_translation
                   WHERE language_id = 2) AS cpat ON ppv.product_audit_id = cpat.product_audit_id
