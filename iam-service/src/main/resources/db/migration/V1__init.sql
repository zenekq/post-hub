CREATE TABLE users (
    id                  BIGSERIAL PRIMARY KEY,
    username            VARCHAR(30)  NOT NULL UNIQUE,
    password            VARCHAR(128) NOT NULL,
    email               VARCHAR(50) UNIQUE,
    created             TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated             TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    registration_status VARCHAR(30)  NOT NULL,
    last_login          TIMESTAMP,
    deleted             BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE posts (
    id      BIGSERIAL PRIMARY KEY,
    title   VARCHAR(255) NOT NULL,
    content TEXT         NOT NULL,
    created TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN      NOT NULL DEFAULT false,
    likes   INTEGER      NOT NULL DEFAULT 0,
    UNIQUE (title)
);

INSERT INTO users (username, password, email, created, updated, registration_status, last_login, deleted) VALUES
    ('first_user', 'pas1', 'first_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('second_user', 'pas2', 'second_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('third_user', 'pas3', 'third_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('fourth_user', 'pas4', 'fourth_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('fifth_user', 'pas5', 'fifth_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false);

INSERT INTO posts (title, content, created, updated, deleted, likes) VALUES
    ('First Posts', 'This is a content for first post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 10),
    ('Second Posts', 'This is a content for second post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 3),
    ('Third Posts', 'This is a content for third post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 10),
    ('Fourth Posts', 'This is a content for fourth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 7),
    ('Fifth Posts', 'This is a content for fifth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 5),
    ('Sixth Posts', 'This is a content for sixth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 10),
    ('Seventh Posts', 'This is a content for seventh post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 8),
    ('Eighth Posts', 'This is a content for eighth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 4),
    ('Ninth Posts', 'This is a content for ninth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 6),
    ('Eleventh Posts', 'This is a content for eleventh post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 11),
    ('Twelfth Posts', 'This is a content for twelfth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 15),
    ('Thirteenth Posts', 'This is a content for thirteenth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 2),
    ('Fourteenth Posts', 'This is a content for fourteenth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 3),
    ('Fifteenth Posts', 'This is a content for fifteenth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 1);
