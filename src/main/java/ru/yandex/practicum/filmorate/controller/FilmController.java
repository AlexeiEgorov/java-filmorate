package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextFilmId = 1;


    @PostMapping
    @ResponseBody
    public Film addFilm(@Valid @RequestBody Film film) {
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
