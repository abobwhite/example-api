UPDATE user_login SET 
  failures_since_last_success = failures_since_last_success + 1 
WHERE username = :username
