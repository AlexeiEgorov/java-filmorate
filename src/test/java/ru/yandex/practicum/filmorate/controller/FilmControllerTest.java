package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private FilmController filmController;
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    }

    @Test
    void addFilm() {
        final Film film1 = new Film("", "Hollow", LocalDate.now(), 1);
        filmController.addFilm(film1);
        final Set<ConstraintViolation<Film>> violations1 = validator.validate(film1);
        assertEquals(1, violations1.size(), "Контроллер пропустил фильм с пустым названием");

        final Film film2 = new Film("Gray Terminal", "H3154l173!".repeat(23), LocalDate.now(), 1);
        filmController.addFilm(film2);
        final Set<ConstraintViolation<Film>> violations2 = validator.validate(film2);
        assertEquals(1, violations1.size(), "Контроллер пропустил фильм с описанием более 200 " +
                "символов");

        final Film film3 = new Film("Gray Terminal", "",
                LocalDate.of(1895, 12, 27), 1);
        final Set<ConstraintViolation<Film>> violations3 = validator.validate(film3);
        filmController.addFilm(film3);
        assertEquals(1, violations3.size(), "Контроллер пропустил фильм с датой релиза ранее 28 дек" +
                " 1895");

        final Film film4 = new Film("Gray Terminal", "", LocalDate.now(), 0);
        final Set<ConstraintViolation<Film>> violations4 = validator.validate(film4);
        filmController.addFilm(film4);
        assertEquals(1, violations4.size(),"Контроллер пропустил фильм с неположительной " +
                "продолжительностью");
    }

    @Test
    void updateFilm() {
        filmController.addFilm(new Film("Millennium Actress", "", LocalDate.now(), 1));

        final Film film2 = new Film(2, "Gray Terminal", "", LocalDate.now(), 1,
                new HashSet<>());
        assertThrows(EntityNotFoundException.class, () -> filmController.updateFilm(film2), "Контроллер " +
                "пропустил фильм с незарегестрированным id");
    }

    @Test
    void getFilms() {
        final Film film1 = new Film("9", "", LocalDate.now(), 1);
        final Film film2 = new Film("Berserk", "", LocalDate.now(), 1);
        final Film film3 = new Film("Millennium Actress", "", LocalDate.now(), 1);
        final Film newFilm2 = new Film(2, "Berserk", "",
                LocalDate.of(1997, 10, 15), 1, new HashSet<>());

        filmController.addFilm(film1);
        filmController.addFilm(film2);
        filmController.addFilm(film3);
        filmController.updateFilm(newFilm2);
        assertEquals(List.of(film1, newFilm2, film3), new ArrayList<>(filmController.getFilms()),
                "Контроллер вернул неподходящий список фильмов");
    }
}
