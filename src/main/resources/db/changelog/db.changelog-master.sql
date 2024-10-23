-- liquibase formatted sql

-- changeset author:1
CREATE TABLE "user" (
                        id BIGSERIAL PRIMARY KEY,
                        username VARCHAR(255) NOT NULL UNIQUE,
                        password_hash VARCHAR(60) NOT NULL,
                        roles VARCHAR(255) NOT NULL,
                        date DATE NOT NULL
);

-- changeset author:2
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
                        author_id BIGINT NOT NULL,
                        date DATE NOT NULL,
                        CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES "user" (id)
);

-- changeset author:3
CREATE TABLE recipeRatings (
                               id BIGSERIAL PRIMARY KEY,
                               userId BIGINT NOT NULL,
                               recipeId BIGINT NOT NULL,
                               rating FLOAT,
                               CONSTRAINT fk_userId FOREIGN KEY (userId) REFERENCES "user" (id),
                               CONSTRAINT fk_recipeId FOREIGN KEY (recipeId) REFERENCES recipe (id)
);

-- Insert users
-- changeset author:4
INSERT INTO "user" (username, password_hash, roles, date) VALUES
                                                              ('chef1', 'hashedpassword1', 'USER', CURRENT_DATE),
                                                              ('chef2', 'hashedpassword2', 'USER', CURRENT_DATE),
                                                              ('chef3', 'hashedpassword3', 'USER', CURRENT_DATE),
                                                              ('chef4', 'hashedpassword4', 'USER', CURRENT_DATE),
                                                              ('chef5', 'hashedpassword5', 'USER', CURRENT_DATE);

-- Insert recipes
-- changeset author:5
INSERT INTO recipe (title, image, description, recette, preparation_time, nb_persons, difficulty, tags, author_id, date) VALUES
                                                                                                                             ('Soupe à l’oignon', 'image_url_1', 'Une délicieuse soupe traditionnelle française.',
                                                                                                                              '{
                                                                                                                                  "ingredients": ["Oignons", "Bouillon de bœuf", "Pain grillé", "Fromage râpé"],
                                                                                                                                  "instructions": ["Caraméliser les oignons.", "Ajouter le bouillon et mijoter.", "Servir avec du pain grillé et du fromage."]
                                                                                                                              }', 60, 4, 2.5, 'entrée,végétarien,traditionnel', 1, CURRENT_DATE),

                                                                                                                             ('Ratatouille', 'image_url_2', 'Un plat provençal à base de légumes mijotés.',
                                                                                                                              '{
                                                                                                                                  "ingredients": ["Aubergines", "Courgettes", "Poivrons", "Tomates", "Oignons", "Herbes de Provence"],
                                                                                                                                  "instructions": ["Couper les légumes.", "Faire revenir les oignons.", "Ajouter les légumes et les herbes.", "Mijoter à feu doux."]
                                                                                                                              }', 90, 4, 3.0, 'plat principal,végétalien,sans gluten,vegan', 2, CURRENT_DATE),

                                                                                                                             ('Quiche Lorraine', 'image_url_3', 'Une quiche savoureuse au lard fumé et au fromage.',
                                                                                                                              '{
                                                                                                                                  "ingredients": ["Pâte brisée", "Lardons", "Œufs", "Crème fraîche", "Gruyère râpé"],
                                                                                                                                  "instructions": ["Préchauffer le four.", "Préparer la garniture.", "Verser sur la pâte.", "Cuire au four."]
                                                                                                                              }', 60, 6, 2.0, 'plat principal,traditionnel', 3, CURRENT_DATE),

                                                                                                                             ('Salade de quinoa', 'image_url_4', 'Une salade fraîche et nutritive.',
                                                                                                                              '{
                                                                                                                                  "ingredients": ["Quinoa", "Tomates cerises", "Concombre", "Féta", "Olives", "Vinaigrette"],
                                                                                                                                  "instructions": ["Cuire le quinoa.", "Mélanger les légumes.", "Assaisonner avec la vinaigrette."]
                                                                                                                              }', 30, 2, 1.5, 'entrée,végétarien,sans gluten,healthy', 4, CURRENT_DATE),

                                                                                                                             ('Crêpes sucrées', 'image_url_5', 'De délicieuses crêpes fines et légères.',
                                                                                                                              '{
                                                                                                                                  "ingredients": ["Farine", "Œufs", "Lait", "Sucre", "Beurre"],
                                                                                                                                  "instructions": ["Préparer la pâte à crêpes.", "Laisser reposer.", "Cuire les crêpes dans une poêle chaude."]
                                                                                                                              }', 20, 4, 1.0, 'dessert,végétarien', 5, CURRENT_DATE);

-- Insert recipe ratings
-- changeset author:6
INSERT INTO recipeRatings (userId, recipeId, rating) VALUES
                                                         (1, 1, 4.5),
                                                         (2, 2, 5.0),
                                                         (3, 3, 4.0),
                                                         (4, 4, 3.5),
                                                         (5, 5, 5.0);