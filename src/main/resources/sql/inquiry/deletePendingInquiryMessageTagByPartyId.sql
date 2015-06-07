delete from pending_inquiry_message_tag
where
    pending_inquiry_id in (select 
        pending_inquiry_id
    from
        pending_inquiry
    where
        party_id = :partyId);
