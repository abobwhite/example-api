UPDATE user_login SET 
  failures_since_last_success = 0
WHERE username = :username
