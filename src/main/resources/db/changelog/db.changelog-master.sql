-- liquibase formatted sql

-- changeset author:1
CREATE TABLE book (
                      id BIGSERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(255) NOT NULL
);

-- changeset author:2
INSERT INTO book (title, author) VALUES ('Sample Book', 'John Doe');

-- changeset author:3
CREATE TABLE "user" (
                        id BIGSERIAL PRIMARY KEY,
                        username VARCHAR(255) NOT NULL UNIQUE,
                        password_hash VARCHAR(60) NOT NULL,
                        roles VARCHAR(255) NOT NULL
);

-- changeset author:4
INSERT INTO "user" (username, password_hash, roles)
VALUES ('user1', '$2a$10$E6iC/.PGrtqG.EcUnx2C9eTShIh4o1gYpS6pHiM69.jqOeKC7x.pu', 'ROLE_USER');