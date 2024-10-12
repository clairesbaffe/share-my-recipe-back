-- liquibase formatted sql

-- changeset author:1
CREATE TABLE book (
                      id BIGSERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(255) NOT NULL
);

-- changeset author:2
INSERT INTO book (title, author) VALUES ('Sample Book', 'John Doe');