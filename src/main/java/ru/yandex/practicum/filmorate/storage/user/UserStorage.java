package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    Collection<User> getUsers();

    User getUser(int id);

    List<User> getFriends(int userId);

    User validateAndGetUserIfRegistered(int id);

    void validateUserRegistration(int userId);
}
