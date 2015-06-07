delete from hot_product_override 
where
    product_id in (select 
        product_id
    from
        product
    
    where
        party_id = :partyId);