delete from message_message_tag
where
	message_id in (select message_id from message
where
    (from_party_id = :partyId  or to_party_id = :partyId));