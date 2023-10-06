package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public interface GenreDao {
    Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException;

    Genre getById(int id);

    List<Genre> getAll();

    Map<Integer, LinkedHashSet<Genre>> getFilmsGenres(List<Film> films);

    int[] genresBatchInsert(Film film);
}
