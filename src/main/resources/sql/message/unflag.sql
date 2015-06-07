DELETE FROM message_flag
WHERE message_id = :messageId
  AND party_id = :partyId
  