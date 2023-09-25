package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ActionAlreadyPerformedException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ImpossibleToUndoUnperformedActionException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.FILM;


@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

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
        return filmStorage.getFilm(id).orElseThrow(() -> new EntityNotFoundException(FILM, id));
    }

    public void likeFilm(int userId, int filmId) {
        final Film film = getFilm(filmId);
        userStorage.validateUserRegistration(userId);
        if (film.getUsersWhoLiked().contains(userId)) {
            throw new ActionAlreadyPerformedException("Лайк уже был поставлен");
        }
        film.getUsersWhoLiked().add(userId);
    }

    public void unlikeFilm(int userId, int filmId) {
        final Film film = getFilm(filmId);
        userStorage.validateUserRegistration(userId);
        if (!film.getUsersWhoLiked().contains(userId)) {
            throw new ImpossibleToUndoUnperformedActionException("Лайк ещё не был поставлен");
        }
        film.getUsersWhoLiked().remove(userId);
    }


    public List<Film> getMostLikedFilms(int count) {
        return filmStorage.getFilms().stream().sorted((f1, f2) -> f2.getLikes() - f1.getLikes())
                .limit(count).collect(Collectors.toUnmodifiableList());
    }
}
