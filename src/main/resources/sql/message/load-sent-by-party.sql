SELECT count(*) as sent_count
  FROM message
 WHERE from_party_id = :id;
  