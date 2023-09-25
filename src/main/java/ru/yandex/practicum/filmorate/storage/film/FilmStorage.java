package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    void addFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getFilms();

    Optional<Film> getFilm(int id);

    Film validateAndGetFilmIfRegistered(int id);

    void validateFilmRegistration(int id);
}
