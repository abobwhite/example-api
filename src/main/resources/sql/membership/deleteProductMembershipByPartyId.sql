delete from product_membership 
where
    membership_id in (select 
        membership_id
    from
        membership
    
    where
        party_id = :partyId);