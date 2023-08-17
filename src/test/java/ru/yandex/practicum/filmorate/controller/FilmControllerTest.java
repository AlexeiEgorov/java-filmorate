package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void addFilm() {
        final Film film1 = new Film("", "Hollow", LocalDate.now(), 1);
        assertThrows(NoTitleException.class, () -> filmController.addFilm(film1), "Контроллер " +
                "пропустил фильм с пустым названием");

        final Film film2 = new Film("Gray Terminal", "H3154l173!".repeat(23), LocalDate.now(), 1);
        assertThrows(DescriptionLengthOutOf200ChLimitException.class, () -> filmController.addFilm(film2),
                "Контроллер пропустил фильм с описанием более 200 символов");

        final Film film3 = new Film("Gray Terminal", "",
                LocalDate.of(1895, 12, 27), 1);
        assertThrows(DateOfReleaseIsEarlierThan28Dec1895Exception.class, () -> filmController.addFilm(film3),
                "Контроллер пропустил фильм с датой релиза ранее 28 дек 1895");

        final Film film4 = new Film("Gray Terminal", "", LocalDate.now(), 0);
        assertThrows(NonPositiveDurationException.class, () -> filmController.addFilm(film4),
                "Контроллер пропустил фильм с неположительной продолжительностью");

    }

    @Test
    void updateFilm() {
        filmController.addFilm(new Film("Millennium Actress", "", LocalDate.now(), 1));

        final Film film1 = new Film(1, "", "Hollow", LocalDate.now(), 1);
        assertThrows(NoTitleException.class, () -> filmController.updateFilm(film1), "Контроллер " +
                "пропустил фильм с пустым названием");

        final Film film2 = new Film(1, "Gray Terminal", "H3154l173!".repeat(22), LocalDate.now(),
                1);
        assertThrows(DescriptionLengthOutOf200ChLimitException.class, () -> filmController.updateFilm(film2),
                "Контроллер пропустил фильм с описанием более 200 символов");

        final Film film3 = new Film(1, "Gray Terminal", "",
                LocalDate.of(1895, 12, 27), 1);
        assertThrows(DateOfReleaseIsEarlierThan28Dec1895Exception.class, () ->
                        filmController.updateFilm(film3), "Контроллер пропустил фильм с датой релиза ранее 28" +
                " дек 1895");

        final Film film4 = new Film(1, "Gray Terminal", "", LocalDate.now(), 0);
        assertThrows(NonPositiveDurationException.class, () -> filmController.updateFilm(film4),
                "Контроллер пропустил фильм с неположительной продолжительностью");

        final Film film5 = new Film(2, "Gray Terminal", "", LocalDate.now(), 1);
        assertThrows(NoSuchFilmException.class, () -> filmController.updateFilm(film5),
                "Контроллер пропустил фильм с незарегестрированным id");
    }

    @Test
    void getFilms() {
        final Film film1 = new Film("9", "", LocalDate.now(), 1);
        final Film film2 = new Film("Berserk", "", LocalDate.now(), 1);
        final Film film3 = new Film("Millennium Actress", "", LocalDate.now(), 1);
        final Film newFilm2 = new Film(2, "Berserk", "",
                LocalDate.of(1997, 10, 15), 1);

        filmController.addFilm(film1);
        filmController.addFilm(film2);
        filmController.addFilm(film3);
        filmController.updateFilm(newFilm2);
        assertEquals(List.of(film1, newFilm2, film3), filmController.getFilms(), "Контроллер " +
                "вернул неподходящий список фильмов");
    }
}
