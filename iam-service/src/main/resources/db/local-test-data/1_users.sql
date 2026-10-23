INSERT INTO users (username, password, email, created, updated, registration_status, last_login, deleted) VALUES
    ('super_admin', '$argon2id$v=19$m=4096,t=3,p=1$qug41fE4Du1KOSxx5q1P9g$Jb6Tv1t8ShC4fowDuGi2LVGsg6g5CPQANeet4qVCmgg', 'super_admin@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('admin', '$argon2id$v=19$m=4096,t=3,p=1$Sy54v7HacX48ICKoMsuwTA$O0tdHrUOkzwbZr1O7gi/LnLznKrUVH5AgkE721KCYGo', 'admin@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('user', '$argon2id$v=19$m=4096,t=3,p=1$z8XAiCqGuVPJJbULaG6Yyg$Pozjdvl6MGtMsGmOs3+xkzA+rH9EbEGLUpg/76ZDWqE', 'user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('fourth_user', '$argon2id$v=19$m=4096,t=3,p=1$nl4YqL2aFDJ8RTwJelR4cg$+jhYq/lW5H7a+BwnuxyNIsWpwbpo5B9hUB58ZjNZYIg', 'fourth_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false),
    ('fifth_user', '$argon2id$v=19$m=4096,t=3,p=1$pqTsS0z5PPni05kHThP/aQ$hFf4HzdXLjYI9e3lBsbBrrfVnGMtjA0CoJqGXIv3jbA', 'fifth_user@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, false);
