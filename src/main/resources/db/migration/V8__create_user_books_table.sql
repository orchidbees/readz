CREATE TABLE user_books (
    user_book_id BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL NOT NULL,
    book_id BIGSERIAL NOT NULL,
    reading_status VARCHAR(50),
    rating NUMERIC CHECK (rating > 0 AND rating < 6),
    date_added TIMESTAMPTZ,
    date_updated TIMESTAMPTZ,
    date_read TIMESTAMPTZ,
    times_read NUMERIC,
    review TEXT,
    personal_notes TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
)