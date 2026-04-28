CREATE TABLE book_authors (
    book_id BIGSERIAL NOT NULL,
    author_id BIGSERIAL NOT NULL,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (author_id) REFERENCES authors(author_id)
)