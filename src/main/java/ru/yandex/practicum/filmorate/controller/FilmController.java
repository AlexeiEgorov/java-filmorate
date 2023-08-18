package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate firstFilmRelease = LocalDate.of(1895, 12, 28);
    private int nextFilmId = 1;


    @PostMapping
    @ResponseBody
    public Film addFilm(@Valid @RequestBody Film film) {
        if (film.getName().isBlank()) {
            log.debug("Пользователь пытается добавить фильм без названия: {}", film);
            throw new ValidationException("Пустое имя");
        } else if (film.getDescription().length() > 200) {
            log.debug("Пользователь пытается добавить фильм с описанием длиной более 200 символов: {}",
                    film.getDescription());
            throw new ValidationException("Длина описания более 200 символов");
        } else if (film.getReleaseDate().isBefore(firstFilmRelease)) {
            log.debug("Пользователь пытается добавить фильм с датой выхода до " + firstFilmRelease + ": {}",
                    film.getReleaseDate());
            throw new ValidationException("Дата выпуска фильма до " + firstFilmRelease);
        } else if (film.getDuration() < 1) {
            log.debug("Пользователь пытается добавить фильм c неположительной длительностью: {}",
                    film.getDuration());
            throw new ValidationException("Неположительная длина фильма");
        }
        film.setId(nextFilmId++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    @ResponseBody
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.get(film.getId()) == null) {
            log.debug("Клиент пытается обновить фильм с незарегестрированным id: {}", film.getId());
            throw new EntityNotFoundException(film.getClass().toString());
        } else if (film.getName().isBlank()) {
            log.debug("Клиент пытается обновить фильм, передавая новый, без названия: {}", film);
            throw new ValidationException("Пустое имя");
        } else if (film.getDescription().length() > 200) {
            log.debug("Клиент пытается обновить фильм, передавая новый, c описанием длиной более 200 символов: " +
                            "{}", film.getDescription());
            throw new ValidationException("Длина описания более 200 символов");
        } else if (film.getReleaseDate().isBefore(firstFilmRelease)) {
            log.debug("Пользователь пытается добавить фильм с датой выхода до " + firstFilmRelease + ": {}",
                    film.getReleaseDate());
            throw new ValidationException("Дата выпуска фильма до " + firstFilmRelease);
        } else if (film.getDuration() < 1) {
            log.debug("Пользователь пытается добавить фильм c неположительной длительностью: {}",
                    film.getDuration());
            throw new ValidationException("Неположительная длина фильма");
        }
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    @ResponseBody
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
