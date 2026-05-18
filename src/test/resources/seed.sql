INSERT INTO books(title, year_first_published) VALUES
    ('Crime & Punishment', 1866),
    ('Cosmos', 1980),
    ('Material World', 2023);

INSERT INTO authors(full_name, birth_date) VALUES
    ('Carl Sagan', '1934-11-09'),
    ('Fyodor Dostoevsky', '1821-11-11'),
    ('Ed Conway', NULL);

INSERT INTO book_authors(book_id, author_id) VALUES
    (1, 2),
    (2, 1),
    (3, 3);

-- Plaintext password: 'test' --
INSERT INTO users(username, pw_hash) VALUES
    ('admin', '$2a$10$bMoNCNbKicoXJhAebJcMIeXrHPavy/6qX0xc1MMV2BPgQx6GpobZi'),
    ('user', '$2a$10$bMoNCNbKicoXJhAebJcMIeXrHPavy/6qX0xc1MMV2BPgQx6GpobZi');

INSERT INTO user_roles(user_id, role_id) VALUES
    (1, 1),
    (2, 2);