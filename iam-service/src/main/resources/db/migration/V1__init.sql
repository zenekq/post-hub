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
    user_id BIGINT NOT NULL,
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

CREATE TABLE roles(
    id SERIAL PRIMARY KEY,
    name VARCHAR(16) NOT NULL,
    user_system_role VARCHAR(32) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    created_by VARCHAR(30) NOT NULL
);

CREATE TABLE user_roles(
    user_id BIGINT NOT NULL,
    roles_id INT NOT NULL,
    PRIMARY KEY (user_id, roles_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (roles_id) REFERENCES roles (id)
);

CREATE TABLE refresh_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(128) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    CONSTRAINT FK_refresh_token_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT refresh_token_UNIQUE UNIQUE (user_id, id)
);
