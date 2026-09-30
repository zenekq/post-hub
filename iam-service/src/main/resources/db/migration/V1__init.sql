CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    likes INTEGER NOT NULL DEFAULT 0,
    UNIQUE (title)
);

INSERT INTO posts (title, content, created, likes) VALUES
    ('First Posts', 'This is a content for first post', CURRENT_TIMESTAMP, 10),
    ('Second Posts', 'This is a content for second post', CURRENT_TIMESTAMP, 3),
    ('Third Posts', 'This is a content for third post', CURRENT_TIMESTAMP, 10),
    ('Fourth Posts', 'This is a content for fourth post', CURRENT_TIMESTAMP, 7),
    ('Fifth Posts', 'This is a content for fifth post', CURRENT_TIMESTAMP, 5),
    ('Sixth Posts', 'This is a content for sixth post', CURRENT_TIMESTAMP, 10),
    ('Seventh Posts', 'This is a content for seventh post', CURRENT_TIMESTAMP, 8),
    ('Eighth Posts', 'This is a content for eighth post', CURRENT_TIMESTAMP, 4),
    ('Ninth Posts', 'This is a content for ninth post', CURRENT_TIMESTAMP, 6),
    ('Tenth Posts', 'This is a content for tenth post', CURRENT_TIMESTAMP, 11);
