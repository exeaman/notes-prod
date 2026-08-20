CREATE TABLE notes (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    author VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);