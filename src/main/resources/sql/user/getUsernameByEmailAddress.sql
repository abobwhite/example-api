SELECT distinct u.username
FROM  (SELECT party_id, username, email_address FROM user_view UNION SELECT party_id, username, email_address FROM pending_user_view) as u
WHERE u.email_address = :emailAddress
