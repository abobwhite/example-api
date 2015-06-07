select 
    product_audit_id
from
    product_audit_status
where
    product_id in (select 
            product_id
        from
            product
        where
            party_id = :partyId);