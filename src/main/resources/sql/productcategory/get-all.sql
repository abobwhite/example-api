SELECT pc.product_category_id,
       pc.product_category,
       pc.product_category_link,
       pct.translated_product_category,
       agg.total AS product_count,
       case when pct.display_order is null then 9999 else pct.display_order end as display_order,
       case when pct.display_type is null then null else pct.display_type end as display_type,
       case when pct.is_visible_on_home_screen is null then true else pct.is_visible_on_home_screen end as is_visible_on_home_screen
FROM product_category pc
  LEFT OUTER JOIN (SELECT pcpa.product_category_id, count(*) total
                   FROM product_category_product_audit pcpa
                     INNER JOIN published_product_view apv ON pcpa.product_audit_id = apv.product_audit_id
                   GROUP BY pcpa.product_category_id) agg ON pc.product_category_id = agg.product_category_id
  LEFT OUTER JOIN (SELECT product_category_id, translated_product_category, case when pctlodo.display_order is null then 9999 else pctlodo.display_order end as display_order, case when pctlodo.display_type is null then null else pctlodo.display_type end as display_type, case when pctlodo.is_visible_on_home_screen is null then true else pctlodo.is_visible_on_home_screen end as is_visible_on_home_screen 
                   FROM product_category_translation pct
                     INNER JOIN language l on pct.language_id = l.language_id
                LEFT OUTER JOIN product_category_translation_level_one_display_order pctlodo on pct.product_category_translation_id = pctlodo.product_category_translation_id 
                   WHERE l.abbreviation = :language) AS pct ON pc.product_category_id = pct.product_category_id
  
