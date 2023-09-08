package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ActionAlreadyPerformedException;
import ru.yandex.practicum.filmorate.exception.ImpossibleToUndoUnperformedActionException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

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
        return filmStorage.validateAndGetFilmIfRegistered(id);
    }

    public void likeFilm(int userId, int filmId) {
        filmStorage.validateFilmRegistration(filmId);
        userStorage.validateUserRegistration(userId);
        if (filmStorage.getFilm(filmId).getUsersWhoLiked().contains(userId)) {
            throw new ActionAlreadyPerformedException("Лайк уже был поставлен");
        }
        filmStorage.getFilm(filmId).getUsersWhoLiked().add(userId);
    }

    public void unlikeFilm(int userId, int filmId) {
        filmStorage.validateFilmRegistration(filmId);
        userStorage.validateUserRegistration(userId);
        if (!filmStorage.getFilm(filmId).getUsersWhoLiked().contains(userId)) {
            throw new ImpossibleToUndoUnperformedActionException("Лайк ещё не был поставлен");
        }
        filmStorage.getFilm(filmId).getUsersWhoLiked().remove(userId);
    }

    public List<Film> getMostLikedFilms(int count) {
        return filmStorage.getFilms().stream().sorted((f1, f2) -> (f1.getUsersWhoLiked().size() -
                f2.getUsersWhoLiked().size()) * -1).limit(count).collect(Collectors.toUnmodifiableList());
    }
}
