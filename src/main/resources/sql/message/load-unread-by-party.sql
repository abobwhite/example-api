SELECT count(*) unread_count
FROM interaction
WHERE to_party_id = :id
  AND read_by_receiver = FALSE
  