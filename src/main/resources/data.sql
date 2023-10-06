INSERT INTO genres (name)
SELECT  NewGenres.name
FROM    ( VALUES ('Комедия'),
                 ('Драма'),
                 ('Мультфильм'),
                 ('Триллер'),
                 ('Документальный'),
                 ('Боевик') ) AS NewGenres (name)
WHERE   NOT EXISTS ( SELECT 1
                     FROM   genres AS g
                     WHERE  g.name = NewGenres.name );

INSERT INTO mpa (name)
SELECT  NewMpa.name
FROM    ( VALUES ('G'),
                 ('PG'),
                 ('PG-13'),
                 ('R'),
                 ('NC-17') ) AS NewMpa (name)
WHERE   NOT EXISTS ( SELECT 1
                     FROM   mpa AS m
                     WHERE  m.name = NewMpa.name );