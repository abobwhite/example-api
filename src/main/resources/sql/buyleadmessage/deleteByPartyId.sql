delete from message_buy_lead 
where
	message_id in (select message_id from message
where
    (from_party_id = :partyId or to_party_id = :partyId));