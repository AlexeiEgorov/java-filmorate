package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

import static ru.yandex.practicum.filmorate.Constants.FILM;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films;
    private int nextFilmId = 1;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
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
    public Film getFilm(int id) {
        return films.get(id);
    }

    @Override
    public Film validateAndGetFilmIfRegistered(int id) {
        final Film film = films.get(id);
        if (film == null) {
            throw new EntityNotFoundException(FILM, id);
        }
        return film;
    }

    @Override
    public void validateFilmRegistration(int id) {
        if (!films.containsKey(id)) {
            throw new EntityNotFoundException(FILM, id);
        }
    }
}
