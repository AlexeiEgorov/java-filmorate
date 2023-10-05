package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ActionAlreadyPerformedException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ImpossibleToUndoUnperformedActionException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.FILM;

@Repository("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final InMemoryUserStorage userStorage;
    private final Map<Integer, Film> films;
    private int nextFilmId;

    @Autowired
    public InMemoryFilmStorage(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
        this.films = new HashMap<>();
        this.nextFilmId = 1;
    }

    @Override
    public void addFilm(Film film) {
        film.setId(nextFilmId++);
        films.put(film.getId(), film);
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilmRegistration(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> findFilm(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void likeFilm(int userId, int filmId) {
        final Film film = findFilm(filmId).orElseThrow(() -> new EntityNotFoundException(FILM, filmId));
        userStorage.validateUserRegistration(userId);
        if (film.getUsersWhoLiked().contains(userId)) {
            throw new ActionAlreadyPerformedException("Лайк уже был поставлен");
        }
        film.getUsersWhoLiked().add(userId);
    }

    @Override
    public void unlikeFilm(int userId, int filmId) {
        final Film film = findFilm(filmId).orElseThrow(() -> new EntityNotFoundException(FILM, filmId));
        userStorage.validateUserRegistration(userId);
        if (!film.getUsersWhoLiked().contains(userId)) {
            throw new ImpossibleToUndoUnperformedActionException("Лайк ещё не был поставлен");
        }
        film.getUsersWhoLiked().remove(userId);
    }


    //public static Film validateAndGetFilmIfRegistered(Map<Integer, Film> films, int id) {
    //    final Film film = films.get(id);
    //    if (film == null) {
    //        throw new EntityNotFoundException(FILM, id);
    //    }
    //    return film;
    //}

    @Override
    public List<Film> getMostLikedFilms(int count) {
        return getFilms().stream().sorted((f1, f2) -> f2.getLikes() - f1.getLikes())
                .limit(count).collect(Collectors.toUnmodifiableList());
    }


    public void validateFilmRegistration(int id) {
        if (!films.containsKey(id)) {
            throw new EntityNotFoundException(FILM, id);
        }
    }
}
