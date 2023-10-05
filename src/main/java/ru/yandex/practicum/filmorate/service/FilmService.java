package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.Constants.FILM;


@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(int id) {
        return filmStorage.findFilm(id).orElseThrow(() -> new EntityNotFoundException(FILM, id));
    }

    public void likeFilm(int userId, int filmId) {
        filmStorage.likeFilm(userId, filmId);
    }

    public void unlikeFilm(int userId, int filmId) {
        filmStorage.unlikeFilm(userId, filmId);
    }


    public List<Film> getMostLikedFilms(int count) {
        return filmStorage.getMostLikedFilms(count);
    }
}
