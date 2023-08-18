package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.model.Film.firstFilmRelease;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void addFilm() {
        final Film film1 = new Film("", "Hollow", LocalDate.now(), 1);
        ValidationException exception1 = assertThrows(ValidationException.class, () -> filmController.addFilm(film1),
                "Контроллер " + "пропустил фильм с пустым названием");
        assertEquals("Пустое имя", exception1.getMessage());

        final Film film2 = new Film("Gray Terminal", "H3154l173!".repeat(23), LocalDate.now(), 1);
        ValidationException exception2 = assertThrows(ValidationException.class, () -> filmController.addFilm(film2),
                "Контроллер пропустил фильм с описанием более 200 символов");
        assertEquals("Длина описания более 200 символов", exception2.getMessage());

        final Film film3 = new Film("Gray Terminal", "",
                LocalDate.of(1895, 12, 27), 1);
        ValidationException exception3 = assertThrows(ValidationException.class, () -> filmController.addFilm(film3),
                "Контроллер пропустил фильм с датой релиза ранее 28 дек 1895");
        assertEquals("Дата выпуска фильма до " + firstFilmRelease, exception3.getMessage());

        final Film film4 = new Film("Gray Terminal", "", LocalDate.now(), 0);
        ValidationException exception4 = assertThrows(ValidationException.class, () -> filmController.addFilm(film4),
                "Контроллер пропустил фильм с неположительной продолжительностью");
        assertEquals("Неположительная длина фильма", exception4.getMessage());
    }

    @Test
    void updateFilm() {
        filmController.addFilm(new Film("Millennium Actress", "", LocalDate.now(), 1));

        final Film film1 = new Film(1, "", "Hollow", LocalDate.now(), 1);
        ValidationException exception1 = assertThrows(ValidationException.class, () -> filmController.updateFilm(film1),
                "Контроллер пропустил фильм с пустым названием");
        assertEquals("Пустое имя", exception1.getMessage());

        final Film film2 = new Film(1, "Gray Terminal", "H3154l173!".repeat(22), LocalDate.now(),
                1);
        ValidationException exception2 = assertThrows(ValidationException.class, () -> filmController.updateFilm(film2),
                "Контроллер пропустил фильм с описанием более 200 символов");
        assertEquals("Длина описания более 200 символов", exception2.getMessage());

        final Film film3 = new Film(1, "Gray Terminal", "",
                LocalDate.of(1895, 12, 27), 1);
        ValidationException exception3 = assertThrows(ValidationException.class, () ->
                        filmController.updateFilm(film3), "Контроллер пропустил фильм с датой релиза ранее 28" +
                " дек 1895");
        assertEquals("Дата выпуска фильма до " + firstFilmRelease, exception3.getMessage());

        final Film film4 = new Film(1, "Gray Terminal", "", LocalDate.now(), 0);
        ValidationException exception4 = assertThrows(ValidationException.class, () -> filmController.updateFilm(film4),
                "Контроллер пропустил фильм с неположительной продолжительностью");
        assertEquals("Неположительная длина фильма", exception4.getMessage());

        final Film film5 = new Film(2, "Gray Terminal", "", LocalDate.now(), 1);
        EntityNotFoundException exception5 = assertThrows(EntityNotFoundException.class, () ->
                        filmController.updateFilm(film5), "Контроллер пропустил фильм с незарегестрированным" +
                " id");
        assertEquals(Film.class.toString(), exception5.getMessage());
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
