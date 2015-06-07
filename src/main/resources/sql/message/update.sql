UPDATE message
SET 
  public_message_id = :public_message_id,
  last_modified_by = :last_modified_by,
  last_modified_date = :last_modified_date
WHERE message_id = :message_id