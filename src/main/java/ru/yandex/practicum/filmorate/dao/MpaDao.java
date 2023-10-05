package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface MpaDao {
    Mpa getById(int id);
    List<Mpa> getAll();
    Mpa mapRowToMpa(ResultSet resultSet, int ronNum) throws SQLException;
}
