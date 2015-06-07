select
    pc.certification_id
from
    party p
        left outer join
    party_certification pc ON pc.party_id = p.party_id
where
    p.party_id = :partyId;