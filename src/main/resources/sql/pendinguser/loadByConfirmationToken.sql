SELECT pending_user_id,
       username,
       password,
       party_id,
       confirmation_token
  FROM pending_user 
 WHERE confirmation_token  = :confirmationToken