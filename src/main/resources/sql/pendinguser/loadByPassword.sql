SELECT pu.pending_user_id,
       pu.username,
       pu.password,
       pu.party_id,
       pu.confirmation_token
  FROM pending_user pu
 WHERE pu.password = :password
