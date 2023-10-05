package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.Constants.FILM;

@Repository("filmDbStorage")
@Primary
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userStorage;
    private final GenreDao genreDao;
    private final MpaDao mpaDao;

    @Override
    public void addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        insertFilmGenres(film);
    }


    @Override
    public Film updateFilm(Film film) {
        findFilm(film.getId());

        String sql = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "where id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        deleteFilmGenres(film.getId());
        insertFilmGenres(film);

        return findFilm(film.getId()).get();
    }

    @Override
    public Collection<Film> getFilms() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> findFilm(int id) {
        String sql = "select * from films where id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id));
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(FILM, id);
        }
    }

    private void insertFilmGenres(Film film) {
        List<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty()) {
            String sql = "insert into film_genre (film_id, genre_id) " +
                    "select ?, ? " +
                    "where " +
                    "not exists (select film_id, genre_id from film_genre " +
                    "where film_id = ? and genre_id = ?)";
            for (Genre genre : genres) {
                jdbcTemplate.update(sql, film.getId(), genre.getId(), film.getId(), genre.getId());
            }
        }
    }

    private void deleteFilmGenres(int filmId) {
        String sql = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    public void likeFilm(int userId, int filmId) {
        userStorage.findUser(userId);
        findFilm(filmId);
        String sql = "insert into film_like (film_id, user_id) " +
                "select ?, ? " +
                "where " +
                "not exists (select film_id, user_id from film_like " +
                "where film_id = ? and user_id = ?)";
        jdbcTemplate.update(sql, filmId, userId, filmId, userId);
    }

    public void unlikeFilm(int userId, int filmId) {
        userStorage.findUser(userId);
        findFilm(filmId);
        String sql = "delete from film_like where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public List<Film> getMostLikedFilms(int count) {
        String sql = "select * from films as f left join (" +
                "select film_id, count(film_id) as likes " +
                "from film_like " +
                "group by film_id " +
                ") as l on f.id = l.film_id order by likes desc " +
                "limit ?";
        return jdbcTemplate.query(sql, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet resultSet, int ronNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .genres(genreDao.getFilmGenres(resultSet.getInt("id")))
                .mpa(mpaDao.getById(resultSet.getInt("mpa_id")))
                .build();
    }
}
