drop ALL OBJECTS;

CREATE TABLE IF NOT EXISTS Film (
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY
        PRIMARY KEY,
    name         VARCHAR NOT NULL,
    description  VARCHAR(200),
    release_date DATE,
    CONSTRAINT release_date_age CHECK (release_date > '1895-12-28'),
    duration     INTEGER,
    CONSTRAINT duration_positive CHECK (duration > 0),
    rating_MPA    INTEGER NOT NULL
);
comment on column film.id is 'The film ID';
comment on column film.name is 'The title of film';
comment on column film.description is 'The description of film, max 200 symbols';
comment on column film.release_date is 'The release date of film, more than 1895-12-28';
comment on column film.duration is 'The duration of film, should be positive';
comment on column film.rating_MPA is 'The film rating Motion Picture Association';


CREATE TABLE IF NOT EXISTS Film_genre (
    film_id  INTEGER REFERENCES Film (id) ON DELETE CASCADE,
    genre_id INTEGER,
    CONSTRAINT film_genre_pk PRIMARY KEY (film_id, genre_id)
);


CREATE TABLE IF NOT EXISTS User_filmorate (
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    VARCHAR NOT NULL,
    CONSTRAINT email_symbol CHECK (email like '%@%'),
    login    VARCHAR NOT NULL,
    CONSTRAINT login_without_space CHECK (login not like '% %' AND login <> ''),
    name     VARCHAR,
    birthday DATE,
    CONSTRAINT birthday_past CHECK (birthday < now())
);


CREATE TABLE IF NOT EXISTS Film_like (
    film_id INTEGER REFERENCES Film (id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES User_filmorate (id) ON DELETE CASCADE,
    CONSTRAINT film_like_pk PRIMARY KEY (film_id, user_id)

);


CREATE TABLE IF NOT EXISTS Friendship (
    user_requester_id INTEGER REFERENCES User_filmorate (id),
    user_responser_id INTEGER REFERENCES User_filmorate (id),
    status            VARCHAR(10),
    CONSTRAINT users_not_equal CHECK (user_requester_id <> user_responser_id),
    CONSTRAINT friendship_pk PRIMARY KEY (user_requester_id, user_responser_id)
);
