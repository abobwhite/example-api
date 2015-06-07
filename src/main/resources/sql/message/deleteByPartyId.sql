delete from message 
where
    (from_party_id = :partyId or to_party_id = :partyId);