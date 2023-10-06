package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreDao genreDao;

    public void addFilm(Film film) {
        filmStorage.addFilm(film);
        genreDao.genresBatchInsert(film);
    }

    public Film updateFilm(Film film) {
        Film updated = filmStorage.updateFilm(film);
        genreDao.genresBatchInsert(film);
        LinkedHashSet<Genre> genres = genreDao.getFilmsGenres(List.of(film)).get(film.getId());
        updated.setGenres(genres);
        if (genres != null) {
            updated.setGenres(genres);
        } else {
            updated.setGenres(new LinkedHashSet<>());
        }

        return updated;
    }

    public Collection<Film> getFilms() {
        Collection<Film> films = filmStorage.getFilms();
        Map<Integer, LinkedHashSet<Genre>> filmsGenres = genreDao.getFilmsGenres(new ArrayList<>(films));
        for (Film film : films) {
            LinkedHashSet<Genre> genres = filmsGenres.get(film.getId());
            if (genres != null) {
                film.setGenres(genres);
            } else {
                film.setGenres(new LinkedHashSet<>());
            }
        }

        return films;
    }

    public Film getFilm(int id) {
        Film film = filmStorage.findFilm(id);
        LinkedHashSet<Genre> genres = genreDao.getFilmsGenres(List.of(film)).get(film.getId());
        if (genres != null) {
            film.setGenres(genres);
        } else {
            film.setGenres(new LinkedHashSet<>());
        }
        return film;
    }

    public void likeFilm(int userId, int filmId) {
        userStorage.findUser(userId);
        filmStorage.likeFilm(userId, filmId);
    }

    public void unlikeFilm(int userId, int filmId) {
        userStorage.findUser(userId);
        filmStorage.unlikeFilm(userId, filmId);
    }


    public List<Film> getMostLikedFilms(int count) {
        Collection<Film> films = filmStorage.getMostLikedFilms(count);
        Map<Integer, LinkedHashSet<Genre>> filmsGenres = genreDao.getFilmsGenres(new ArrayList<>(films));
        for (Film film : films) {
            LinkedHashSet<Genre> genres = filmsGenres.get(film.getId());
            if (genres != null) {
                film.setGenres(genres);
            } else {
                film.setGenres(new LinkedHashSet<>());
            }
        }
        return new ArrayList<>(films);
    }
}
