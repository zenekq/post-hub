CREATE TABLE comments(
    id BIGSERIAL NOT NULL,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT false,
    created_by VARCHAR(50),
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES posts (id) ON DELETE CASCADE
);

CREATE INDEX idx_comments_post_id ON comments (post_id);
CREATE INDEX idx_comments_user_id ON comments (user_id);

INSERT INTO comments (post_id, user_id, message) VALUES
    (1,1, 'Test comment for first post'),
    (2,2, 'Test comment for second post'),
    (3,3, 'Test comment for third post'),
    (4,4, 'Test comment for fourth post'),
    (5,5, 'Test comment for fifth post');