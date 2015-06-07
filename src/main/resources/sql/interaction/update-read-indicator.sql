UPDATE interaction SET
  read_by_receiver = :read,
  last_modified_by = :last_modified_by,
  last_modified_date = :last_modified_date
WHERE interaction_id = :id
