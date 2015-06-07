delete
from
    product_audit_status
where
    product_id in (select 
            product_id
        from
            product
        where
            party_id = :partyId);