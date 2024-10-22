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
                        roles VARCHAR(255) NOT NULL,
                        date DATE NOT NULL
);

-- changeset author:4
CREATE TABLE recipe (
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        image TEXT NOT NULL,
                        description TEXT NOT NULL,
                        recette TEXT NOT NULL,
                        preparation_time FLOAT NOT NULL,
                        nb_persons INT NOT NULL,
                        difficulty FLOAT NOT NULL,
                        tags TEXT NOT NULL,
                        ratings FLOAT NOT NULL,
                        author_id BIGINT NOT NULL,
                        date DATE NOT NULL,
                        CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES "user" (id)
);

-- changeset author:5
CREATE TABLE recipeRatings (
                               userId int not null,
                               recipeId int not null,
                               rating float,
                               CONSTRAINT fk_userId FOREIGN KEY (userId) REFERENCES "user" (id),
                               CONSTRAINT fk_recipeId FOREIGN KEY (recipeId) REFERENCES recipe (id)
);