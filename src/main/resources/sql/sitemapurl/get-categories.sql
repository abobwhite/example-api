SELECT pc.product_category_id, pc.product_category,
CASE WHEN psa.parent_product_category_id is not null THEN 'CHILD' ELSE 'PARENT' END as type,
pc.last_modified_date
FROM product_category pc
left join product_category_association psa on
pc.product_category_id = psa.child_product_category_id
order by pc.product_category_id