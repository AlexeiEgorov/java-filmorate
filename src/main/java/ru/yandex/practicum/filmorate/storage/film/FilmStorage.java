package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    void addFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getFilms();

    Optional<Film> findFilm(int id);

    void likeFilm(int userId, int filmId);

    void unlikeFilm(int userId, int filmId);

    List<Film> getMostLikedFilms(int count);
}
