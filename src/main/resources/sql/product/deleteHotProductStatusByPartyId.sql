delete from hot_product_status 
where
    product_id in (select 
        product_id
    from
        product
    
    where
        party_id = :partyId);