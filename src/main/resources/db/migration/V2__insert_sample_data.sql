INSERT INTO users (email, password, name, role)
VALUES ('admin@gmail.com', 'admin', 'Administrator', 'ROLE_ADMIN'),
       ('selvam@gmail.com', 'secret', 'Selvam', 'ROLE_USER');

INSERT INTO short_urls (short_key, original_url, created_by, created_at, expires_at, is_private, click_count)
VALUES ('rs1Aed', 'https://www.imdb.com/title/tt0903747/', 1, TIMESTAMP '2024-07-15', NULL, FALSE,
        0),
       ('hujfDf', 'https://www.imdb.com/title/tt0944947/', 1,
        TIMESTAMP '2024-07-16', NULL, FALSE, 0),
       ('ertcbn', 'https://www.imdb.com/title/tt4574334/', 1, TIMESTAMP '2024-07-17', NULL, FALSE,
        0),
       ('edfrtg', 'https://www.imdb.com/title/tt0386676/', 1, TIMESTAMP '2024-07-18',
        NULL, TRUE, 0),
       ('vbgtyh', 'https://www.imdb.com/title/tt0108778/', 1, TIMESTAMP '2024-07-19', NULL, FALSE,
        0),
       ('rtyfgb', 'https://www.imdb.com/title/tt1190634/', 1, TIMESTAMP '2024-07-25',
        NULL, FALSE, 0),
       ('rtvbop', 'https://www.imdb.com/title/tt2356777/', 1,
        TIMESTAMP '2024-07-26', NULL, FALSE, 0),
       ('2wedfg', 'https://www.imdb.com/title/tt7366338/', 1, TIMESTAMP '2024-07-27', NULL,
        TRUE, 0),
       ('6yfrd4', 'https://www.imdb.com/title/tt2085059/', 1,
        TIMESTAMP '2024-07-28', NULL, FALSE, 0),
       ('r5t4tt', 'https://www.imdb.com/title/tt2085059/', 1, TIMESTAMP '2024-07-29', NULL, FALSE,
        0),

       ('ffr4rt', 'https://www.imdb.com/title/tt3032476/', 1,
        TIMESTAMP '2024-08-10', NULL, FALSE, 0),
       ('9oui7u', 'https://www.imdb.com/title/tt0111161/', 1,
        TIMESTAMP '2024-08-11', NULL, FALSE, 0),
       ('cvbg5t', 'https://www.imdb.com/title/tt0068646/', 1, TIMESTAMP '2024-08-12', NULL, FALSE,
        0),
       ('nm6ytf', 'https://www.imdb.com/title/tt0468569/', 1,
        TIMESTAMP '2024-08-13', NULL, FALSE, 0),

       ('tt5y6r', 'https://www.imdb.com/title/tt0110912/', 1,
        TIMESTAMP '2024-08-14', NULL, FALSE, 0),
       ('fgghty', 'https://www.imdb.com/title/tt0109830/', 1,
        TIMESTAMP '2024-08-15', NULL, FALSE, 0),
       ('f45tre', 'https://www.imdb.com/title/tt1375666/', 1, TIMESTAMP '2024-08-16', NULL,
        FALSE, 0),
       ('54rt54', 'https://www.imdb.com/title/tt1375666/', 1, TIMESTAMP '2024-08-17', NULL,
        FALSE, 0)
;