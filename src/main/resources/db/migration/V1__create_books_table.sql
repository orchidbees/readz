CREATE TABLE books (
    book_id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    year_published INTEGER,
    isbn_10 VARCHAR(20),
    isbn_13 VARCHAR(20)
);