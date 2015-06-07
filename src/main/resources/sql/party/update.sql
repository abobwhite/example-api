UPDATE party
SET 
  public_party_id = :public_party_id,
  language_id = :language_id, 
  last_modified_by = :last_modified_by,
  last_modified_date = :last_modified_date
WHERE party_id = :party_id