package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.USER;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;
    private int nextUserId = 1;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public User addUser(User user) {
        user.setId(nextUserId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUserRegistration(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public List<User> getFriends(int userId) {
        return users.get(userId).getFriends().stream().map(users::get).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public User validateAndGetUserIfRegistered(int id) {
        final User user = users.get(id);
        if (user == null) {
            throw new EntityNotFoundException(USER, id);
        }
        return user;
    }

    @Override
    public void validateUserRegistration(int userId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException(USER, userId);
        }
    }
}
