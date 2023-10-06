package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.Constants.USER;

@Repository("userDbStorage")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        final SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());

        return user;
    }

    @Override
    public User updateUser(User user) {
        findUser(user.getId());
        final String sql = "update users set email = ?, login = ?, name = ?, birthday = ? where id = ?";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public Collection<User> getUsers() {
        String sql = "select id, email, login, name, birthday from users";

        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User findUser(int id) {
        String sql = "select * from users where id =  ?";

        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(USER, id);
        }

    }

    public void addFriend(int userId, int friendId) {
        findUser(friendId);
        String sql = "insert into user_friend (user_id, friend_id) " +
                "select ?, ? " +
                "where " +
                "not exists (select user_id, friend_id from user_friend " +
                "where user_id = ? and friend_id = ?)";

        jdbcTemplate.update(sql, userId, friendId, userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        String sql = "delete from user_friend where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        String sql = "select * from user_friend as uf inner join users as u " +
                "on uf.friend_id = u.id where uf.user_id = ?";;

        return jdbcTemplate.query(sql, this::mapRowToUser, userId);
    }

    @Override
    public List<User> getCommonFriends(int firstUserId, int secUserId) {
        String sql = "select * from users where id in " +
                "(select uf1.friend_id from user_friend as uf1 " +
                "inner join user_friend as uf2 on uf1.friend_id = uf2.friend_id " +
                "where uf1.user_id = ? and uf2.user_id = ?)";

        return jdbcTemplate.query(sql, this::mapRowToUser, firstUserId, secUserId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNumber) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
