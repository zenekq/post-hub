INSERT INTO action_logs (id, user_id, created_at, is_read, message, action_type, priority_type, service) VALUES
     (1,1, CURRENT_TIMESTAMP, FALSE, 'user "new_user" was created', 'CREATE', 'HIGH', 'iam-service'),
     (2,2, CURRENT_TIMESTAMP, FALSE, 'user "new_use2r" update profile', 'UPDATE', 'MEDIUM', 'iam-service');
