DELETE FROM user_role
WHERE user_id = :user_id
 AND  role_description = :role_description