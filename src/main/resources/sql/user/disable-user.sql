UPDATE user_login 
   SET password = :password,
       enabled = false
WHERE username = :username