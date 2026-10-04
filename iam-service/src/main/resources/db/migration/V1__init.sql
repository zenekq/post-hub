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
    user_id INTEGER NOT NULL,
    title   VARCHAR(255) NOT NULL,
    content TEXT         NOT NULL,
    created TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN      NOT NULL DEFAULT false,
    likes   INTEGER      NOT NULL DEFAULT 0,
    created_by VARCHAR(30),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (title)
);

INSERT INTO users (username, password, email, created, updated, registration_status, last_login, deleted) VALUES
    ('first_user', '$2a$10$esq3XddqYdSzyvfKmIXn1OXspdvzk98kDAUzmbE.1jjxY26D72quq', 'first_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('second_user', '$2a$10$g6H7do4Txmgraarf9HwxHe4brj72TlPFfps78w/ThixIaOvPv1ZPK', 'second_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('third_user', '$2a$10$HT/VouLOxW0EDuLLgPASsuSN9MeDTkP1V5zpCW0pN9rNkV/R5Vuma', 'third_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('fourth_user', '$2a$10$a3iM0krwVEiECG3EbzxNBOTJBNVaHPVEjLPEOOp4ysvJSje54j/Be', 'fourth_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('fifth_user', '$2a$10$eiiNTTyQTH8Aa8/UbCayZuPybkToefdtUswsYn6OumCfXyhSGyDuW', 'fifth_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false);

INSERT INTO posts (user_id ,title, content, created, updated, deleted, likes) VALUES
    (1,'First Posts', 'This is a content for first post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 10),
    (3,'Second Posts', 'This is a content for second post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 3),
    (4,'Third Posts', 'This is a content for third post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 10),
    (5,'Fourth Posts', 'This is a content for fourth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 7),
    (1,'Fifth Posts', 'This is a content for fifth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 5),
    (2,'Sixth Posts', 'This is a content for sixth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 10),
    (3,'Seventh Posts', 'This is a content for seventh post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 8),
    (4,'Eighth Posts', 'This is a content for eighth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 4),
    (5,'Ninth Posts', 'This is a content for ninth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 6),
    (1,'Eleventh Posts', 'This is a content for eleventh post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 11),
    (2,'Twelfth Posts', 'This is a content for twelfth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 15),
    (3,'Thirteenth Posts', 'This is a content for thirteenth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 2),
    (4,'Fourteenth Posts', 'This is a content for fourteenth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 3),
    (5,'Fifteenth Posts', 'This is a content for fifteenth post', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, 1);
