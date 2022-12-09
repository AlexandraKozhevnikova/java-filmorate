INSERT INTO director (NAME)
VALUES ('Стивен Спилберг');
VALUES ('Майкл Бэй');

INSERT INTO user_filmorate (email, login, name, birthday)
VALUES ('hp@ya.ru', 'hp', 'harry potter', '2000-12-1'),
       ('hp2@ya.ru', 'hp', 'harry potter', '2000-12-1'),
       ('hp3@ya.ru', 'hp', 'harry potter', '2000-12-1'),
       ('hp4@ya.ru', 'hp', 'harry potter', '2000-12-1'),
       ('hp5@ya.ru', 'hp', 'harry potter', '2000-12-1'),
       ('hp6@ya.ru', 'hp', 'harry potter', '2000-12-1');

INSERT INTO film (name, release_date, duration, rating_mpa)
VALUES ('один дома 1', '2000-10-10', 100, 1) ,
       ('один дома 2', '2000-10-10', 100, 1),
       ('один дома 3', '2001-10-10', 100, 1),
       ('один дома 4', '2001-10-10', 100, 1),
       ('один дома 5', '2002-10-10', 100, 1),
       ('один дома 6', '2002-10-10', 100, 1),
       ('один дома 7', '2022-10-10', 100, 1),
       ('один дома 8', '2022-10-10', 100, 1),
       ('один дома 9', '2022-10-10', 100, 1),
       ('один дома 10', '2022-10-10', 100, 1),
       ('один дома 11', '2022-10-10', 100, 1),
       ('один дома 12', '2026-10-10', 100, 1);

INSERT INTO film_genre (film_id, genre_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 2),
       (6, 2);

INSERT INTO FILM_LIKE(film_id, user_id)
VALUES (2, 1),
       (2, 2),
       (2, 3),
       (1, 1),
       (1, 2),
       (3, 1),
       (10, 1),
       (11, 1),
       (11, 2),
       (11, 3),
       (11, 4),
       (12, 6);
