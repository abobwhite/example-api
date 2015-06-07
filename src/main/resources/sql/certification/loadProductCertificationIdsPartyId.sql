select
    pc.certification_id
from
    product p
        left outer join
    product_certification pc ON pc.product_id = p.product_id
where
    p.party_id = :partyId;