CREATE TABLE actions_logs (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    message VARCHAR(255) NOT NULL,
    action_type VARCHAR(55) NOT NULL,
    priority_type VARCHAR(55) NOT NULL,
    service VARCHAR(55) NOT NULL
);

INSERT INTO actions_logs (id, user_id, created_at, is_read, message, action_type, priority_type, service) VALUES
    (1,1, CURRENT_TIMESTAMP, FALSE, 'user "new_user" was created', 'CREATE', 'HIGH', 'iam-service'),
    (2,2, CURRENT_TIMESTAMP, FALSE, 'user "new_use2r" update profile', 'UPDATE', 'MEDIUM', 'iam-service');
