package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface GenreDao {
    Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException;
    Genre getById(int id);
    List<Genre> getAll();
    List<Genre> getFilmGenres(int filmId);
}
