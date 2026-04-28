CREATE TABLE authors (
    author_id BIGSERIAL PRIMARY KEY,
    full_name TEXT NOT NULL,
    birth_date DATE
);