CREATE TABLE IF NOT EXISTS genres (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(150) NOT NULL,
    description varchar(200) NOT NULL,
    release_date date,
    duration integer,
    mpa_id INTEGER REFERENCES mpa (id) ON DELETE SET NULL
    CONSTRAINT film_constr CHECK(name <> '' AND release_date >= '1895-12-28' AND duration > 0)
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id INTEGER REFERENCES films (id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genres (id) ON DELETE CASCADE,
    CONSTRAINT fg_comp_key PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(120) NOT NULL,
    login varchar(64) NOT NULL,
    name varchar(64) NOT NULL,
    birthday date,
    CONSTRAINT user_birthday_constr CHECK(birthday <= CURRENT_DATE()),
    CONSTRAINT user_email_constr CHECK(email LIKE '%_@__%.__%'),
    CONSTRAINT user_login_constr CHECK(login <> '' AND login NOT LIKE '% %')
);

CREATE UNIQUE INDEX IF NOT EXISTS user_email_uindex ON users (email);

CREATE UNIQUE INDEX IF NOT EXISTS user_login_uindex ON users (login);

CREATE TABLE IF NOT EXISTS film_like (
    film_id INTEGER REFERENCES films (id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fl_comp_key PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS user_friend (
    user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    friend_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uf_comp_key PRIMARY KEY (user_id, friend_id)
);