SELECT pu.pending_user_id,
       pu.username,
       pu.password,
       pu.party_id,
       pu.confirmation_token
  FROM party_audit_status AS pas
  INNER JOIN party_audit AS pa ON pas.party_audit_id = pa.party_audit_id
  INNER JOIN party p ON pas.party_id = p.party_id
  INNER JOIN pending_user pu ON p.party_id = pu.party_id
WHERE pa.email_address = :emailAddress



