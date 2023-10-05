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
@Primary
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
    public Optional<User> findUser(int id) {
        String sql = "select * from users where id =  ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToUser, id));
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
        final List<User> firstUserFriends = getFriends(firstUserId);
        final List<User> secUserFriends = getFriends(secUserId);

        final List<User> commonFriends = new ArrayList<>();
        List<User> fewerFriends;
        List<User> moreFriends;

        if (firstUserFriends.size() <= secUserFriends.size()) {
            fewerFriends = firstUserFriends;
            moreFriends = secUserFriends;
        } else {
            fewerFriends = secUserFriends;
            moreFriends = firstUserFriends;
        }

        for (User friend : fewerFriends) {
            if (moreFriends.contains(friend)) {
                commonFriends.add(friend);
            }
        }

        return commonFriends;
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
