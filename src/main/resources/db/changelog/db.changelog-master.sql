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

-- Insert initial users
-- changeset author:4
INSERT INTO "user" (username, password_hash, roles, date) VALUES
                                                              ('chef1', 'hashedpassword1', 'USER', CURRENT_DATE),
                                                              ('chef2', 'hashedpassword2', 'USER', CURRENT_DATE),
                                                              ('chef3', 'hashedpassword3', 'USER', CURRENT_DATE);

-- Insert 10 French recipes
-- changeset author:5
INSERT INTO recipe (title, image, description, recette, preparation_time, nb_persons, difficulty, tags, author_id, date) VALUES
                                                                                                                             ('Quiche Lorraine', 'image_url_1', 'Une délicieuse quiche au lard et au fromage.', '1. Préchauffer le four. 2. Préparer la pâte. 3. Ajouter les ingrédients. 4. Cuire au four.', 60, 4, 2.5, 'plat principal,quiche,français', 1, CURRENT_DATE),
                                                                                                                             ('Soupe à l’oignon', 'image_url_2', 'Une soupe traditionnelle française avec des oignons caramélisés.', '1. Caraméliser les oignons. 2. Ajouter le bouillon. 3. Servir avec du pain grillé et du fromage.', 45, 6, 2.0, 'soupe,entrée,français', 2, CURRENT_DATE),
                                                                                                                             ('Bœuf bourguignon', 'image_url_3', 'Un ragoût de bœuf mijoté dans du vin rouge avec des légumes.', '1. Faire dorer la viande. 2. Ajouter les légumes et le vin. 3. Mijoter plusieurs heures.', 240, 6, 4.5, 'plat principal,viande,français', 1, CURRENT_DATE),
                                                                                                                             ('Ratatouille', 'image_url_4', 'Un mélange de légumes mijotés aux saveurs provençales.', '1. Couper et faire revenir les légumes. 2. Ajouter les herbes. 3. Cuire à feu doux.', 90, 4, 3.0, 'accompagnement,légumes,français', 3, CURRENT_DATE),
                                                                                                                             ('Crème brûlée', 'image_url_5', 'Un dessert onctueux à base de crème et de vanille, avec une croûte caramélisée.', '1. Préparer la crème. 2. Cuire au bain-marie. 3. Caraméliser le sucre sur le dessus.', 50, 4, 2.5, 'dessert,français', 2, CURRENT_DATE),
                                                                                                                             ('Tarte Tatin', 'image_url_6', 'Une tarte aux pommes caramélisées, servie à l’envers.', '1. Caraméliser les pommes. 2. Préparer la pâte. 3. Cuire et retourner.', 70, 6, 3.0, 'dessert,français', 3, CURRENT_DATE),
                                                                                                                             ('Coq au vin', 'image_url_7', 'Un ragoût de poulet mijoté dans du vin rouge avec des champignons.', '1. Faire revenir le poulet. 2. Ajouter les légumes et le vin. 3. Mijoter longuement.', 180, 6, 4.0, 'plat principal,viande,français', 1, CURRENT_DATE),
                                                                                                                             ('Salade niçoise', 'image_url_8', 'Une salade composée typique de la région niçoise.', '1. Préparer les légumes et le poisson. 2. Assaisonner avec de l’huile d’olive.', 30, 2, 1.5, 'entrée,salade,français', 2, CURRENT_DATE),
                                                                                                                             ('Madeleines', 'image_url_9', 'Des petits gâteaux moelleux en forme de coquillage.', '1. Préparer la pâte. 2. Cuire dans des moules à madeleine.', 40, 8, 2.0, 'dessert,français', 3, CURRENT_DATE),
                                                                                                                             ('Croissants', 'image_url_10', 'De délicieux croissants feuilletés.', '1. Préparer la pâte feuilletée. 2. Plier et cuire.', 120, 4, 4.0, 'petit déjeuner,français', 1, CURRENT_DATE);

-- Insert recipe ratings from users
-- changeset author:6
INSERT INTO recipeRatings (userId, recipeId, rating) VALUES
                                                         (1, 1, 4.5),
                                                         (2, 2, 5.0),
                                                         (3, 3, 4.0),
                                                         (1, 4, 3.5),
                                                         (2, 5, 5.0),
                                                         (3, 6, 4.5),
                                                         (1, 7, 4.0),
                                                         (2, 8, 4.5),
                                                         (3, 9, 4.0),
                                                         (1, 10, 5.0);