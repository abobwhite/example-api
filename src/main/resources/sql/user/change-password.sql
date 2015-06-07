UPDATE user_login 
   SET password = :newPassword,
       enabled = true
WHERE username = :username
--   AND password = :oldPassword
