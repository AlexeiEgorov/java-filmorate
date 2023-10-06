package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ru.yandex.practicum.filmorate.Constants.MPA;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getById(int id) {
        String sql = "select * from mpa where id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(MPA, id);
        }
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "select * from mpa";

        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Mpa mapRowToMpa(ResultSet resultSet, int ronNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
