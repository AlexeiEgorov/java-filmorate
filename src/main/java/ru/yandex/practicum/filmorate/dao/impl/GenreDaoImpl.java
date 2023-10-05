package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ru.yandex.practicum.filmorate.Constants.GENRE;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getById(int id) {
        String sql = "select * from genres where id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(GENRE, id);
        }
    }

    @Override
    public List<Genre> getAll() {
        String sql = "select * from genres";

        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    @Override
    public List<Genre> getFilmGenres(int filmId) {
        String sql = "select * from film_genre as fg " +
                "inner join genres as g on fg.genre_id=g.id where fg.film_id = ?";

        return jdbcTemplate.query(sql, this::mapRowToGenre, filmId);
    }

    @Override
    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
