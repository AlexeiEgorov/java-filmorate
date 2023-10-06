package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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
    public Map<Integer, LinkedHashSet<Genre>> getFilmsGenres(List<Film> films) {
        Map<Integer, LinkedHashSet<Genre>> filmsGenres = new LinkedHashMap<>();
        String sql = "select * from film_genre as fg " +
                "left join genres as g on fg.genre_id=g.id where fg.film_id in (%s)";
        String inSql = films.stream().map(film -> Integer.toString(film.getId()))
                .collect(Collectors.joining(","));

        List<Genre> genres = jdbcTemplate.query(
                    String.format(sql, inSql),
                    (rs, rowNum) ->  {
                        Genre genre = new Genre(rs.getInt("id"), rs.getString("name"));
                        if (filmsGenres.get(rs.getInt("film_id")) == null) {
                            LinkedHashSet<Genre> fGenres = new LinkedHashSet<>();
                            fGenres.add(genre);
                            filmsGenres.put(rs.getInt("film_id"), fGenres);
                        } else {
                            filmsGenres.get(rs.getInt("film_id")).add(genre);
                        }
                        return genre;
                    });
        return filmsGenres;
    }

    @Override
    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Map<String, Set<String>> fir = new HashMap<>();
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public int[] genresBatchInsert(Film film) {
        List<Genre> genres = new ArrayList<>(film.getGenres());

        return this.jdbcTemplate.batchUpdate(
                "insert into film_genre (film_id, genre_id) values(?,?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genres.get(i).getId());
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }
}
